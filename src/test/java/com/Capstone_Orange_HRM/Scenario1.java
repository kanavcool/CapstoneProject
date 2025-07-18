package com.Capstone_Orange_HRM;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class Scenario1 {
	public static WebDriver driver = new ChromeDriver();

	String fpath = System.getProperty("user.dir") + "\\Excel_Files\\LoginData.xlsx";
	File file;
	FileInputStream fis;
	XSSFWorkbook wb;
	XSSFSheet sheet;
	XSSFRow row;
	XSSFCell cell;

	ExtentSparkReporter htmlreport;
	ExtentReports report;
	ExtentTest test;
	String extentreportpath = System.getProperty("user.dir") + "\\Reports\\OHRM_Login_Report.html";

	@Test(dataProvider = "loginData")
	public void Ohrmlogin(String un, String ps) throws InterruptedException {
		driver.findElement(By.name("username")).sendKeys(un);
		driver.findElement(By.name("password")).sendKeys(ps);
		// --- NEW: Capture screenshot after entering credentials ---
		// Note: Utility.getScreenshot(driver) expects only one argument based on your
		// current Utility class.
		// If you want custom names like "OHRM_Login_Data_Entered", you MUST update
		// Utility.getScreenshot
		// to accept a String parameter as discussed previously.
		Utility.getScreenshot(driver); // This will save with a timestamp name
		// If you had updated Utility.getScreenshot, it would be:
		// String screenshotPath = Utility.getScreenshot(driver,
		// "OHRM_Login_Data_Entered");
		// test.addScreenCaptureFromPath(screenshotPath, "Data Entered"); // To add to
		// Extent Report

		Thread.sleep(5000);
		driver.findElement(By.xpath("//button[@type=\"submit\"]")).submit();
	}

	@DataProvider
	public Object[][] loginData() {
		int totalrows = sheet.getPhysicalNumberOfRows();
		String[][] logindata = new String[totalrows - 1][2];

		for (int i = 0; i < totalrows - 1; i++) {
			row = sheet.getRow(i + 1);
			for (int j = 0; j < 2; j++) {
				cell = row.getCell(j);
				logindata[i][j] = cell.getStringCellValue();
			}
		}
		return logindata;
	}

	@BeforeTest
	public void beforeTest() throws IOException {
		
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\kanav\\.cache\\selenium\\chromedriver\\win64\\138.0.7204.157\\chromedriver.exe");

		file = new File(fpath);
		fis = new FileInputStream(file);
		wb = new XSSFWorkbook(fis);
		sheet = wb.getSheetAt(0);

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

		// Extent Report initialization
		htmlreport = new ExtentSparkReporter(extentreportpath);
		report = new ExtentReports();
		report.attachReporter(htmlreport);

		// setting environment for Extent report
		report.setSystemInfo("Machine name:", "Dell");
		report.setSystemInfo("Tester name:", "Kanav");
		report.setSystemInfo("OS", "Windows 11");
		report.setSystemInfo("Browser", "Google Chrome");

		// Configuration of the report
		htmlreport.config().setDocumentTitle("OHRM_Login_Report");
		htmlreport.config().setReportName("OHRM_Login_Details");
		htmlreport.config().setTheme(Theme.STANDARD);
		htmlreport.config().setTimeStampFormat("EEEE MM DD,yyyy HH:mm:ss");
	}

	@AfterMethod
	public void afterMethod() throws Exception {
		String current_url = driver.getCurrentUrl();
		Assert.assertTrue(current_url.contains(current_url), "URl does not match");
		if (current_url.contains("dashboard")) {
			System.out.println("Test Case Pass");
			Thread.sleep(3000);
			Utility.getScreenshot(driver);
			test = report.createTest("OHRM Valid Login");
			test.log(Status.PASS, MarkupHelper.createLabel("OHRM Login:Pass", ExtentColor.GREEN));
			driver.findElement(By.xpath("//i[@class=\"oxd-icon bi-caret-down-fill oxd-userdropdown-icon\"]")).click();
			driver.findElement(By.linkText("Logout")).click();
		} else {
			System.out.println("Test Case Fail");
			Thread.sleep(3000);
			Utility.getScreenshot(driver);
			test = report.createTest("OHRM Invalid Login");
			test.log(Status.FAIL, MarkupHelper.createLabel("OHRM Login:Fail", ExtentColor.RED));
		}
	}

	@AfterTest
	public void afterTest() throws IOException {
		wb.close();
		fis.close();
		report.flush();
		driver.quit();
	}
}
