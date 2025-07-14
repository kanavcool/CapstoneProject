package com.CapstoneProject.OHRMProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
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

import io.github.bonigarcia.wdm.WebDriverManager;
import utility.Utility;

public class Scenario1_OHRM {

    public static WebDriver driver;
    String fpath = System.getProperty("user.dir") + File.separator + "Excel_Files" + File.separator + "LoginData.xlsx";
    File file;
    FileInputStream fis;
    XSSFWorkbook wb;
    XSSFSheet sheet;

    ExtentSparkReporter htmlreport;
    ExtentReports report;
    ExtentTest test;
    String extentreportpath = System.getProperty("user.dir") + File.separator + "Reports" + File.separator
            + "OHRM_Login_Report.html";

    @BeforeTest
    public void beforeTest() throws IOException {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("disable-infobars");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-extensions");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--remote-allow-origins=*");
        // options.addArguments("--headless");
        // options.addArguments("--disable-gpu");
        // options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(45));

        System.out.println("Navigating to URL: https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        WebDriverWait urlWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        try {
            urlWait.until(ExpectedConditions.urlContains("/auth/login"));
            System.out.println("Browser successfully navigated to the login URL.");
        } catch (org.openqa.selenium.TimeoutException e) {
            System.err.println("ERROR: Browser did not reach the expected login URL within 20 seconds.");
            System.err.println("Current URL: " + driver.getCurrentUrl());
            System.err.println("Page Title: " + driver.getTitle());
            Utility.captureScreenshot(driver, "PageLoadIssue_BeforeTest");
            throw new IOException("Failed to load login page URL", e);
        }

        // Excel file setup
        file = new File(fpath);
        if (!file.exists()) {
            throw new IOException("Excel file not found at: " + fpath
                    + ". Please ensure the 'Excel_Files' folder and 'LoginData.xlsx' exist.");
        }
        fis = new FileInputStream(file);
        wb = new XSSFWorkbook(fis);
        sheet = wb.getSheetAt(0);

        // ExtentReports setup
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

    @DataProvider
    public Object[][] loginData() {
        if (sheet == null) {
            System.err.println("Excel sheet is null in DataProvider. Check @BeforeTest setup.");
            return new Object[0][0];
        }
        int totalRows = sheet.getPhysicalNumberOfRows();
        if (totalRows <= 1) {
            System.out.println("No data rows found in Excel sheet. Please add login data starting from row 2.");
            return new Object[0][0];
        }

        Object[][] logindata = new Object[totalRows - 1][2];
        for (int i = 0; i < totalRows - 1; i++) {
            XSSFRow row = sheet.getRow(i + 1);
            if (row != null) {
                logindata[i][0] = (row.getCell(0) == null) ? "" : row.getCell(0).getStringCellValue();
                logindata[i][1] = (row.getCell(1) == null) ? "" : row.getCell(1).getStringCellValue();
            } else {
                logindata[i][0] = "";
                logindata[i][1] = "";
            }
        }
        return logindata;
    }

