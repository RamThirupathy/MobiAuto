/**
 * Created by Ram_Thirupathy on 8/18/2016.
 */
package com.ramkt.mobiAuto.configuration;


public class MessageConstants {
	public static String UNREACHABLE_BROWSER = "Unable to run test case, Please start Appium server.";
	public static String SESSION_NOT_CREATED = "Unable to run test case, Please re-start Appium server.";
	public static String GENERAL_EXCEPTION = "Exception occured, Please try again.";
	public static String MODULE_SHEET_ERROR = "Module sheet name is not matching with the names in Events sheet. Names are case sensitive.";
	public static String MODULE_EXCEPTION = "Unable to execute module: ";
	public static String TEST_CASE_EXCEPTION = "Unable to execute test case: ";
	public static String TEST_EVENT_EXCEPTION = "Unable to execute test event: ";
	public static String TEST_ELEMENT_NOT_FOUND = "Element not found: ";
	public static String TEST_RESULT_EXCEPTION = "Unable to validate test result: ";
	public static String NO_SUCH_EVENT ="Unable to recogonize the event: ";
	public static String NO_SUCH_ACTION ="Unable to recogonize the action: ";
	public static String ELEMENT_MISSING ="Element is missing in the test event: ";
	public static String ELEMENT_POSITION_ISSUE ="Element position is wrong in the test event: ";
	public static String NO_SUCH_IDENTIFIER ="Unable to recogonize the identifier: ";
	public static String TEST_SCRIPT_ELEMENT_ISSUE ="Test Script provided is not proper: Check test element ";
	public static String TEST_SCRIPT_CASE_ISSUE ="Test Script provided is not proper: Check test case ";
	public static String TEST_SCRIPT_MODULE_ISSUE ="Test Script provided is not proper: Check test module ";
	public static String TEST_SCRIPT_MODULES_ISSUE ="Test Script provided is not proper: Check list of modules ";
	public static String TEST_SCRIPT_RESULT_ISSUE ="Test Script provided is not proper: Check test case result ";
	public static String ACTION_FAILURE ="Unable to perform the action ";
	public static String APPIUM_SERVER_URL_ISSUE ="Appium server remote address is incorrect ";
	public static String SCROLL_TEXT_ISSUE = "Unable to scroll to the text, text not available: ";
	public static String SCREEN_SHOT_ISSUE = "Unable to take screen shot";
	public static String VIDEO_VALIDATE_ISSUE = "Unable to validate video";
	public static String IMAGE_READ_ISSUE = "Unable to access the image in ";
}
