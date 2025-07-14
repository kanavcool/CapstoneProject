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
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);

			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String fileName = screenshotName + "_" + timestamp + ".png";

			File directory = new File(
					System.getProperty("user.dir") + File.separator + "Reports" + File.separator + "Screenshots");
			if (!directory.exists()) {
				directory.mkdirs();
			}

			destinationPath = directory.getAbsolutePath() + File.separator + fileName;
			File destination = new File(destinationPath);

			FileUtils.copyFile(source, destination);
			System.out.println("Screenshot saved: " + destinationPath);

		} catch (IOException e) {
			System.err.println("Failed to capture screenshot: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("An unexpected error occurred while capturing screenshot: " + e.getMessage());
			e.printStackTrace();
		}
		return destinationPath;
	}
}