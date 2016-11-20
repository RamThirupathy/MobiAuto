/**
 * Created by Ram_Thirupathy on 8/9/2016.
 */
package com.ramkt.mobiAuto.testController;

import io.appium.java_client.SwipeElementDirection;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import com.ramkt.mobiAuto.configuration.MessageConstants;
import com.ramkt.mobiAuto.driver.MobDriver;
import com.ramkt.mobiAuto.scriptObjects.Result;
import com.ramkt.mobiAuto.scriptObjects.TestCases;
import com.ramkt.mobiAuto.scriptObjects.TestEvents;
import com.ramkt.mobiAuto.scriptObjects.TestEvents.IDENTIFIER;
import com.ramkt.mobiAuto.utils.ReportData;
import com.ramkt.mobiAuto.utils.Reporting;


public class TestScriptRunner {
	private String mClassName = this.getClass().getSimpleName()+": ";
	enum ACTION {
		TAP("TAP"),CLICK("CLICK"),SENDKEYS("SENDKEYS"),SWIPE_RIGHT("SWIPE_RIGHT"),SWIPE_LEFT("SWIPE_LEFT"),
		SCROLL("SCROLL"),BACK("BACK"),ZOOM("ZOOM"),PINCH("PINCH"),WAIT("WAIT"),SCREENSHOT("SCREENSHOT"),
		PRINT("PRINT"),PLAY_VIDEO("PLAY_VIDEO"),REPEAT("REPEAT"),SWIPE_SCREEN_LEFT("SWIPE_SCREEN_LEFT")
		,SWIPE_SCREEN_RIGHT("SWIPE_SCREEN_RIGHT");
		private ACTION(String type) {
		}
	}
	private MobDriver mDriver;
	public TestScriptRunner(MobDriver driver){
		this.mDriver = driver;
	}

	public void run(ArrayList<TestCases> testCases,ReportData data) throws Exception {
		String currentTestCase = "";
		try{
			for(TestCases testCase: testCases)
			{
				currentTestCase = testCase.getmDescription();
				data.setmTestCaseStatus(currentTestCase,"EXECUTING");
				Reporting.getReporting().info(mClassName+"Execute testCase "+currentTestCase);
				Result expectedResult = testCase.getmResult();
				String currentTestEvent = "";
				try{
					for(TestEvents event:testCase.getmEvents()){
						currentTestEvent = event.getmEventDescription();
						executeEvent(event,testCase,data);
					}
				}catch(Exception e){
					data.updatemTestEventStatus(currentTestCase,currentTestEvent,"FAIL");
					Reporting.getReporting().exception(e,MessageConstants.TEST_EVENT_EXCEPTION+currentTestEvent);
				}
				Reporting.getReporting().info(mClassName+"Expected "+expectedResult.getmAssertExpected());
				if(expectedResult.getmElement()!=null && !expectedResult.getmElement().isEmpty()){
					boolean actual = validateResult(expectedResult);
					Reporting.getReporting().info(mClassName+"Actual "+actual);
					Assert.assertEquals(expectedResult.getmAssertExpected(), actual);
				}
				Reporting.getReporting().info(mClassName+currentTestCase+" Test case Passed");
				data.setmTestCaseStatus(currentTestCase,"PASS");
			}
		}catch(AssertionError e){
			data.setmTestCaseStatus(currentTestCase,"FAIL");
			Reporting.getReporting().info(mClassName+currentTestCase+" Test case failed");
		}
		catch(Exception e){
			data.setmTestCaseStatus(currentTestCase,"FAIL");
			Reporting.getReporting().exception(e,MessageConstants.TEST_CASE_EXCEPTION+currentTestCase);
		}
	}
	
