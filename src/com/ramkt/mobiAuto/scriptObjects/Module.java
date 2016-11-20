/**
 * Created by Ram_Thirupathy on 8/18/2016.
 */
package com.ramkt.mobiAuto.scriptObjects;

import java.util.ArrayList;

public class Module {
	private String mModuleName;
	private ArrayList<TestCases> mExecutableTestCases = new ArrayList<TestCases>();
	public Module(){
	}

	public void setmModuleName(String mModuleName) {
		this.mModuleName = mModuleName;
	}

	public void setmExecutableTestCases(ArrayList<TestCases> mExecutableTestCases) {
		this.mExecutableTestCases = mExecutableTestCases;
	}


	public String getmModuleName() {
		return mModuleName;
	}
	public ArrayList<TestCases> getmExecutableTestCases() {
		return mExecutableTestCases;
	}
}
