/**
 * Created by Ram_Thirupathy on 8/9/2016.
 */
package com.ramkt.mobiAuto.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.rendersnake.HtmlAttributes;
import org.rendersnake.HtmlAttributesFactory;
import org.rendersnake.HtmlCanvas;

import com.ramkt.mobiAuto.testController.BaseController;

public class HtmlReport extends Reporting {

	protected HtmlReport(String configFilePath,BaseController controller) throws IOException {
		super(configFilePath,controller);
	}

	/**
	 * This is the method which generate HTML report  
	 * @param results Hashmap (channel name and status)
	 * @return Nothing.
	 */
	@Override
	public void generateReport(){
		if(!mReportData.isEmpty()){
			//generate a new html file for exception report
			generateModuleReport();
		}

	}

	private void generateModuleReport(){
		try{
			System.out.println("Html start");
			HtmlCanvas html = new HtmlCanvas();
			html
			.head()
			.macros().stylesheet(mInputPath+"Report.css")
			._head();  
			html
			.h3().content("MOB-Auto Report");
			html
			.table()
			.tr()
			.th().content("Modules")
			.th().content("Status")
			._tr();
			for(ReportData data : mReportData){
				html.tr()
				.td()
				.a(attachPage(mReportDataPath+data.getmModuleName()+".html"))
				.content(data.getmModuleName())

				.td().content(data.getmModuleStatus())
				._td()
				._tr();
				generateTestCaseReport(data.getmModuleName(), data.getmTestCaseStatus(),data);
			}
			html._table();
			// write the file
			final String rendered = html.toHtml();
			final File output = new File(mOutputPath,"Report.html");
			if(!output.exists()){
				output.createNewFile();
			}
			Files.write(output.toPath(), rendered.getBytes("UTF-8"), StandardOpenOption.TRUNCATE_EXISTING);
			//			 Desktop.getDesktop().open(output);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void generateTestCaseReport(String moduleName,LinkedHashMap<String, String> map,ReportData rep){
		try{
			System.out.println("Html start");
			HtmlCanvas html = new HtmlCanvas();
			html
			.head()
			.macros().stylesheet(mInputPath+"Report.css")
			._head();  
			html
			.h3().content("MOB-Auto Report");
			html
			.table()
			.tr()
			.th().content("Test Cases")
			.th().content("Status")
			._tr();
			int no =1;
			for(Entry<String, String> data : map.entrySet()){
				html.tr()
				.td()
				.a(attachPage(mReportDataPath+moduleName+no+".html"))
				.content(data.getKey())

				.td().content(data.getValue())
				._td()
				._tr();
				generateTestEventReport(moduleName+no,rep.getmTestEventStatus().get(data.getKey()));
				no++;

			}
			html._table();
			// write the file
			final String rendered = html.toHtml();
			final File output = new File(mReportDataPath,moduleName+".html");
			if(!output.exists()){
				output.createNewFile();
			}
			Files.write(output.toPath(), rendered.getBytes("UTF-8"), StandardOpenOption.TRUNCATE_EXISTING);
			//			 Desktop.getDesktop().open(output);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void generateTestEventReport(String testCase,LinkedHashMap<String, String> map){
		try{
			System.out.println("Html start");
			HtmlCanvas html = new HtmlCanvas();
			html
			.head()
			.macros().stylesheet(mInputPath+"Report.css")
			._head();  
			html
			.h3().content("MOB-Auto Report");
			html
			.table()
			.tr()
			.th().content("Test Events")
			.th().content("Status")
			._tr();
			for(Entry<String, String> data : map.entrySet()){
				html.tr()
				.td().content(data.getKey())
				.td().content(data.getValue())
				._tr();
				if(data.getValue().equals("FAIL")){
					if(mExceptionDetails.isEmpty()){
						failedTestEventScreenShot(data.getKey());
						html.tr()
						.td().content("")
						.td().img(attachImg(mReportDataPath+data.getKey()+".png"))
						._td()
						._tr();
					}else{
						generateExceptionReport();	
						html.tr()
						.td().content("")
						.td()
						.a(attachPage(mReportDataPath+"ReportError.html"))
						.content("Error")
						._td()
						._tr();
					}
				}
			}
			html._table();
			// write the file
			final String rendered = html.toHtml();
			final File output = new File(mReportDataPath,testCase+".html");
			if(!output.exists()){
				output.createNewFile();
			}
			Files.write(output.toPath(), rendered.getBytes("UTF-8"), StandardOpenOption.TRUNCATE_EXISTING);
			//			 Desktop.getDesktop().open(output);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private HtmlAttributes attachImg(String imagePath){
		HtmlAttributes attributes = new HtmlAttributes();

		attributes
		.src(imagePath)
		.width(Constants.ERROR_IMAGE_WIDTH)
		.height(Constants.ERROR_IMAGE_HEIGHT);
		return attributes;
	}

	private HtmlAttributes attachPage(String filePath){
		return HtmlAttributesFactory.href(filePath).target("_blank");
	}

	@Override
	public void generateExceptionReport(){
		if(!mExceptionDetails.isEmpty()){
			//generate a new html file for exception report
			try{
				System.out.println("Html start");
				HtmlCanvas html = new HtmlCanvas();
				html
				.head()
				.macros().stylesheet(mInputPath+"ReportError.css")
				._head();  
				html
				.h3().content("MOB-Auto Error Report");
				html
				.table()
				.tr()
				.th().content("Trace")
				._tr();
				for(String error : mExceptionDetails){
					html.tr()
					.td().content(error)
					._tr();
				}
				html.tr()
				.td().img(attachImg(mReportDataPath+"Error.png"))
				._td()
				._tr();
				html._table();
				// write the file
				final String rendered = html.toHtml();
				final File output = new File(mReportDataPath,"ReportError.html");
				if(!output.exists()){
					output.createNewFile();
				}
				Files.write(output.toPath(), rendered.getBytes("UTF-8"), StandardOpenOption.TRUNCATE_EXISTING);
				//			 Desktop.getDesktop().open(output);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}



}
