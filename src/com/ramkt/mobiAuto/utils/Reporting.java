/**
 * Created by Ram_Thirupathy on 8/9/2016.
 */
package com.ramkt.mobiAuto.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.ramkt.mobiAuto.testController.BaseController;

public abstract class Reporting {
	private static Reporting mReporting;
	protected ArrayList<ReportData> mReportData = new ArrayList<ReportData>();
	protected Logger mLog;
	private BaseController mController;
	protected ArrayList<String> mExceptionDetails;
	String mInputPath, mOutputPath, mReportDataPath;
	protected Reporting(String configFilePath,BaseController controller) throws IOException{
		String mBasePath = new File("").getAbsolutePath();
		mInputPath = mBasePath+"\\test-input\\";
		mOutputPath = mBasePath+"\\test-output\\";
		mReportDataPath = mOutputPath+"data\\";
		clearOutputDirectory();
		mLog = Logger.getLogger(Reporting.class);
		PropertyConfigurator.configure(configFilePath);
		createDataDirectory();
		mExceptionDetails = new ArrayList<String>();
		this.mController = controller;
	}

	public static void initializeReporting(String configFilePath,BaseController controller) throws IOException{
		mReporting = new HtmlReport(configFilePath,controller);//reporting will be based on type(Html,excel..), for now it is html
	}

	public static Reporting getReporting(){
		return mReporting;
	}

	public void info(String message){

		mLog.info(message);
	}

	public void exception(Exception e, String message) throws Exception{
		trace(e, message);
		throw new MobAutoException(e.getMessage());
	}

	public void trace(Exception e, String message) throws Exception{
		mExceptionDetails.add(message);
		mLog.error(message);
	}

	public void halt(Exception e, String message) throws Exception{
		trace(e, message);
		try{
			File outputfile = new File(mReportDataPath+"Error.png");
			ImageIO.write(mController.getDriver().screenShot(), "png", outputfile);
		}catch(Exception e1){

		}
	}

	private void createDataDirectory(){
		File theDir = new File(mReportDataPath);
		theDir.mkdirs();
	}

	private void clearOutputDirectory() throws IOException{
		System.out.println(mOutputPath);
		File theDir = new File(mOutputPath);
		if (theDir.exists()) {
			System.out.println("DELETE");
			FileUtils.deleteDirectory(theDir);
		}
	}

	public ReportData storeReport(String moduleName){
		ReportData data = new ReportData();
		data.setmModuleName(moduleName);
		mReportData.add(data);
		return data;
	}

	protected void failedTestEventScreenShot(String name) throws IOException, Exception{
		File outputfile = new File(mReportDataPath+name+".png");
		ImageIO.write(mController.getDriver().screenShot(), "png", outputfile);
	}

	public abstract void generateReport();

	public abstract void generateExceptionReport();

}
