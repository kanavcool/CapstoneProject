package com.E_commerce_Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ExecutionClass {
	
	WebDriver driver;
	HomePage homePage;
	AddtoCart addtoCart;
	Extentreport extentReport;
	//Screenshots screenshot;
	@BeforeMethod
	public void setUp() {
	System.setProperty("webdriver.chrome.driver","C:\\Users\\kanav\\Downloads\\chrome-win64");	
	driver = new ChromeDriver();
	driver.get("https://www.demoblaze.com/");
	homePage = new HomePage(driver);
	addtoCart = new AddtoCart(driver);
	extentReport=new Extentreport();
	//screenshot = new Screenshots();
	
	}
	@Test(priority=1)
	public void signupNew() {
	homePage.signupPage("test@123", "Pass1245");
	
	}
	
	@Test(priority=2)
	public void login() {
	homePage.loginPage("test@123", "Pass1245");
	Assert.assertTrue(driver.findElement(By.id("nameofuser")).isDisplayed());
	extentReport.logPass("Login successful");
	}
	
	@Test(priority=3)
	public void checkout() {
		addtoCart.checkout("Test", "USA", "Newyork", "12345678", "12", "2025");
		Assert.assertTrue(driver.findElement(By.xpath("//h2[text()='Thank you for your purchase!']")).isDisplayed());
	}
	
	
	@AfterMethod
	public void tearDown() {
	Screenshots.captureScreenshot(driver, "screen");
	driver.quit();
	extentReport.generateReport();
	}



}
