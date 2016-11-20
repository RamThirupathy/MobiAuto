/**
 * Created by Ram_Thirupathy on 8/18/2016.
 */
package com.ramkt.mobiAuto.scriptObjects;

import java.util.List;

public class Result {
	private String mAction;
	private List<String> mElement;
	private String mIdentifierType;
	private boolean mAssertExpected;
	private String mDescription;
	public void setmAction(String mAction) {
		this.mAction = mAction;
	}

	public void setmElement(List<String> mElement) {
		this.mElement = mElement;
	}

	public void setmIdentifierType(String mIdentifierType) {
		this.mIdentifierType = mIdentifierType;
	}

	public void setmAssertExpected(boolean mAssertExpected) {
		this.mAssertExpected = mAssertExpected;
	}

	public void setmDescription(String mDescription) {
		this.mDescription = mDescription;
	}
	public Result(){
	}

	public String getmDescription() {
		return mDescription;
	}

	public String getmAction() {
		return mAction;
	}
	public List<String> getmElement() {
		return mElement;
	}
	public String getmIdentifierType() {
		return mIdentifierType;
	}
	public boolean getmAssertExpected() {
		return mAssertExpected;
	}
}
