/**
 * Created by Ram_Thirupathy on 8/9/2016.
 */
package com.ramkt.mobiAuto.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.ramkt.mobiAuto.configuration.MessageConstants;
import com.ramkt.mobiAuto.scriptObjects.Environment;
import com.ramkt.mobiAuto.scriptObjects.Events;
import com.ramkt.mobiAuto.scriptObjects.Module;
import com.ramkt.mobiAuto.scriptObjects.Modules;
import com.ramkt.mobiAuto.scriptObjects.Result;
import com.ramkt.mobiAuto.scriptObjects.TestCases;
import com.ramkt.mobiAuto.scriptObjects.TestEvents;
import com.ramkt.mobiAuto.scriptObjects.Events.EVENTSKEY;

public class ExcelReader implements ScriptReader{
	Workbook mWorkBook = null;
	LinkedHashMap<String, Sheet> mWorkSheetMap = new LinkedHashMap<String, Sheet>();
	
	public ExcelReader(String path) throws BiffException, IOException{
		Workbook workbook = Workbook.getWorkbook(new File(path));
		Reporting.getReporting().info(mClassName+"workbook "+workbook+" path "+path);
		Sheet[] sheets = workbook.getSheets();
		int noOfSheets = sheets.length;
		Reporting.getReporting().info(mClassName+ "No Of Sheets "+noOfSheets);
		for(int i=0;i<noOfSheets;i++){
			Sheet sheet = sheets[i];
			mWorkSheetMap.put(sheet.getName(),sheet);
			Reporting.getReporting().info(mClassName+"Sheet name "+sheet.getName());
		}
	}
	
	public Sheet getSheet(String sheetName){
		Reporting.getReporting().info(mClassName+"Get Sheet "+sheetName);
		return mWorkSheetMap.get(sheetName);
	}
	
	public Environment getEnvironment(Environment environment) throws Exception{
		Sheet environmentSheet = getSheet(environment.getClass().getSimpleName());
		Reporting.getReporting().info(mClassName+"Get EnvironmentSheet "+environmentSheet);
		environment.setmPlatformName(environmentSheet.getCell(0,1).getContents().trim());
		environment.setmDeviceName(environmentSheet.getCell(1,1).getContents().trim());
		environment.setmAppPath(environmentSheet.getCell(2,1).getContents().trim());
		environment.setmAppPackage(environmentSheet.getCell(3,1).getContents().trim());
		environment.setmAppiumServerPath(environmentSheet.getCell(4,1).getContents().trim());
		environment.setmAppiumVersion(environmentSheet.getCell(5,1).getContents().trim());
		environment.setmElementWaitTime(Integer.valueOf(environmentSheet.getCell(6,1).getContents().trim()));
		environment.setmCommandTimeout(environmentSheet.getCell(7,1).getContents().trim());
		environment.setmEvents(getEvents(new Events()));
		return environment;
	}
	
	public Events getEvents(Events events) throws Exception{
		String eventName = "";
		try{
			Sheet eventsSheet = getSheet(events.getClass().getSimpleName());
			Reporting.getReporting().info(mClassName+"Events Sheet "+eventsSheet);
			HashMap<EVENTSKEY, Integer> eventConfiguration =new HashMap<EVENTSKEY, Integer>(); 
			int rows = eventsSheet.getRows();
			for(int i=1;i<rows;i++){
				eventName = eventsSheet.getCell(0,i).getContents().trim();
				if(eventName!=null && !eventName.isEmpty()){
					eventConfiguration.put(EVENTSKEY.valueOf(eventName),Integer.valueOf(eventsSheet.getCell(1,i).getContents().trim()));
				}else{
					break;
				}
			}
			events.setEventConfiguration(eventConfiguration);
		}catch(IllegalArgumentException e){
			Reporting.getReporting().exception(e, MessageConstants.NO_SUCH_EVENT+ eventName);
		}
		return events;
	}
	
