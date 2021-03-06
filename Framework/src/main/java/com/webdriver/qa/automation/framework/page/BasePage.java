package com.webdriver.qa.automation.framework.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.webdriver.qa.automation.framework.Framework;

public abstract class BasePage extends Framework {

	private WebDriver driver;

	public BasePage(WebDriver driver) {
		this.setDriver(driver);
	}

	public WebDriver getDriver() {
		return driver;
	}
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public boolean isElementPresent(By by) {
		return driver.findElements(by).size() > 0;
	}
	
	public void waitForElementPresent(By by) {
		ExpectedConditions.presenceOfElementLocated(by).apply(driver);
	}
}