	private void executeEvent(TestEvents event, TestCases testCase,ReportData data) throws Exception{
		try{
			data.setmTestEventStatus(testCase.getmDescription(),event,"EXECUTING");
			WebElement element = (WebElement)event.getmElement(mDriver);
			ACTION action = ACTION.valueOf(event.getmAction());
			Reporting.getReporting().info(mClassName+"Run action "+element+" "+action);
			switch (action) {
			case TAP:
				Reporting.getReporting().info(mClassName+"Tap "+event.getmInput());
				element.click();
				data.updatemTestEventStatus(testCase.getmDescription(),event.getmEventDescription(),"PASS");
				break;
			case CLICK:
				Reporting.getReporting().info(mClassName+"Click "+event.getmInput());
				element.click();
				data.updatemTestEventStatus(testCase.getmDescription(),event.getmEventDescription(),"PASS");
				break;
			case SENDKEYS:
				Reporting.getReporting().info(mClassName+"Sendkeys "+event.getmInput()+" "+element);
				element.clear();
				element.sendKeys(event.getmInput());
				mDriver.hideKeyboard();
				data.updatemTestEventStatus(testCase.getmDescription(),event.getmEventDescription(),"PASS");
				break;
			case BACK:
				mDriver.back();
				data.updatemTestEventStatus(testCase.getmDescription(),event.getmEventDescription(),"PASS");
				break;
			case SCROLL:
				element = mDriver.scrollTo(event.getmInput());
				data.updatemTestEventStatus(testCase.getmDescription(),event.getmEventDescription(),"PASS");
				break;
			case ZOOM:
				mDriver.zoom(element);
				data.updatemTestEventStatus(testCase.getmDescription(),event.getmEventDescription(),"PASS");
				break;
			case PINCH:
				mDriver.pinch(element);
				data.updatemTestEventStatus(testCase.getmDescription(),event.getmEventDescription(),"PASS");
				break;
			case WAIT:
				data.updatemTestEventStatus(testCase.getmDescription(),event.getmEventDescription(),"PASS");
				break;
			case SWIPE_LEFT:
				element = mDriver.swipe(SwipeElementDirection.LEFT,element,event.getmInput());
				data.updatemTestEventStatus(testCase.getmDescription(),event.getmEventDescription(),"PASS");
				break;
			case SWIPE_RIGHT:
				element = mDriver.swipe(SwipeElementDirection.RIGHT,element,event.getmInput());
				data.updatemTestEventStatus(testCase.getmDescription(),event.getmEventDescription(),"PASS");
				break;
			case SCREENSHOT:
				mDriver.screenShot();
				data.updatemTestEventStatus(testCase.getmDescription(),event.getmEventDescription(),"PASS");
				break;
			case PRINT:
				String text = mDriver.print(element);
				data.updatemTestEventStatus(testCase.getmDescription(),event.getmEventDescription(),text);
				break;
			case PLAY_VIDEO:
				boolean isPlaying = mDriver.validateVideo();
				Reporting.getReporting().info("Has video played? "+isPlaying);
				data.updatemTestEventStatus(testCase.getmDescription(),event.getmEventDescription(),isPlaying?"PASS":"FAIL");
				
				break;
			case REPEAT:
				repeatEvents(testCase, event.getmInput(),data);
				break;
			case SWIPE_SCREEN_LEFT:
				mDriver.swipeScreen(SwipeElementDirection.LEFT);
				data.updatemTestEventStatus(testCase.getmDescription(),event.getmEventDescription(),"PASS");
				break;
			case SWIPE_SCREEN_RIGHT:
				mDriver.swipeScreen(SwipeElementDirection.RIGHT);
				data.updatemTestEventStatus(testCase.getmDescription(),event.getmEventDescription(),"PASS");
				break;
			default:
				break;
			}
		}catch(org.openqa.selenium.NoSuchElementException e){
			Reporting.getReporting().exception(e,MessageConstants.TEST_ELEMENT_NOT_FOUND+event.getmElementName()+" "+event.getmInnerElementName());
		}
		catch(TimeoutException e){
			Reporting.getReporting().exception(e,MessageConstants.NO_SUCH_IDENTIFIER+event.getmElementName()+" "+event.getmInnerElementName());
		}
		catch(NotFoundException e){
			Reporting.getReporting().exception(e,MessageConstants.TEST_ELEMENT_NOT_FOUND+event.getmElementName()+" "+event.getmInnerElementName());
		}catch(IllegalArgumentException e){
			Reporting.getReporting().exception(e, MessageConstants.NO_SUCH_ACTION+ event.getmAction());
		}catch(NullPointerException e){
			e.printStackTrace();
			Reporting.getReporting().exception(e, MessageConstants.ELEMENT_MISSING+ event.getmEventDescription());
		}catch(IndexOutOfBoundsException e){
			Reporting.getReporting().exception(e, MessageConstants.ELEMENT_POSITION_ISSUE+ event.getmEventDescription());
		}

	
	}

