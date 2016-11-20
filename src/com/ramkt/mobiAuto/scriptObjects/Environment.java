/**
 * Created by Ram_Thirupathy on 8/18/2016.
 */
package com.ramkt.mobiAuto.scriptObjects;

import com.ramkt.mobiAuto.scriptObjects.Events.EVENTSKEY;

public class Environment {
	private String mAppPath;
	private String mPlatformName;
	private String mDeviceName;
	private String mAppPackage;
	private String mAppiumServerPath;
	private String mAppiumVersion;
	private int mElementWaitTime;
	private String mCommandTimeout;
	private Events mEvents;
	
	public Environment(){
	}
	
	public void setmAppPath(String mAppPath) {
		this.mAppPath = mAppPath;
	}
	public void setmPlatformName(String mPlatformName) {
		this.mPlatformName = mPlatformName;
	}
	public void setmDeviceName(String mDeviceName) {
		this.mDeviceName = mDeviceName;
	}
	public void setmAppPackage(String mAppPackage) {
		this.mAppPackage = mAppPackage;
	}
	public void setmAppiumServerPath(String mAppiumServerPath) {
		this.mAppiumServerPath = mAppiumServerPath;
	}
	public void setmAppiumVersion(String mAppiumVersion) {
		this.mAppiumVersion = mAppiumVersion;
	}
	public void setmElementWaitTime(int mElementWaitTime) {
		this.mElementWaitTime = mElementWaitTime;
	}
	public void setmCommandTimeout(String mCommandTimeout) {
		this.mCommandTimeout = mCommandTimeout;
	}
	public void setmEvents(Events mEvents) {
		this.mEvents = mEvents;
	}
	
	public Events getmEvent() {
		return mEvents;
	}
	
	public int getmEvent(EVENTSKEY eventName) {
		return mEvents.getConfigurationValue(eventName);
	}
	
	public String getmAppiumServerPath() {
		return mAppiumServerPath;
	}
	
	public String getmAppiumVersion() {
		return mAppiumVersion;
	}
	public int getmElementWaitTime() {
		return mElementWaitTime;
	}
	public String getmCommandTimeout() {
		return mCommandTimeout;
	}
	public String getmAppPath() {
		return mAppPath;
	}
	public String getmPlatformName() {
		return mPlatformName;
	}
	public String getmDeviceName() {
		return mDeviceName;
	}
	
	public String getmAppPackage() {
		return mAppPackage;
	}
}
