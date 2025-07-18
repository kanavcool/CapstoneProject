package com.E_commerce_Pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage {
	
 WebDriver driver;
	
	public HomePage(WebDriver driver) {
		this.driver = driver;
		}
	
	@FindBy(id="login2") private WebElement login;
	@FindBy(id="signin2") private WebElement signup;
	@FindBy(id="loginusername") private WebElement userName;
	@FindBy(id="loginpassword") private WebElement pass;
	@FindBy(xpath="button[text()='Log in']") private WebElement loginButton;
	@FindBy(id="sign-username") private WebElement signupname;
	@FindBy(id="sign-password") private WebElement Signuppass;
	@FindBy(xpath="button[text()='Sign up']") private WebElement signupButton;

	public WebElement getLogin() {
		return login;
	}

	public WebElement getSignup() {
		return signup;
	}

	public WebElement getUserName() {
		return userName;
	}

	public WebElement getPass() {
		return pass;
	}

	public WebElement getLoginButton() {
		return loginButton;
	}

	public WebElement getSignupname() {
		return signupname;
	}

	public WebElement getSignuppass() {
		return Signuppass;
	}
	public WebElement getSignupButton() {
		return signupButton;
	}

	
	
	public void signupPage(String UN,String Pass)
	
	{
		getSignup().click();
		getSignupname().sendKeys(UN);
		getSignuppass().sendKeys(Pass);
		getSignupButton().click();
		Alert a = driver.switchTo().alert();
		a.accept();

	}
	
	public void loginPage(String UN,String Pass)
	{
		getLogin().click();
		getUserName().sendKeys(UN);
		getPass().sendKeys(Pass);
		getLoginButton().click();

	}



}