	public Modules getModules(Modules modules) throws Exception{
		try{
			ArrayList<Module> eModules = new ArrayList<Module>();
			Sheet modulesSheet = getSheet(modules.getClass().getSimpleName());
			int rows = modulesSheet.getRows();
			for(int i=1;i<rows;i++){
				String execFlag = modulesSheet.getCell(1,i).getContents();
				if(execFlag!=null && !execFlag.isEmpty() && execFlag.trim().equalsIgnoreCase("Y")){
					Module module = getModule(new Module(),modulesSheet.getCell(0,i).getContents().trim());
					Reporting.getReporting().info(mClassName+"Add module"+module.getmExecutableTestCases().size());
					if(!module.getmExecutableTestCases().isEmpty()){
						eModules.add(module);
					}
				}
			}
			modules.setmModules(eModules);
		}catch(ArrayIndexOutOfBoundsException e){
			Reporting.getReporting().exception(e, MessageConstants.TEST_SCRIPT_MODULES_ISSUE);
		}catch(NullPointerException e){
			Reporting.getReporting().exception(e, MessageConstants.TEST_SCRIPT_MODULES_ISSUE);
		}
		return modules;
	}
	
	public Module getModule(Module module,String moduleName) throws Exception{
		try{
			module.setmModuleName(moduleName);
			module.setmExecutableTestCases(getTestCases(moduleName));
		}catch(Exception e){
			Reporting.getReporting().exception(e, MessageConstants.TEST_SCRIPT_MODULE_ISSUE+moduleName);
		}
		return module;
	}
	
	public ArrayList<TestCases> getTestCases(String moduleName) throws Exception{
		Sheet moduleSheet = getSheet(moduleName);
		if(moduleSheet==null){
			Reporting.getReporting().exception(new MobAutoException(MessageConstants.MODULE_SHEET_ERROR),MessageConstants.MODULE_SHEET_ERROR);
		}
		int rows = moduleSheet.getRows();
		Reporting.getReporting().info(mClassName+"Rows "+rows+" Cols "+moduleSheet.getColumns());
		ArrayList<TestCases> executableTestCases = new ArrayList<TestCases>();
		for(int i=1;i<rows;i++){
			TestCases testCase = getTestCase(new TestCases(),moduleSheet,i);
			Reporting.getReporting().info(mClassName+moduleName+ " Test case row: "+i);
			if(testCase.getmSNo()<=0){
				Reporting.getReporting().info(mClassName+"break");
				break;
			}
			if(testCase.ismExecFlag()){
				executableTestCases.add(testCase);
			}
			i = i + testCase.getmEvents().size() -1;//test case can have more than one event
		}
		return executableTestCases;
	}
	
