package com.Capstone_Orange_HRM;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

public class LoginPage {

	WebDriver driver;

	public LoginPage(WebDriver d) {
		this.driver = d;
		PageFactory.initElements(driver, this);
	}

	@FindBy(name = "username")
	private WebElement username;

	@FindBy(name = "password")
	private WebElement password;

	@FindBy(xpath = "//button[@type=\"submit\"]")
	private WebElement submit;

	public WebElement getUsername() {
		return username;
	}

	public WebElement getPassword() {
		return password;
	}

	public WebElement getSubmit() {
		return submit;
	}

	@Test
	public void login(String un, String ps) {
		getUsername().sendKeys(un);
		getPassword().sendKeys(ps);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getSubmit().submit();
		System.out.println("Login Successful");
	}
}
