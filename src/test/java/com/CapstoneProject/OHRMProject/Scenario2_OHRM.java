package com.CapstoneProject.OHRMProject; // Ensure this package name is correct

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert; // For assertions
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By; // Needed for By.id, By.xpath etc.

import com.CapstoneProject.OHRMProject.pages.Admin_Page;
import com.CapstoneProject.OHRMProject.pages.Login_Page;

public class Scenario2_OHRM {
	public WebDriver driver;
	public Login_Page l1;
	public Admin_Page a1;
	private WebDriverWait wait; // Declare WebDriverWait for this test class

	@BeforeTest
	public void beforeTest() {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Implicit wait
		wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Explicit wait for this test class

		// **CRITICAL:** Use the base URL that works and leads to the login page.
		// You MUST manually verify this URL in a browser.
		driver.get("https://opensource-demo.orangehrmlive.com/");

		l1 = new Login_Page(driver);
		a1 = new Admin_Page(driver);
	}

	@Test
	public void testLogin_and_AdminPage() {
		System.out.println("Starting testLogin_and_AdminPage...");

		// 1. Login
		try {
			l1.login("Admin", "admin123");
			// Assert for successful login (e.g., check URL or a dashboard element)
			wait.until(ExpectedConditions.urlContains("dashboard"));
			// Optionally, verify a dashboard element is present
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h6[text()='Dashboard']")));
			System.out.println("Login Successful and Dashboard loaded.");
		} catch (Exception e) {
			System.err.println("Login failed: " + e.getMessage());
			Assert.fail("Login failed: " + e.getMessage()); // Fail the TestNG test
		}

		// 2. Navigate to Admin Page
		try {
			a1.navigateToAdminPage();
			// Assert that we are on the Admin page
			wait.until(ExpectedConditions.urlContains("admin"));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h6[text()='Admin']")));
			System.out.println("Navigated to Admin Page successfully.");
		} catch (Exception e) {
			System.err.println("Navigation to Admin page failed: " + e.getMessage());
			Assert.fail("Navigation to Admin page failed: " + e.getMessage());
		}

		// 3. Search by Username
		try {
			a1.searchByUserName("Admin"); // Pass the username to search
			// Assert for search results (e.g., check records_found_text)
			wait.until(ExpectedConditions.visibilityOf(a1.getRecords_found_text()));
			String actualRecordsText = a1.getRecords_found_text().getText().trim(); // Get text and trim whitespace
			System.out.println("DEBUG: Actual records text for username search: '" + actualRecordsText + "'"); // Debug
																												// print

			// Updated assertion to be more robust, checking for either "Record Found" or
			// "Records Found"
			boolean recordsFound = actualRecordsText.contains("Record Found")
					|| actualRecordsText.contains("Records Found");
			Assert.assertTrue(recordsFound,
					"Username search failed or no records found. Actual text: '" + actualRecordsText + "'");

			System.out.println("Username search completed. Records: " + actualRecordsText);
			a1.refreshPage(); // Refresh to clear search for next criteria
		} catch (Exception e) {
			System.err.println("Search by username failed: " + e.getMessage());
			Assert.fail("Search by username failed: " + e.getMessage());
		}

		// 4. Search by User Role
		try {
			a1.searchByUserRole("Admin"); // Pass the role to search
			wait.until(ExpectedConditions.visibilityOf(a1.getRecords_found_text()));
			String actualRecordsText = a1.getRecords_found_text().getText().trim();
			boolean recordsFound = actualRecordsText.contains("Record Found")
					|| actualRecordsText.contains("Records Found");
			Assert.assertTrue(recordsFound,
					"User Role search failed or no records found. Actual text: '" + actualRecordsText + "'");
			System.out.println("User Role search completed. Records: " + actualRecordsText);
			a1.refreshPage();
		} catch (Exception e) {
			System.err.println("Search by user role failed: " + e.getMessage());
			Assert.fail("Search by user role failed: " + e.getMessage());
		}

		// 5. Search by User Status
		try {
			a1.searchByUserStatus("Enabled"); // Pass the status to search
			wait.until(ExpectedConditions.visibilityOf(a1.getRecords_found_text()));
			String actualRecordsText = a1.getRecords_found_text().getText().trim();
			boolean recordsFound = actualRecordsText.contains("Record Found")
					|| actualRecordsText.contains("Records Found");
			Assert.assertTrue(recordsFound,
					"User Status search failed or no records found. Actual text: '" + actualRecordsText + "'");
			System.out.println("User Status search completed. Records: " + actualRecordsText);
		} catch (Exception e) {
			System.err.println("Search by user status failed: " + e.getMessage());
			Assert.fail("Search by user status failed: " + e.getMessage());
		}

		System.out.println("testLogin_and_AdminPage completed successfully.");
	}

	@AfterTest
	public void afterTest() {
		if (driver != null) {
			driver.quit();
		}
	}
}