	private TestCases getTestCase(TestCases testCase,Sheet moduleSheet, int rowNum) throws Exception {
		String no = moduleSheet.getCell(0,rowNum).getContents();
		Reporting.getReporting().info(mClassName+"Test no"+no);
		String tcase = null;
		try{
			if(no!=null && !no.isEmpty()){
				ArrayList<TestEvents> events = new ArrayList<TestEvents>();
				String execFlag = moduleSheet.getCell(7,rowNum).getContents().trim();
				testCase.setmExecFlag(execFlag!=null && !execFlag.isEmpty() && execFlag.equalsIgnoreCase("Y"));
				testCase.setmSNo(Integer.valueOf(no.trim()));
				tcase = moduleSheet.getCell(1,rowNum).getContents();
				testCase.setmTestCase(tcase);
				testCase.setmDescription(moduleSheet.getCell(2,rowNum).getContents());
				testCase.setmResult(getResult(new Result(),moduleSheet.getCell(8,rowNum).getContents().trim(),moduleSheet.getCell(9,rowNum).getContents(),moduleSheet.getCell(10,rowNum).getContents().trim()));
				testCase.setmTestCaseID( moduleSheet.getCell(11,rowNum).getContents());
				testCase.setmScenario(moduleSheet.getCell(12,rowNum).getContents());
				events.add(getTestEvents(new TestEvents(),moduleSheet.getCell(3,rowNum).getContents().trim(), moduleSheet.getCell(4,rowNum).getContents().trim(),moduleSheet.getCell(5,rowNum).getContents().trim(),moduleSheet.getCell(6,rowNum).getContents()));
				int nextRow = rowNum+1;
				String nextRowNo = moduleSheet.getCell(0,nextRow).getContents();
				Reporting.getReporting().info(mClassName+"Next Row No "+nextRowNo);
				while(nextRowNo==null || nextRowNo.isEmpty() ){
					String elements = moduleSheet.getCell(3,nextRow).getContents().trim();
					String actions = moduleSheet.getCell(4,nextRow).getContents().trim();
					Reporting.getReporting().info(mClassName+"Next Row No elements "+elements+" ,actions "+actions);
					if((elements!=null && !elements.isEmpty()) || (actions!=null && !actions.isEmpty())){
						events.add(getTestEvents(new TestEvents(),elements,actions,moduleSheet.getCell(5,nextRow).getContents().trim(),moduleSheet.getCell(6,nextRow).getContents()));
					}else{
						Reporting.getReporting().info(mClassName+"No more test events");
						break;
					}
					nextRow = nextRow+1;
					nextRowNo = moduleSheet.getCell(0,nextRow).getContents();
				}
				testCase.setmEvents(events);
			}
		}catch(Exception e){
			Reporting.getReporting().exception(e, MessageConstants.TEST_SCRIPT_CASE_ISSUE+tcase);
		}
		return testCase;
	}
	
	public TestEvents getTestEvents(TestEvents testEvent,String element,String action,String input,String description)throws Exception {
		Reporting.getReporting().info(mClassName+"Input for the test case "+input);
		try{
			if(element!=null && !element.isEmpty()){
				String[] ele = element.split(",");
				testEvent.setmElementName(ele[0]);
				testEvent.setmElementType(ele[1].toUpperCase());
				testEvent.setmElementPosition(Integer.valueOf(ele[2]));
				if(ele.length>3){
					testEvent.setmInnerElementName(ele[3]);
					testEvent.setmInnerIdentifierType(ele[4]);
					testEvent.setmInnerElementPosition(Integer.valueOf(ele[5]));
				}
			}
			testEvent.setmAction(action.toUpperCase());
			testEvent.setmInput(input);
			testEvent.setmEventDescription(description);
		}catch(ArrayIndexOutOfBoundsException e){
			Reporting.getReporting().exception(e, MessageConstants.TEST_SCRIPT_ELEMENT_ISSUE+element);
		}catch(NullPointerException e){
			Reporting.getReporting().exception(e,  MessageConstants.TEST_SCRIPT_ELEMENT_ISSUE+element);
		}
		return testEvent;
	}
	
	public Result getResult(Result result,String resultVal,String resultDesc,String assertExpected) throws Exception {
		Reporting.getReporting().info(mClassName+"Test Result "+resultVal+" "+assertExpected);
		try{
			if(resultVal!=null && !resultVal.isEmpty()){
				String[] res = resultVal.split(",");
				result.setmAction(res[0]);
				String element = res[1].replace('{',' ').replace('}', ' ').trim();//can be replaced with regex later
				String[] eles = element.split(";");
				result.setmElement(Arrays.asList(eles));
				result.setmIdentifierType(res[2].toUpperCase());
				result.setmAssertExpected(Boolean.valueOf(assertExpected));
				result.setmDescription(resultDesc);
			}
		}catch(ArrayIndexOutOfBoundsException e){
			Reporting.getReporting().exception(e, MessageConstants.TEST_SCRIPT_RESULT_ISSUE+resultDesc);
		}catch(NullPointerException e){
			Reporting.getReporting().exception(e,  MessageConstants.TEST_SCRIPT_RESULT_ISSUE+resultDesc);

		}
		return result;
	}

}


