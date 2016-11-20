/**
 * Created by Ram_Thirupathy on 8/9/2016.
 */
package com.ramkt.mobiAuto.utils;

import java.util.ArrayList;

import com.ramkt.mobiAuto.scriptObjects.Environment;
import com.ramkt.mobiAuto.scriptObjects.Events;
import com.ramkt.mobiAuto.scriptObjects.Module;
import com.ramkt.mobiAuto.scriptObjects.Modules;
import com.ramkt.mobiAuto.scriptObjects.Result;
import com.ramkt.mobiAuto.scriptObjects.TestCases;
import com.ramkt.mobiAuto.scriptObjects.TestEvents;

public interface ScriptReader {
	public String mClassName = "ScriptReader: ";
	public Environment getEnvironment(Environment environment) throws Exception;
	public Events getEvents(Events events)throws Exception;
	public Modules getModules(Modules modules)throws Exception;
	public Module getModule(Module module,String moduleName)throws Exception;
	public ArrayList<TestCases> getTestCases(String moduleName)throws Exception;
	public TestEvents getTestEvents(TestEvents testEvent,String element,String action,String input,String description)throws Exception;
	public Result getResult(Result result,String resultVal,String resultDesc,String assertExpected)throws Exception;
}
