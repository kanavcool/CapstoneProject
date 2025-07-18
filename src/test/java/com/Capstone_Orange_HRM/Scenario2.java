package com.Capstone_Orange_HRM;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class Scenario2 {

	public WebDriver driver;
	public LoginPage l1;
	public AdminPage a1;

	@BeforeTest
	public void beforeTest() {
		// Set the path to your chromedriver.exe (replace with your actual path)
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\kanav\\.cache\\selenium\\chromedriver\\win64\\138.0.7204.157\\chromedriver.exe"); // Path// based// on// previous// successful// output
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

		l1 = new LoginPage(driver);
		a1 = new AdminPage(driver);
	}

	@Test
	public void testLogin_and_AdminPage() {
		l1.login("Admin", "admin123");
		a1.menu_options();
		a1.searchByUserName("Admin"); // This method has @Test in Admin_Page, which means TestNG will try to run it as
										// a separate test
		a1.searchByUserRole(); // This method has @Test in Admin_Page, which means TestNG will try to run it as
								// a separate test
		a1.searchByUserStatus();
	}

	@AfterTest
	public void afterTest() {
		if (driver != null) {
			driver.quit();
		}
	}
}
