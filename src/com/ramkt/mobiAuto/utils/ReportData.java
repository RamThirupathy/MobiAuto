/**
 * Created by Ram_Thirupathy on 8/9/2016.
 */
package com.ramkt.mobiAuto.utils;

import java.util.LinkedHashMap;

import com.ramkt.mobiAuto.scriptObjects.TestEvents;

public class ReportData {
	String mModuleName;
	String mModuleStatus;
	LinkedHashMap<String, String> mTestCaseStatus = new LinkedHashMap<String,String>();
	LinkedHashMap<String, LinkedHashMap<String, String>> mTestEventStatus = new LinkedHashMap<String,LinkedHashMap<String, String>>();
	public String getmModuleName() {
		return mModuleName;
	}
	public void setmModuleName(String mModuleName) {
		this.mModuleName = mModuleName;
	}
	public LinkedHashMap<String, String> getmTestCaseStatus() {
		return mTestCaseStatus;
	}
	
	public void setmTestCaseStatus(String testCase,String status) {
		this.mTestCaseStatus.put(testCase, status);
		if(!mTestEventStatus.containsKey(testCase)){
			mTestEventStatus.put(testCase, new LinkedHashMap<String, String>());
		}
	}
	public LinkedHashMap<String, LinkedHashMap<String, String>> getmTestEventStatus() {
		return mTestEventStatus;
	}
	public void setmTestEventStatus(String testCase,TestEvents event,String status) {
		String testEvent = event.getmEventDescription();
		System.out.println("set"+testCase+" "+testEvent+" "+status);
		int i = 0;
		while(this.mTestEventStatus.get(testCase).containsKey(testEvent)){
			i++;
			testEvent = i+". "+testEvent;
		}
		if(i>0){
			event.setmEventDescription(testEvent);
		}
		this.mTestEventStatus.get(testCase).put(testEvent, status);
	}
	
	public void updatemTestEventStatus(String testCase,String testEvent,String status) {
		System.out.println(testCase+" "+testEvent+" "+status);
		this.mTestEventStatus.get(testCase).put(testEvent, status);
	}
	
	public String getmModuleStatus() {
		return mModuleStatus;
	}
	public void setmModuleStatus(String mModuleStatus) {
		this.mModuleStatus = mModuleStatus;
	}
}