    @Test(dataProvider = "loginData")
    public void Ohrmlogin(String un, String ps) {
        test = report.createTest("OHRM Login Test for User: " + un);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            System.out.println("Attempting login for user: " + un);

            if (!driver.getCurrentUrl().contains("auth/login")) {
                driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
                wait.until(ExpectedConditions.urlContains("/auth/login"));
                test.log(Status.INFO, "Navigated to login page for next iteration.");
            }

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).clear();
            driver.findElement(By.name("username")).sendKeys(un);
            test.log(Status.INFO, "Entered username: " + un);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password"))).clear();
            driver.findElement(By.name("password")).sendKeys(ps);
            test.log(Status.INFO, "Entered password: " + ps);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type=\"submit\"]"))).click();
            test.log(Status.INFO, "Clicked Login button.");

            // Explicitly wait for either dashboard or error message after login attempt
            try {
                wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("dashboard"),
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[text()='Invalid credentials']")),
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[text()='Required']"))
                    // ADD MORE EXPECTED ERROR LOCATORS HERE IF YOU FIND NEW ONES
                ));
            } catch (Exception e) {
                test.log(Status.WARNING, "Neither dashboard nor expected common error message appeared within timeout after login attempt.");
            }

            // --- ASSERTION LOGIC ---
            if (driver.getCurrentUrl().contains("dashboard")) {
                test.log(Status.PASS, MarkupHelper.createLabel("Login Successful: Dashboard reached.", ExtentColor.GREEN));
                System.out.println("Login Successful for: " + un);
                Assert.assertTrue(true, "Login should be successful for " + un);
            } else if (driver.findElements(By.xpath("//p[text()='Invalid credentials']")).size() > 0) {
                test.log(Status.PASS, MarkupHelper.createLabel("Login Failed as expected: Invalid Credentials.", ExtentColor.ORANGE));
                System.out.println("Login Failed (Invalid Credentials) for: " + un);
                Assert.assertTrue(true, "Login expected to fail for invalid credentials: " + un);
            } else if (driver.findElements(By.xpath("//p[text()='Required']")).size() > 0) {
                test.log(Status.PASS, MarkupHelper.createLabel("Login Failed as expected: Required Fields.", ExtentColor.ORANGE));
                System.out.println("Login Failed (Required Fields) for: " + un);
                Assert.assertTrue(true, "Login expected to fail for empty fields: " + un);
            }
            // Add more `else if` blocks here for other expected error messages found during manual testing
            // Example:
            // else if (driver.findElements(By.xpath("//p[text()='User does not exist']")).size() > 0) {
            //     test.log(Status.PASS, MarkupHelper.createLabel("Login Failed as expected: User does not exist.", ExtentColor.ORANGE));
            //     System.out.println("Login Failed (User does not exist) for: " + un);
            //     Assert.assertTrue(true, "Login expected to fail because user does not exist: " + un);
            // }
            else {
                // This 'else' block catches genuinely unexpected outcomes
                test.log(Status.FAIL, MarkupHelper.createLabel("Login Failed: Unexpected outcome, URL: " + driver.getCurrentUrl(), ExtentColor.RED));
                System.out.println("Login Failed (Unexpected outcome) for: " + un + ". Current URL: " + driver.getCurrentUrl());
                Assert.fail("Login failed unexpectedly for " + un); // Mark as actual failure in TestNG
            }

        } catch (Exception e) {
            System.err.println("ERROR during test for user " + un + ": " + e.getMessage());
            test.log(Status.FAIL, MarkupHelper.createLabel("Test Exception: " + e.getMessage(), ExtentColor.RED));
            Assert.fail("Test failed due to exception for " + un + ": " + e.getMessage(), e);
        }
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        String usernameUsed = "N/A";
        if (result.getParameters() != null && result.getParameters().length > 0) {
            usernameUsed = (String) result.getParameters()[0];
        }

        String screenshotNameBase = result.getMethod().getMethodName() + "_" + usernameUsed + "_" + (result.getStatus() == ITestResult.SUCCESS ? "PASS" : "FAIL");
        String screenshotPath = Utility.captureScreenshot(driver, screenshotNameBase);
        if (screenshotPath != null && !screenshotPath.isEmpty()) {
            test.addScreenCaptureFromPath(screenshotPath, "Screenshot after test: " + result.getMethod().getMethodName());
        }

        // Handle logout only if the login was successful (reached dashboard)
        if (driver.getCurrentUrl().contains("dashboard")) {
            try {
                WebDriverWait logoutWait = new WebDriverWait(driver, Duration.ofSeconds(10));
                logoutWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p[@class='oxd-userdropdown-name']"))).click();
                logoutWait.until(ExpectedConditions.elementToBeClickable(By.linkText("Logout"))).click();
                logoutWait.until(ExpectedConditions.urlContains("/auth/login"));
                test.log(Status.INFO, "Logged out successfully for user: " + usernameUsed);
                System.out.println("Logged out successfully for user: " + usernameUsed);
            } catch (Exception e) {
                test.log(Status.WARNING, "Logout failed (might be already logged out or element not found): " + e.getMessage());
                Utility.captureScreenshot(driver, "LogoutFailure_" + usernameUsed);
                System.err.println("Logout failed for user " + usernameUsed + ": " + e.getMessage());
            }
        } else {
            test.log(Status.INFO, "No logout performed as not on dashboard.");
        }

        // Ensure we are on the login page for the next data provider iteration
        try {
            if (driver != null && !driver.getCurrentUrl().contains("login")) {
                driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
                new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains("/auth/login"));
            }
        } catch (Exception e) {
            System.err.println("Error navigating back to login page in afterMethod: " + e.getMessage());
        }
    }

    @AfterTest
    public void afterTest() throws IOException {
        if (wb != null) {
            wb.close();
        }
        if (fis != null) {
            fis.close();
        }
        if (report != null) {
            report.flush();
        }
        if (driver != null) {
            driver.quit();
        }
        System.out.println("All resources closed and report flushed.");
    }
}