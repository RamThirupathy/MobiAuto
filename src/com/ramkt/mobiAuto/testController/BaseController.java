/**
 * Created by Ram_Thirupathy on 8/9/2016.
 */
package com.ramkt.mobiAuto.testController;

import io.appium.java_client.remote.MobileCapabilityType;

import java.io.File;

import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;

import com.ramkt.mobiAuto.configuration.MessageConstants;
import com.ramkt.mobiAuto.driver.MobDriver;
import com.ramkt.mobiAuto.scriptObjects.Environment;
import com.ramkt.mobiAuto.utils.ExcelReader;
import com.ramkt.mobiAuto.utils.Reporting;
import com.ramkt.mobiAuto.utils.ScriptReader;

public class BaseController {
	private String mClassName = this.getClass().getSimpleName()+": ";
	protected MobDriver mDriver;
	protected ScriptReader mScriptReader;
	public void setUp() throws Exception {
		try{
			String basePath = new File("").getAbsolutePath();
			System.out.println(basePath);
			Reporting.initializeReporting(basePath+"\\test-input\\Log4jConfig.txt",this);
			Reporting.getReporting().info(mClassName+"Set up Start");
			mScriptReader = new ExcelReader(basePath+"\\test-input\\TestScript.xls");//can be changed based on type of input script,,, for now excel
			Environment environment = mScriptReader.getEnvironment(new Environment());
			if(environment.getmPlatformName().equalsIgnoreCase("web")){
				mDriver = new MobDriver(null, environment);
			}else{
				DesiredCapabilities capabilities =new DesiredCapabilities();
				capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, environment.getmPlatformName());
				capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, environment.getmDeviceName());
				capabilities.setCapability(MobileCapabilityType.APPIUM_VERSION,environment.getmAppiumVersion());
				capabilities.setCapability(MobileCapabilityType.APP, environment.getmAppPath());
				capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, environment.getmAppPackage());
				capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, environment.getmCommandTimeout());
				mDriver = new MobDriver(capabilities, environment);
			}
		}catch(UnreachableBrowserException e){
			Reporting.getReporting().exception(e,MessageConstants.UNREACHABLE_BROWSER);
		}catch(SessionNotCreatedException e){
			Reporting.getReporting().exception(e,MessageConstants.SESSION_NOT_CREATED);
		}
		Reporting.getReporting().info(mClassName+"Set up end");
	}

	public MobDriver getDriver(){
		return mDriver;
	}
}
