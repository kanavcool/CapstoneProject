package com.CapstoneProject.OHRMProject.pages; // Standardized package name

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
// Removed org.testng.Assert and org.testng.annotations.Test as they are not needed in Page Objects.

public class Admin_Page {
	private WebDriver driver;
	private WebDriverWait wait; // Explicit wait for dynamic elements

	// --- WebElements using @FindBy ---

	// Menu items on the left sidebar (e.g., Admin, PIM, Leave)
	@FindBy(xpath = "//ul[@class='oxd-main-menu']/li/a")
	private List<WebElement> menu_items;

	// Specific link for the Admin menu item
	@FindBy(linkText = "Admin")
	private WebElement admin_link;

	// Username input field on the Admin -> User Management -> Users page
	// Corrected XPath: ensure 'oxd-input-field-bottom-space' has a hyphen
	@FindBy(xpath = "//div[@class='oxd-input-group oxd-input-field-bottom-space']/div[2]/input")
	private WebElement usernameInput; // Renamed for clarity

	// Search button on the Admin -> User Management -> Users page
	@FindBy(xpath = "//button[@type='submit']")
	private WebElement search_button;

	// Element displaying "No Records Found" or "X Record(s) Found"
	@FindBy(xpath = "//div[@class='orangehrm-horizontal-padding orangehrm-vertical-padding']//span")
	private WebElement records_found_text; // Renamed for clarity

	// Dropdown arrows (e.g., for User Role, Status) - usually the
	// 'bi-caret-down-fill' icon
	// This XPath targets the dropdown arrow itself, which opens the options
	@FindBy(xpath = "//div[@class='oxd-select-wrapper']//i[@class='oxd-icon bi-caret-down-fill oxd-select-text--arrow']")
	private List<WebElement> dropdownArrows; // Use List for multiple dropdowns on the page

	// Common XPath for dropdown list items once a dropdown is clicked
	// These are the actual options that appear in the listbox
	@FindBy(xpath = "//div[@role='listbox']/div/span")
	private List<WebElement> dropdownOptions;

	// Constructor to initialize WebDriver and PageFactory
	public Admin_Page(WebDriver d) {
		this.driver = d;
		PageFactory.initElements(driver, this);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // Initialize WebDriverWait
	}

	// --- Page Action Methods ---

	/**
	 * Clicks on the Admin menu link and verifies navigation to the Admin page.
	 */
	// IMPORTANT: Removed @Test annotation. Page Objects should not have @Test.
	public void navigateToAdminPage() { // Renamed for clarity
		System.out.println("Total number of Menu items: " + menu_items.size());
		try {
			wait.until(ExpectedConditions.elementToBeClickable(admin_link)).click();
			// Wait for the URL to contain "admin" or for a specific element on the Admin
			// page
			wait.until(ExpectedConditions.urlContains("admin"));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h6[text()='Admin']"))); // Wait for
																											// Admin
																											// page
																											// header
			System.out.println("Logged into Admin Page");
		} catch (Exception e) {
			System.err.println("Failed to navigate to Admin page: " + e.getMessage());
			throw new RuntimeException("Navigation to Admin page failed", e); // Re-throw to fail the test
		}
	}

	/**
	 * Searches for a user by username on the Admin -> User Management -> Users
	 * page. Clicks the search button after entering the username.
	 *
	 * @param username The username to search for.
	 */
	// IMPORTANT: Removed @Test annotation. Page Objects should not have @Test.
	public void searchByUserName(String username) {
		System.out.println("Attempting to search by username: " + username);
		try {
			// Use explicit wait for the username input field to be visible and interactable
			wait.until(ExpectedConditions.visibilityOf(usernameInput)).sendKeys(username);
			System.out.println("Entered username: " + username);
			wait.until(ExpectedConditions.elementToBeClickable(search_button)).click();
			System.out.println("Clicked search button.");
			// Wait for search results to load (e.g., records_found_text to be visible)
			wait.until(ExpectedConditions.visibilityOf(records_found_text));
			System.out.println("Total records With username: " + username + " is: " + records_found_text.getText());
		} catch (Exception e) {
			System.err.println("Error searching by username '" + username + "': " + e.getMessage());
			throw new RuntimeException("Failed to search by username", e); // Re-throw to fail the test
		}
	}

