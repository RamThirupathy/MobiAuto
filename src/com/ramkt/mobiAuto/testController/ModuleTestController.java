/**
 * Created by Ram_Thirupathy on 8/9/2016.
 */
package com.ramkt.mobiAuto.testController;

import java.util.ArrayList;
import java.util.List;

import com.ramkt.mobiAuto.configuration.MessageConstants;
import com.ramkt.mobiAuto.scriptObjects.Module;
import com.ramkt.mobiAuto.scriptObjects.Modules;
import com.ramkt.mobiAuto.utils.ReportData;
import com.ramkt.mobiAuto.utils.Reporting;

public class ModuleTestController extends BaseController {
	static List<String> usernames = new ArrayList<String>();
	private String mClassName = this.getClass().getSimpleName()+": ";
	private TestScriptRunner mTestScriptRunner;
	public static void main (String[] args){
		try {
//			new GuiHome();
			new ModuleTestController().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	

	public void start() throws Exception{
		try{
			setUp();
			mTestScriptRunner = new TestScriptRunner(mDriver);
			Modules modules = mScriptReader.getModules(new Modules());
			ExecuteModules(modules.getModules());
		}catch(Exception e){
			e.printStackTrace();
			Reporting.getReporting().halt(e,MessageConstants.GENERAL_EXCEPTION);
			Reporting.getReporting().generateReport();
		}
	}

	private void ExecuteModules(ArrayList<Module> modules) throws Exception{
		String currentModule = "";
		Reporting reporting = Reporting.getReporting();
		ReportData data = null;
		try{
			reporting.info(mClassName+"No of Executable modules" + modules.size());
			for(Module module: modules){
				currentModule = module.getmModuleName();
				reporting.info(mClassName+"Execute module "+currentModule);
				data = reporting.storeReport(currentModule);
				data.setmModuleStatus("EXECUTING");
				mTestScriptRunner.run(module.getmExecutableTestCases(),data);
				data.setmModuleStatus("PASS");
				reporting.generateReport();
				reporting.info(mClassName+currentModule+" Module completed");
			}
		}catch(Exception e){
			data.setmModuleStatus("FAIL");
			reporting.exception(e,MessageConstants.MODULE_EXCEPTION+currentModule);
		}
	}

}
