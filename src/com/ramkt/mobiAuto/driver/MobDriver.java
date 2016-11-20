/**
 * Created by Ram_Thirupathy on 8/18/2016.
 */
package com.ramkt.mobiAuto.driver;

import io.appium.java_client.MobileElement;
import io.appium.java_client.SwipeElementDirection;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ramkt.mobiAuto.configuration.MessageConstants;
import com.ramkt.mobiAuto.scriptObjects.Environment;
import com.ramkt.mobiAuto.scriptObjects.Events;
import com.ramkt.mobiAuto.scriptObjects.Events.EVENTSKEY;
import com.ramkt.mobiAuto.utils.Constants;
import com.ramkt.mobiAuto.utils.Reporting;
import com.ramkt.mobiAuto.utils.SikuliUtil;

public class MobDriver {
	private String mClassName = this.getClass().getSimpleName()+": ";
	private WebDriver mDriver;

	private SikuliUtil mSikuli;
	private int mElementWaitDuration;
	private Events mEvents;
	public MobDriver(DesiredCapabilities capabilities,Environment environment) throws Exception {
		try {
			if(environment.getmPlatformName().equalsIgnoreCase("ANDROID")){
				mDriver = new AndroidDriver(new URL(environment.getmAppiumServerPath()), capabilities);
			}else if(environment.getmPlatformName().equalsIgnoreCase("iOS")){
				mDriver = new IOSDriver(new URL(environment.getmAppiumServerPath()), capabilities);
			}else{
				mDriver = new FirefoxDriver();
				Constants.ERROR_IMAGE_WIDTH = 1000;
				Constants.ERROR_IMAGE_HEIGHT = 500;
				mDriver.get(environment.getmAppPath());
			}
			this.mElementWaitDuration = environment.getmElementWaitTime();
			this.mEvents = environment.getmEvent();
		} catch (MalformedURLException e) {
			Reporting.getReporting().exception(e, MessageConstants.APPIUM_SERVER_URL_ISSUE);
		}
	}

	public WebDriver getDriver(){
		return null;
	}

	/*****************************************Action**********************************************/
	public void back(){
		mDriver.navigate().back();
	}

	public WebElement scrollTo(String text) throws Exception{
		try{
			if(mDriver instanceof AndroidDriver){
				return ((AndroidDriver)mDriver).scrollTo(text);
			}else if(mDriver instanceof FirefoxDriver){
				WebElement element = ((FirefoxDriver) mDriver).findElementByName(text);
				((JavascriptExecutor) mDriver).executeScript("arguments[0].scrollIntoView(true);", element);
			}else{
				return ((IOSDriver)mDriver).scrollTo(text);
			}
		}catch(NoSuchElementException e){
			Reporting.getReporting().exception(e, MessageConstants.SCROLL_TEXT_ISSUE+text);
		}
		return null;
	}

	public void zoom(WebElement element){
		if(mDriver instanceof AndroidDriver){
			((AndroidDriver)mDriver).zoom(element);
		}else if(mDriver instanceof FirefoxDriver){
		}else{
			((IOSDriver)mDriver).zoom(element);
		}
	}

	public void pinch(WebElement element){
		if(mDriver instanceof AndroidDriver){
			((AndroidDriver)mDriver).pinch(element);
		}else if(mDriver instanceof FirefoxDriver){
		}else{
			((IOSDriver)mDriver).pinch(element);
		}
	}

	public void hideKeyboard(){
		if(mDriver instanceof AndroidDriver){
			((AndroidDriver)mDriver).hideKeyboard();
		}else if(mDriver instanceof FirefoxDriver){
		}else{
			((IOSDriver)mDriver).hideKeyboard();
		}
	}

	public WebElement swipe(SwipeElementDirection direction,WebElement element,String name){
		Point p = ((MobileElement)element).getCenter();
		int width = element.getSize().width;
		int endX = 0;
		Double offset =  width *.02;
		if(direction==SwipeElementDirection.RIGHT){
			endX = width-offset.intValue();
		}else{
			endX = element.getLocation().getX()- offset.intValue();
			endX = endX>0?endX:offset.intValue();
		}
		if(name!=null && !name.isEmpty()){
			WebElement elementToFind = null;
			while(elementToFind==null){
				try{
					elementToFind = (WebElement) mDriver.findElement(By.name(name));
				}catch(org.openqa.selenium.NoSuchElementException e){
					Reporting.getReporting().info(mClassName+"Looking for element.... "+name);
				}
				driverSwipe(p.getX(),endX,p.getY(),p.getY());
			}
			return elementToFind;
		}else{
			driverSwipe(p.getX(),endX,p.getY(),p.getY());
		}
		return element;
	}

