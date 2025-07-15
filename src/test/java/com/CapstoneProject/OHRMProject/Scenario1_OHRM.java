package com.CapstoneProject.OHRMProject;

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
import org.openqa.selenium.support.ui.ExpectedConditions; // Import for explicit waits
import org.openqa.selenium.support.ui.WebDriverWait; // Import for explicit waits
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod; // Added BeforeMethod
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
import utility.Utility; // Ensure this import is correct

public class Scenario1_OHRM {

	public static WebDriver driver; // Changed to non-initialized here, will initialize in @BeforeTest
	String fpath = System.getProperty("user.dir") + "\\Excel_Files\\LoginData.xlsx";
	File file;
	FileInputStream fis;
	XSSFWorkbook wb;
	XSSFSheet sheet;
	XSSFRow row;
	XSSFCell cell;
	ExtentSparkReporter htmlreport;
	ExtentReports report;
	ExtentTest test; // This will be initialized per method call in @BeforeMethod

	String extentreportpath = System.getProperty("user.dir") + "\\Reports\\OHRM_Login_Report.html";
	WebDriverWait wait;

	@BeforeTest
	public void beforeTest() throws IOException {
		driver = new ChromeDriver(); // Initialize WebDriver instance
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Implicit wait for general element presence
		wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Explicit wait for specific conditions

		driver.get("https://opensource-demo.orangehrmlive.com/"); // Try this base URL first
		file = new File(fpath);
		fis = new FileInputStream(file);
		wb = new XSSFWorkbook(fis);
		sheet = wb.getSheetAt(0);

		htmlreport = new ExtentSparkReporter(extentreportpath);
		report = new ExtentReports();
		report.attachReporter(htmlreport);

		report.setSystemInfo("Machine name:", "Dell");
		report.setSystemInfo("Tester name:", "Kanav");
		report.setSystemInfo("OS", "Windows 11");
		report.setSystemInfo("Browser", "Google Chrome");

		htmlreport.config().setDocumentTitle("OHRM_Login_Report");
		htmlreport.config().setReportName("OHRM_Login_Details");
		htmlreport.config().setTheme(Theme.STANDARD);
		htmlreport.config().setTimeStampFormat("EEEE MM DD,yyyy HH:mm:ss");
	}

	@BeforeMethod // New BeforeMethod to create a new ExtentTest for each @Test method iteration
	public void beforeMethodSetup() {

		test = report.createTest("OHRM Login Test");
	}

	@Test(dataProvider = "loginData")
	public void Ohrmlogin(String un, String ps) { // Removed throws InterruptedException here
		test.log(Status.INFO, "Attempting login with Username: " + un + " and Password: " + ps);
		try {
			// Wait for username field to be visible and then send keys
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys(un);
			test.log(Status.INFO, "Entered username: " + un);

			// Wait for password field to be visible and then send keys
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password"))).sendKeys(ps);
			test.log(Status.INFO, "Entered password.");

			// Wait for submit button to be clickable and then click
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type=\"submit\"]"))).click();
			test.log(Status.INFO, "Clicked submit button.");

		} catch (Exception e) {
			test.log(Status.FAIL, "Login elements not found or interaction failed: " + e.getMessage());
			String screenshotPath = Utility.captureScreenshot(driver, "Login_Elements_Not_Found");
			test.addScreenCaptureFromPath(screenshotPath, "Login Elements Error");
			// Re-throw the exception so TestNG marks the test as FAILED
			Assert.fail("Login elements not found or interaction failed", e);
		}
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

	@AfterMethod
	public void afterMethod() throws Exception {
		String current_url = driver.getCurrentUrl();
		if (current_url.contains("dashboard")) {
			System.out.println("Test Case Pass");
			test.log(Status.PASS, MarkupHelper.createLabel("OHRM Login: Pass", ExtentColor.GREEN));
			String screenshotPath = Utility.captureScreenshot(driver, "OHRM_Login_Success"); // Corrected method call
			test.addScreenCaptureFromPath(screenshotPath, "Login Success Screenshot"); // Attach screenshot to report
			// Logout logic
			try {

				wait.until(ExpectedConditions.elementToBeClickable(
						By.xpath("//i[@class=\"oxd-icon bi-caret-down-fill oxd-userdropdown-icon\"]"))).click();
				test.log(Status.INFO, "Clicked user dropdown.");

				// Wait for the Logout link to be clickable
				wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Logout"))).click();
				test.log(Status.INFO, "Logged out successfully.");

				// Navigate back to login page for the next data provider iteration
				driver.navigate().to("https://opensource-demo.orangehrmlive.com/");

			} catch (Exception e) {
				test.log(Status.WARNING,
						"Logout failed or element not found after successful login: " + e.getMessage());
				String screenshotPathFail = Utility.captureScreenshot(driver, "Logout_Failure");
				test.addScreenCaptureFromPath(screenshotPathFail, "Logout Failure Screenshot");
				// Navigate back to login page even if logout fails, to prepare for next test
				driver.navigate().to("https://opensource-demo.orangehrmlive.com/");
			}

		} else {
			System.out.println("Test Case Fail");
			test.log(Status.FAIL, MarkupHelper.createLabel("OHRM Login: Fail", ExtentColor.RED));
			String screenshotPath = Utility.captureScreenshot(driver, "OHRM_Login_Failure"); // Corrected method call
			test.addScreenCaptureFromPath(screenshotPath, "Login Failure Screenshot"); // Attach screenshot to report

			// If login fails, navigate back to login page for the next data provider
			// iteration
			driver.navigate().to("https://opensource-demo.orangehrmlive.com/");
		}
	}

	@AfterTest
	public void afterTest() throws IOException {
		wb.close();
		fis.close();
		report.flush(); // Ensure report is flushed at the end
		if (driver != null) {
			driver.quit();
		}
	}
}