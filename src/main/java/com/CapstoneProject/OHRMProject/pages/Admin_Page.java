// File: src/main/java/com/CapstoneProject/OHRMProject/pages/Admin_Page.java

package com.CapstoneProject.OHRMProject.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Admin_Page {
	private WebDriver driver;
	private WebDriverWait wait;

	public Admin_Page(WebDriver d) {
		this.driver = d;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//ul[@class='oxd-main-menu']/li/a")
	private List<WebElement> menuItems;

	@FindBy(xpath = "//span[normalize-space()='Admin']")
	private WebElement adminLink;

	@FindBy(xpath = "//div[@class='oxd-input-group oxd-input-field-bottom-space']//input[@class='oxd-input oxd-input--active']")
	private WebElement usernameSearchField;

	@FindBy(xpath = "//button[@type='submit']")
	private WebElement searchButton;

	@FindBy(xpath = "//span[@class='oxd-text oxd-text--span']")
	private WebElement recordsFoundMessage;

	@FindBy(xpath = "(//div[@class='oxd-select-text--after'])[1]")
	private WebElement userRoleDropdownClickable;

	@FindBy(xpath = "(//div[@class='oxd-select-text--after'])[2]")
	private WebElement statusDropdownClickable;

	@FindBy(xpath = "//div[@role='listbox']//span")
	private List<WebElement> dropdownOptions;

	public void navigateToAdminPage() {
		wait.until(ExpectedConditions.elementToBeClickable(adminLink)).click();
		wait.until(ExpectedConditions.urlContains("admin/viewSystemUsers"));
		// Add a wait here for a prominent element on the Admin search form to be
		// visible
		// This ensures the page is fully loaded and stable before interacting with
		// search fields
		wait.until(ExpectedConditions.visibilityOf(usernameSearchField)); // Wait for the username search field
		System.out.println("Successfully navigated to Admin Page.");
	}

	public void searchUserByUsername(String username) {
		System.out.println("Searching user by username: " + username);
		// Ensure the element is visible and interactable before clearing/sending keys
		wait.until(ExpectedConditions.visibilityOf(usernameSearchField)).clear();
		usernameSearchField.sendKeys(username);
		searchButton.click();
		wait.until(ExpectedConditions.visibilityOf(recordsFoundMessage));
		System.out.println("Search results for username '" + username + "': " + recordsFoundMessage.getText());
	}

	public void searchUserByUserRole(String role) {
		System.out.println("Searching user by role: " + role);
		wait.until(ExpectedConditions.elementToBeClickable(userRoleDropdownClickable)).click();
		selectOptionFromDropdown(role);
		searchButton.click();
		wait.until(ExpectedConditions.visibilityOf(recordsFoundMessage));
		System.out.println("Search results for user role '" + role + "': " + recordsFoundMessage.getText());
	}

	public void searchUserByUserStatus(String status) {
		System.out.println("Searching user by status: " + status);
		wait.until(ExpectedConditions.elementToBeClickable(statusDropdownClickable)).click();
		selectOptionFromDropdown(status);
		searchButton.click();
		wait.until(ExpectedConditions.visibilityOf(recordsFoundMessage));
		System.out.println("Search results for user status '" + status + "': " + recordsFoundMessage.getText());
	}

	private void selectOptionFromDropdown(String optionText) {
		wait.until(ExpectedConditions.visibilityOfAllElements(dropdownOptions));
		for (WebElement option : dropdownOptions) {
			if (option.getText().trim().equalsIgnoreCase(optionText)) {
				option.click();
				break;
			}
		}
	}

	public WebElement getRecordsFoundMessage() {
		return wait.until(ExpectedConditions.visibilityOf(recordsFoundMessage));
	}

	public List<WebElement> getMenuItems() {
		return menuItems;
	}
}