	/**
	 * Searches for users by a specific User Role. Assumes the User Role dropdown is
	 * the first one (index 0) in the `dropdownArrows` list.
	 *
	 * @param role The user role to select (e.g., "Admin", "ESS").
	 */
	// IMPORTANT: Removed @Test annotation. Page Objects should not have @Test.
	public void searchByUserRole(String role) {
		System.out.println("Attempting to search by user role: " + role);
		try {
			// Click the User Role dropdown arrow (assuming it's the first one)
			wait.until(ExpectedConditions.elementToBeClickable(dropdownArrows.get(0))).click();

			// Iterate through the dropdown options to find and click the desired role
			for (WebElement option : dropdownOptions) {
				if (option.getText().trim().contains(role)) { // Use trim() to remove whitespace
					wait.until(ExpectedConditions.elementToBeClickable(option)).click();
					System.out.println("Selected user role: " + role);
					break;
				}
			}
			wait.until(ExpectedConditions.elementToBeClickable(search_button)).click();
			System.out.println("Clicked search button for user role.");
			wait.until(ExpectedConditions.visibilityOf(records_found_text));
			System.out.println("Total records with User Role " + role + ": " + records_found_text.getText());
		} catch (Exception e) {
			System.err.println("Error searching by user role '" + role + "': " + e.getMessage());
			throw new RuntimeException("Failed to search by user role", e); // Re-throw
		}
	}

	/**
	 * Searches for users by a specific Status. Assumes the Status dropdown is the
	 * second one (index 1) in the `dropdownArrows` list.
	 *
	 * @param status The user status to select (e.g., "Enabled", "Disabled").
	 */
	public void searchByUserStatus(String status) {
		System.out.println("Attempting to search by user status: " + status);
		try {
			// Click the Status dropdown arrow (assuming it's the second one)
			wait.until(ExpectedConditions.elementToBeClickable(dropdownArrows.get(1))).click();

			// Iterate through the dropdown options to find and click the desired status
			for (WebElement option : dropdownOptions) {
				if (option.getText().trim().contains(status)) { // Use trim() to remove whitespace
					wait.until(ExpectedConditions.elementToBeClickable(option)).click();
					System.out.println("Selected user status: " + status);
					break;
				}
			}
			wait.until(ExpectedConditions.elementToBeClickable(search_button)).click();
			System.out.println("Clicked search button for user status.");
			wait.until(ExpectedConditions.visibilityOf(records_found_text));
			System.out.println("Total Records with status " + status + ": " + records_found_text.getText());
		} catch (Exception e) {
			System.err.println("Error searching by user status '" + status + "': " + e.getMessage());
			throw new RuntimeException("Failed to search by user status", e); // Re-throw
		}
	}

	/**
	 * Refreshes the current page.
	 */
	public void refreshPage() {
		driver.navigate().refresh();
		System.out.println("Page refreshed.");
	}

	// --- Getter methods (optional, but can be useful for assertions in test
	// classes) ---
	public List<WebElement> getMenu_items() {
		return menu_items;
	}

	public WebElement getAdmin_link() {
		return admin_link;
	}

	public WebElement getUsernameInput() {
		return usernameInput;
	}

	public WebElement getSearch_button() {
		return search_button;
	}

	public WebElement getRecords_found_text() {
		return records_found_text;
	}

	public List<WebElement> getDropdownArrows() {
		return dropdownArrows;
	}

	public List<WebElement> getDropdownOptions() {
		return dropdownOptions;
	}
}
