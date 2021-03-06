package com.webdriver.qa.automation.execute.runner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.webdriver.qa.automation.framework.Framework;
import com.webdriver.qa.automation.framework.WebDriverManager;

public class CustomTestListener extends TestListenerAdapter {

	private static Logger Log = Logger.getRootLogger();
	File screenshotFilePath = null;
	
	private void saveScreenShot() throws WebDriverException, Exception {
		Date date = new Date();		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		String folderDate = dateFormat.format(date);
		dateFormat.applyPattern("HH.mm.ss");
		String fileDate = dateFormat.format(date);

		File screenshotFile = ((TakesScreenshot)WebDriverManager.driver).getScreenshotAs(OutputType.FILE);
		File screenshotDirectory = new File(Framework.getScreenShotDirectory() + "/" + folderDate);
		if(!screenshotDirectory.exists()) {
			screenshotDirectory.mkdirs();
		}
		screenshotFilePath = new File(screenshotDirectory.getAbsoluteFile() + "/" + fileDate + ".png");
		FileUtils.copyFile(screenshotFile, screenshotFilePath);
	}

	@Override
	public void onTestFailure(ITestResult testResult) {
		super.onTestFailure(testResult);
		Log.error("Test failed - " + testResult.getName());
		Log.info("Saving Screenshot");
		try {
			saveScreenShot();
			Log.info("Screnshot saved - " + screenshotFilePath);
		}
		catch(Exception ex) {
			Log.error("Could not take screenshot", ex);
		}
		testResult.getThrowable().printStackTrace();
	}

	@Override
	public void onTestSuccess(ITestResult testResult) {
		super.onTestSuccess(testResult);
		Log.info("Woo Hoo! The following test passed: " + testResult.getName());
	}
	
	@Override
	public void onStart(ITestContext testContext) {
		super.onStart(testContext);
		DOMConfigurator.configure("log4j.xml");
	}
	
	@Override
	public void onFinish(ITestContext testContext) {
		super.onFinish(testContext);
		Set<ITestResult> passedTests = testContext.getPassedTests().getAllResults();
		Set<ITestResult> failedTests = testContext.getFailedTests().getAllResults();		
		Log.info("*********************");
		Log.info("");
		Log.info("PASSED TESTS");
		if(passedTests.size() == 0) {
			Log.info("NO TESTS PASSED");
		}
		else {
			for(ITestResult passedResult : passedTests) {
				Log.info(passedResult.getName());
			}			
		}
		Log.info("===============================================");
		Log.info("FAILED TESTS");
		if(failedTests.size() == 0) {
			Log.info("NO TESTS FAILED");
		}
		else {
			for(ITestResult failedResult : failedTests) {
				Log.info(failedResult.getName());
			}
		}
	}
}