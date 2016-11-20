/**
 * Created by Ram_Thirupathy on 8/18/2016.
 */
package com.ramkt.mobiAuto.scriptObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ramkt.mobiAuto.configuration.MessageConstants;
import com.ramkt.mobiAuto.driver.MobDriver;
import com.ramkt.mobiAuto.utils.Reporting;

public class TestEvents {
	private String mClassName = this.getClass().getSimpleName()+": ";
	public enum IDENTIFIER {
		ID("ID"),CLASSNAME("CLASSNAME"),NAME("NAME");
		private IDENTIFIER(String type) {
		}
	}
	private String mElementName;
	private String mAction;
	private String mInput;
	private String mElementType;
	private int mElementPosition;
	private String mEventDescription;
	private String mInnerElementName = "";
	private String mInnerIdentifierType;
	private int mInnerElementPosition;
	public TestEvents(){
	}

	public void setmElementName(String mElementName) {
		this.mElementName = mElementName;
	}
	public void setmAction(String mAction) {
		this.mAction = mAction;
	}
	public void setmInput(String mInput) {
		this.mInput = mInput;
	}
	public void setmElementType(String mElementType) {
		this.mElementType = mElementType;
	}
	public void setmEventDescription(String mEventDescription) {
		this.mEventDescription = mEventDescription;
	}
	public int getmElementPosition() {
		return mElementPosition;
	}

	public void setmElementPosition(int mElementPosition) {
		this.mElementPosition = mElementPosition;
	}
	public String getmElementName() {
		return mElementName; 
	}
	public String getmAction() {
		return mAction;
	}
	public String getmInput() {
		return mInput;
	}
	public String getmElementType() {
		return mElementType;
	}
	public String getmEventDescription() {
		return mEventDescription;
	}
	public String getmInnerElementName() {
		return mInnerElementName;
	}

	public void setmInnerElementName(String mInnerElementName) {
		this.mInnerElementName = mInnerElementName;
	}

	public int getmInnerElementPosition() {
		return mInnerElementPosition;
	}

	public void setmInnerElementPosition(int mInnerElementPosition) {
		this.mInnerElementPosition = mInnerElementPosition;
	}
	
	public String getmInnerIdentifierType() {
		return mInnerIdentifierType;
	}

	public void setmInnerIdentifierType(String mInnerIdentifierType) {
		this.mInnerIdentifierType = mInnerIdentifierType;
	}

	public WebElement getmElement(MobDriver driver) throws Exception{
		if(mElementType==null){
			return null;
		}
		WebElement element = null;
		try{
			Reporting.getReporting().info(mClassName+"Event identifier "+mElementType+" "+mElementName+" "+mElementPosition+" "+mInnerElementName);
			element = find(mElementName, mElementType, mElementPosition, driver,element);
			if(mInnerElementName!=null && !mInnerElementName.isEmpty()){
				element = find(mInnerElementName, mInnerIdentifierType, mInnerElementPosition, driver,element);
			}
		}catch(IllegalArgumentException e){
			Reporting.getReporting().exception(e, MessageConstants.NO_SUCH_IDENTIFIER+ mElementType);
		}
		return element;
	}

	private WebElement find(String elementName,String elementType, int elementPosition,MobDriver driver,WebElement element){
		IDENTIFIER identifiter = IDENTIFIER.valueOf(elementType);
		switch (identifiter) {
		case ID:
			driver.explicitWaitID(elementName);
			element =driver.findElement(By.id(elementName),element);
			break;
		case CLASSNAME:
			driver.explicitWaitClassName(elementName);
			List<WebElement> elements = driver.findElementsByClassName(elementName,element);
			Reporting.getReporting().info(mClassName+"Elements size "+elements.size());
			if(elementPosition<0){
				element = elements.get(elements.size()+elementPosition);
			}else{
				element = elements.get(elementPosition);
			}
			break;
		case NAME:
			driver.explicitWaitName(elementName);
			element = driver.findElement(By.name(elementName),element);
			break;
		default:
			break;
		}
		return element;
	}
	public int getmLastElementIndex(MobDriver driver){
		driver.explicitWaitClassName(mElementName);
		WebElement element = find(mElementName, mElementType, mElementPosition, driver,null);
		List<WebElement> elements = driver.findElementsByClassName(mInnerElementName,element);
		Reporting.getReporting().info(mClassName+"Index Elements size "+elements.size()+" "+mInnerElementName+" "+mElementName);
		return elements.size();
	}
}
