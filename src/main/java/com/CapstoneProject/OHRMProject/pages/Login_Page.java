package com.CapstoneProject.OHRMProject.pages;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Login_Page {
	private WebDriver driver;
	private WebDriverWait wait;

	public Login_Page(WebDriver d) {
		this.driver = d;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Initialize WebDriverWait here
		PageFactory.initElements(driver, this);
	}

	@FindBy(name = "username")
	private WebElement usernameField;

	@FindBy(name = "password")
	private WebElement passwordField;

	@FindBy(xpath = "//button[@type=\"submit\"]")
	private WebElement submitButton;

	@FindBy(xpath = "//p[@class='oxd-userdropdown-name']")
	private WebElement userDropdown;

	@FindBy(linkText = "Logout")
	private WebElement logoutLink;

	public void enterUsername(String un) {
		wait.until(ExpectedConditions.visibilityOf(usernameField)).clear();
		usernameField.sendKeys(un);
	}

	public void enterPassword(String ps) {
		wait.until(ExpectedConditions.visibilityOf(passwordField)).clear();
		passwordField.sendKeys(ps);
	}

	public void clickLoginButton() {
		wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
	}

	public void login(String un, String ps) {
		enterUsername(un);
		enterPassword(ps);
		clickLoginButton();
	}

	public boolean isLoginSuccessful() {
		try {
			// Wait for URL to contain "dashboard" or for a dashboard-specific element
			return wait.until(ExpectedConditions.urlContains("dashboard"));
		} catch (Exception e) {
			return false;
		}
	}

	public void logout() {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(userDropdown)).click();
			wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
			wait.until(ExpectedConditions.urlContains("/auth/login")); // Wait for logout to complete
			System.out.println("Logout action performed successfully.");
		} catch (Exception e) {
			System.err.println("Error during logout: " + e.getMessage());
			// Optionally, re-throw if logout is critical for subsequent tests or state
		}
	}
}