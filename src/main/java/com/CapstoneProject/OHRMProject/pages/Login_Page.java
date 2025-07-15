package com.CapstoneProject.OHRMProject.pages; // Standardized package name

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

// Removed org.testng.annotations.Test import as it's not needed in Page Objects

public class Login_Page {
	private WebDriver driver;
	private WebDriverWait wait; // Declare WebDriverWait for explicit waits

	// Constructor to initialize WebDriver and PageFactory elements
	public Login_Page(WebDriver d) {
		this.driver = d;
		PageFactory.initElements(driver, this);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Initialize WebDriverWait
	}

	// WebElements using @FindBy
	@FindBy(name = "username")
	private WebElement username;

	@FindBy(name = "password")
	private WebElement password;

	@FindBy(xpath = "//button[@type=\"submit\"]")
	private WebElement submit;

	// Public Getter methods (as per your original structure) - optional for POM,
	// but kept as requested
	public WebElement getUsername() {
		return username;
	}

	public WebElement getPassword() {
		return password;
	}

	public WebElement getSubmit() {
		return submit;
	}

	/**
	 * Performs the login action on the OrangeHRM login page. Waits for the
	 * username, password, and submit button to be interactable.
	 *
	 * @param un The username to enter.
	 * @param ps The password to enter.
	 */
	// IMPORTANT: Removed @Test annotation. Page Objects should not have @Test.
	public void login(String un, String ps) {
		try {
			// Use explicit waits for robustness instead of Thread.sleep()
			wait.until(ExpectedConditions.visibilityOf(getUsername())).sendKeys(un);
			wait.until(ExpectedConditions.visibilityOf(getPassword())).sendKeys(ps);
			wait.until(ExpectedConditions.elementToBeClickable(getSubmit())).click(); // Using click() is generally
																						// preferred for buttons

			// Removed "Login Successful" print statement.
			// Test outcome messages belong in the test class (e.g., Scenario2_OHRM).

		} catch (Exception e) {
			System.err.println("Error during login process for user: " + un + ". Details: " + e.getMessage());
			// Re-throw the exception to ensure the calling test method (e.g., in
			// Scenario2_OHRM)
			// catches it and marks the test as failed.
			throw new RuntimeException("Login action failed", e);
		}
	}
}
