package com.Capstone_Orange_HRM;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test; // Keep this import as @Test is used

public class AdminPage {

	private WebDriver driver;

	public AdminPage(WebDriver d) {
		this.driver = d;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//ul[@class=\"oxd-main-menu\"]/li/a")
	private List<WebElement> menu_items;

	@FindBy(linkText = "Admin")
	private WebElement admin_link;

	@FindBy(xpath = "//div[@class=\"oxd-input-group oxd-input-field-bottom-space\"]/div[2]/input")
	private WebElement username;

	@FindBy(xpath = "//button[@type=\"submit\"]")
	private WebElement search_button;

	@FindBy(xpath = "//div[@class=\"orangehrm-horizontal-padding orangehrm-vertical-padding\"]//span")
	private WebElement records_found;

	@FindBy(xpath = "//i[@class=\"oxd-icon bi-caret-down-fill oxd-select-text--arrow\"]")
	private List<WebElement> drop_down;

	@FindBy(xpath = "//div[@role=\"listbox\"]/div/span")
	private List<WebElement> drop_down_items;

	@FindBy(xpath = "//div[@tabindex=\"0\"]")
	private List<WebElement> user_status_items;

	public List<WebElement> getUser_status_items() {
		return user_status_items;
	}

	public List<WebElement> getDrop_down() {
		return drop_down;
	}

	public List<WebElement> getdrop_down_items() {
		return drop_down_items;
	}

	public WebElement getRecords_found() {
		return records_found;
	}

	public WebElement getUsername() {
		return username;
	}

	public WebElement getSearch_button() {
		return search_button;
	}

	public List<WebElement> getMenu_items() {
		return menu_items;
	}

	public WebElement getAdmin_link() {
		return admin_link;
	}

	@Test // This method is marked as a TestNG test
	public void menu_options() {
		System.out.println("Total number of Menu items:" + getMenu_items().size());
		getAdmin_link().click();
		String url = driver.getCurrentUrl();
		Assert.assertTrue(url.contains("admin"), "Url does not match");
		System.out.println("Logged into Admin Page");
	}

	@Test // This method is marked as a TestNG test
	public void searchByUserName(String un) {
		getUsername().sendKeys(un);
		getSearch_button().click();
		try {
			Thread.sleep(3000); // Using Thread.sleep as per original code
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Total records With username:" + un + " is:" + getRecords_found().getText());
		driver.navigate().refresh();
	}

	@Test // This method is marked as a TestNG test
	public void searchByUserRole() {
		getDrop_down().get(0).click();
		for (WebElement i : getdrop_down_items()) {
			if (i.getText().contains("Admin")) {
				i.click();
				break;
			}
		}
		getSearch_button().click();
		try {
			Thread.sleep(3000); // Using Thread.sleep as per original code
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Total records with User Role Admin:" + getRecords_found().getText());
		driver.navigate().refresh();
	}

	public void searchByUserStatus() { // This method is NOT marked as a TestNG test
		try {
			Thread.sleep(3000); // Using Thread.sleep as per original code
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getDrop_down().get(1).click();
		for (WebElement i : getdrop_down_items()) {
			if (i.getText().contains("Enabled")) {
				i.click();
				break;
			}
		}
		getSearch_button().click();
		try {
			Thread.sleep(3000); // Using Thread.sleep as per original code
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Total Records enabled:" + getRecords_found().getText());
	}
}
