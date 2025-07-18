package com.E_commerce_Pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AddtoCart {

	WebDriver driver;

	public AddtoCart(WebDriver driver) {
		this.driver = driver;
	}

	@FindBy(xpath = "button[text()='Nokia lumia 1520']")
	private WebElement product;
	@FindBy(xpath = "button[text()='Add to cart']")
	private WebElement addtocart;
	@FindBy(xpath = "button[text()='Cart']")
	private WebElement cart;
	@FindBy(xpath = "button[text()='Place Order']")
	private WebElement placeorder;
	@FindBy(id = "name")
	private WebElement name;
	@FindBy(id = "country")
	private WebElement country;
	@FindBy(id = "city")
	private WebElement city;
	@FindBy(id = "card")
	private WebElement card;
	@FindBy(id = "month")
	private WebElement month;
	@FindBy(id = "year")
	private WebElement year;
	@FindBy(xpath = "button[text()='Purchase']")
	private WebElement purchase;

	public WebElement getProduct() {
		return product;
	}

	public WebElement getAddtocart() {
		return addtocart;
	}

	public WebElement getCart() {
		return cart;
	}

	public WebElement getPlaceorder() {
		return placeorder;

	}

	public WebElement getName() {
		return name;
	}

	public WebElement getCountry() {
		return country;
	}

	public WebElement getCity() {
		return city;
	}

	public WebElement getCard() {
		return card;
	}

	public WebElement getMonth() {
		return month;
	}

	public WebElement getYear() {
		return year;
	}

	public WebElement getPurchase() {
		return purchase;
	}

	public void productselection()

	{
		getProduct().click();
		getAddtocart().click();
		Alert a = driver.switchTo().alert();
		a.accept();

	}

	public void checkout(String name, String country, String city, String card, String month, String year) {
		getCart().click();
		getPlaceorder().click();
		getName().sendKeys(name);
		getCountry().sendKeys(country);

		getCity().sendKeys(city);
		getCard().sendKeys(card);

		getMonth().sendKeys(month);
		getYear().sendKeys(year);
		getPurchase().click();

	}

}