	public void swipeScreen(SwipeElementDirection direction){
		Dimension size = mDriver.manage().window().getSize(); 
		int startX = 1;
		int endX = size.width/2;
		if(direction==SwipeElementDirection.LEFT){
			startX = size.width-startX;
		}
		driverSwipe(startX, endX,0,0);
	}

	private void driverSwipe(int startX,int endX,int startY,int endY){
		Reporting.getReporting().info(mClassName+" Swipe startX "+startX+" endX "+endX+" "+startY);
		if(mDriver instanceof AndroidDriver){
			((AndroidDriver)mDriver).swipe(startX, startY, endX, endY, 500);//duration can be configurable from script sheet later
		}else if(mDriver instanceof FirefoxDriver){
		}else{
			((IOSDriver)mDriver).swipe(startX, startY, endX, endY, 500);//duration can be configurable from script sheet later
		}
	}

	public String print(WebElement element){
		String text = element.getText();
		if(text==null || text.isEmpty()){
			text = element.getAttribute("value");
		}
		Reporting.getReporting().info(mClassName+" "+text);
		return text;
	}


	/**
	 * This is the method which take the screenshot 
	 * @param MobileDriver to take device screenshot.
	 * @return BufferedImage screenshot in bytes.
	 * @throws Exception 
	 */
	public BufferedImage screenShot() throws Exception {
		BufferedImage image = null;
		try{
			if(mDriver instanceof AndroidDriver){
				image = ImageIO.read(((AndroidDriver) mDriver).getScreenshotAs(OutputType.FILE));
			}else if(mDriver instanceof FirefoxDriver){
				image = ImageIO.read(((FirefoxDriver) mDriver).getScreenshotAs(OutputType.FILE));
			}else{
				image = ImageIO.read(((IOSDriver) mDriver).getScreenshotAs(OutputType.FILE));
			}
		}catch(Exception e){
			Reporting.getReporting().exception(e, MessageConstants.SCREEN_SHOT_ISSUE);
		}
		return image;
	}

	public boolean validateVideo() throws Exception{
		try{
			if(mSikuli==null){
				mSikuli = new SikuliUtil();
			}
			boolean isPlaying = false;
			setBaseImage(screenShot());
			int attempt = mEvents.getConfigurationValue(EVENTSKEY.VIDEO_MONITOR_ATTEMPT);
			for(int i=0;i<attempt;i++){
				BufferedImage image = screenShot();
				isPlaying = !mSikuli.compareImage(image);//if image matches then not playing
				Reporting.getReporting().info(mClassName+ "Video validate attempts "+i+" - is playing now? "+isPlaying);
				setBaseImage(image);
			}
			return isPlaying;
		}catch(Exception e){
			Reporting.getReporting().exception(e, MessageConstants.VIDEO_VALIDATE_ISSUE);
		}
		return false;
	}

	private void setBaseImage(BufferedImage image){
		mSikuli.setBaseImage(image);
		try {
			Thread.sleep(mEvents.getConfigurationValue(EVENTSKEY.VIDEO_MONITOR_WAIT));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*****************************************Finder**********************************************/
	public WebElement findElement(By locator,WebElement element){
		if(element!=null){
			return element.findElement(locator);
		}else{
			return mDriver.findElement(locator);
		}
	}
	
	
	public List<WebElement> findElementsByClassName(String className,WebElement element){
		if(element!=null){
			return element.findElements(By.className(className));
		}else{
			return mDriver.findElements(By.className(className));
		}
	}

	/*****************************************waiter**********************************************/
	/**
	 * This is the method which waits till the element has been found.
	 * @param name This is the name of the element, driver is awaiting to see in UI.
	 * @return Nothing.
	 */
	public void explicitWaitName(String name){
		Reporting.getReporting().info(mClassName+ "Waiting....");
		WebDriverWait wait = new WebDriverWait(mDriver, mElementWaitDuration);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name(name)));
	}

	public void explicitWaitXpath(String path){
		Reporting.getReporting().info(mClassName+"Waiting....");
		WebDriverWait wait = new WebDriverWait(mDriver, mElementWaitDuration);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("path")));
	}

	/**
	 * This is the method which waits till the element has been found.
	 * @param className This is the class name of the element, driver is awaiting to see in UI.
	 * @return Nothing.
	 */
	public void explicitWaitClassName(String className){
		Reporting.getReporting().info(mClassName+"Waiting....");
		WebDriverWait wait = new WebDriverWait(mDriver, mElementWaitDuration);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.className(className)));
	}

	/**
	 * This is the method which waits till the element has been found.
	 * @param ID This is the id of the element, driver is awaiting to see in UI.
	 * @return Nothing.
	 */
	public void explicitWaitID(String ID){
		Reporting.getReporting().info(mClassName+"Waiting....");
		WebDriverWait wait = new WebDriverWait(mDriver, mElementWaitDuration);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id(ID)));
	}
}
