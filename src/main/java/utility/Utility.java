package utility;



import java.io.File;

import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.Date;



import org.apache.commons.io.FileUtils;

import org.openqa.selenium.OutputType;

import org.openqa.selenium.TakesScreenshot;

import org.openqa.selenium.WebDriver;



public class Utility {



public static String captureScreenshot(WebDriver driver, String screenshotName) {

String destinationPath = "";

try {

// Capture screenshot

TakesScreenshot ts = (TakesScreenshot) driver;

File source = ts.getScreenshotAs(OutputType.FILE);



// Create filename with timestamp

String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

String fileName = screenshotName + "_" + timestamp + ".png";



// ✅ Save screenshot in root-level "Screenshots" folder (no "Reports")

File directory = new File(System.getProperty("user.dir") + File.separator + "Screenshots");

if (!directory.exists()) {

directory.mkdirs();

}



// Full path where screenshot will be saved

destinationPath = directory.getAbsolutePath() + File.separator + fileName;

File destination = new File(destinationPath);



// Copy file

FileUtils.copyFile(source, destination);

System.out.println("✅ Screenshot saved at: " + destinationPath);

} catch (IOException e) {

System.err.println("❌ Failed to capture screenshot: " + e.getMessage());

e.printStackTrace();

} catch (Exception e) {

System.err.println("❌ Unexpected error while capturing screenshot: " + e.getMessage());

e.printStackTrace();

}



return destinationPath;

}

}

