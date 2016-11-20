/**
 * Created by Ram_Thirupathy on 8/18/2016.
 */
package com.ramkt.mobiAuto.scriptObjects;

import java.util.ArrayList;

public class TestCases {

	private int mSNo;
	private String mTestCase;
	private String mDescription;
	private String mTestCaseID;
	private String mScenario;
	private boolean mExecFlag;
	private Result mResult;
	private ArrayList<TestEvents> mEvents = new ArrayList<TestEvents>();

	public TestCases(){
	}

	public void setmSNo(int mSNo) {
		this.mSNo = mSNo;
	}

	public void setmTestCase(String mTestCase) {
		this.mTestCase = mTestCase;
	}

	public void setmDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public void setmTestCaseID(String mTestCaseID) {
		this.mTestCaseID = mTestCaseID;
	}

	public void setmScenario(String mScenario) {
		this.mScenario = mScenario;
	}

	public void setmExecFlag(boolean mExecFlag) {
		this.mExecFlag = mExecFlag;
	}

	public void setmResult(Result mResult) {
		this.mResult = mResult;
	}

	public void setmEvents(ArrayList<TestEvents> mEvents) {
		this.mEvents = mEvents;
	}

	public ArrayList<TestEvents> getmEvents() {
		return mEvents;
	}

	public int getmSNo() {
		return mSNo;
	}
	public String getmTestCase() {
		return mTestCase;
	}
	public String getmDescription() {
		return mDescription;
	}
	public String getmTestCaseID() {
		return mTestCaseID;
	}
	public String getmScenario() {
		return mScenario;
	}
	public boolean ismExecFlag() {
		return mExecFlag;
	}
	public Result getmResult() {
		return mResult;
	}

}
