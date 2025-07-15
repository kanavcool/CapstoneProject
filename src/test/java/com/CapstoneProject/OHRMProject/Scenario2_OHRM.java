package com.CapstoneProject.OHRMProject;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import utility.Utility;

import com.CapstoneProject.OHRMProject.pages.Login_Page;
import com.CapstoneProject.OHRMProject.pages.Admin_Page;

public class Scenario2_OHRM {
	public WebDriver driver;
	public Login_Page loginPage;
	public Admin_Page adminPage; // Declare here, but initialize later

	@BeforeTest
	public void setup() {
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
			Utility.captureScreenshot(driver, "PageLoadIssue_POM_Client");
			Assert.fail("Failed to load login page URL", e);
		}

		loginPage = new Login_Page(driver);
		// adminPage will be initialized after login and navigation to Admin page
	}

	@Test
	public void testLoginAndAdminFunctionality() {
		// Step 1: Login
		System.out.println("--- Executing Login Test ---");
		loginPage.login("Admin", "admin123");
		Assert.assertTrue(loginPage.isLoginSuccessful(), "Login Failed! Did not reach dashboard.");
		System.out.println("Login Successful. Current URL: " + driver.getCurrentUrl());

		// Step 2: Navigate to Admin Page and Verify
		System.out.println("\n--- Executing Admin Page Navigation ---");
		// Initialize adminPage *after* login, before navigating to it
		adminPage = new Admin_Page(driver); // Initialize/re-initialize Admin_Page object
		adminPage.navigateToAdminPage();
		Assert.assertTrue(driver.getCurrentUrl().contains("admin/viewSystemUsers"),
				"Failed to navigate to Admin page.");
		System.out.println("Successfully navigated to Admin Page.");

		// Step 3: Search by Username
		System.out.println("\n--- Executing Search by Username ---");
		adminPage.searchUserByUsername("Admin"); // Search for 'Admin' user
		// Fix the assertion to match the actual text "(X) Record Found"
		Assert.assertTrue(adminPage.getRecordsFoundMessage().getText().contains("Record Found"),
				"No records found for username search.");

		// Step 4: Search by User Role
		System.out.println("\n--- Executing Search by User Role ---");
		adminPage.searchUserByUserRole("Admin"); // Search for 'Admin' role
		// Fix the assertion to match the actual text "(X) Record Found"
		Assert.assertTrue(adminPage.getRecordsFoundMessage().getText().contains("Record Found"),
				"No records found for user role search.");

		// Step 5: Search by User Status
		System.out.println("\n--- Executing Search by User Status ---");
		adminPage.searchUserByUserStatus("Enabled"); // Search for 'Enabled' status
		// Fix the assertion to match the actual text "(X) Record Found"
		Assert.assertTrue(adminPage.getRecordsFoundMessage().getText().contains("Record Found"),
				"No records found for user status search.");

		// Optional: Logout after all Admin tests are done
		System.out.println("\n--- Performing Logout ---");
		loginPage.logout();
		Assert.assertTrue(driver.getCurrentUrl().contains("/auth/login"),
				"Logout Failed! Did not return to login page.");
		System.out.println("Successfully logged out.");
	}

	@AfterTest
	public void teardown() {
		if (driver != null) {
			driver.quit();
		}
		System.out.println("Browser closed.");
	}
}