	private void repeatEvents(TestCases testCase,String input,ReportData data) throws Exception{
		ArrayList<TestEvents> testEvents = testCase.getmEvents();
		Reporting.getReporting().info("Repeat "+input);
		String[] res = input.split(",");
		int eventStart = Integer.valueOf(res[0]);
		int eventEnd = Integer.valueOf(res[1]);
		String repeater = res[2].replace('{',' ').replace('}', ' ').trim();//can be replaced with regex later
		int listStart = 0;
		int listEnd = 0;
		if(repeater.contains(";")){
			Reporting.getReporting().info("Repeat list scroll-er");
			String[] repeatCondition = repeater.split(";");
			int elementInEvent = Integer.valueOf(repeatCondition[0]);
			listStart = Integer.valueOf(repeatCondition[1]);
			listEnd = 0;
			if(repeatCondition[2].equalsIgnoreCase("E")){
				listEnd = testEvents.get(elementInEvent).getmLastElementIndex(mDriver);
			}else{
				listEnd = Integer.valueOf(repeatCondition[2]);
			}
			if(elementInEvent>=eventStart && elementInEvent<=eventEnd){
				//proceed with repeating test events
				Reporting.getReporting().info("Repeat listStart - procced..");
			}else{
				//throw exception
				Reporting.getReporting().info("Repeat listStart - exception");
			}
			Reporting.getReporting().info("Repeat listStart "+listStart+" listEnd "+listEnd+" elementInEvent "+elementInEvent+" eventStart "+eventStart+" eventEnd "+eventEnd);
			int actualPosition = testEvents.get(listStart).getmElementPosition();
			for(int j=listStart;j<listEnd;j++){
				testEvents.get(elementInEvent).setmInnerElementPosition(j);
				for(int i=eventStart;i<eventEnd;i++){
					TestEvents event = testEvents.get(i);
					executeEvent(event,testCase,data);
				}
			}
			testEvents.get(listStart).setmInnerElementPosition(actualPosition);
		}else{
			Reporting.getReporting().info("Repeat the events");
			listEnd = Integer.valueOf(repeater);
			Reporting.getReporting().info("Repeat events "+listStart+" listEnd "+listEnd+" eventStart "+eventStart+" eventEnd "+eventEnd);
			for(int j=listStart;j<listEnd;j++){
				for(int i=eventStart;i<eventEnd;i++){
					TestEvents event = testEvents.get(i);
					executeEvent(event,testCase,data);
				}
			}
		}
	}

	private boolean validateResult(Result result) throws Exception{
		try{
			Reporting.getReporting().info(mClassName+"Validate result identifier "+result.getmIdentifierType()+" element "+result.getmElement());
			IDENTIFIER identifiter = IDENTIFIER.valueOf(result.getmIdentifierType());
			for(String element: result.getmElement()){
				Reporting.getReporting().info(mClassName+"Element - "+element);
				switch(identifiter){
				case ID:
					mDriver.explicitWaitID(element);
					break;
				case CLASSNAME:
					mDriver.explicitWaitClassName(element);
					break;
				case NAME:
					mDriver.explicitWaitName(element);
					break;
				default:
					break;
				}
			}
			return true;
		}catch(NoSuchElementException e){
			e.printStackTrace();
		}
		catch(TimeoutException e){
			e.printStackTrace();
		}
		catch(NotFoundException e){
			e.printStackTrace();
		}
		catch(Exception e){
			Reporting.getReporting().exception(e,MessageConstants.TEST_RESULT_EXCEPTION+result.getmDescription());
		}
		return false;
	}


}
