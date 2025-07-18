package com.E_commerce_Pages;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Screenshots {

	public static void captureScreenshot(WebDriver driver, String screenshotName) {
		try {
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshot, new File("test-output/screenshots/" + screenshotName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
