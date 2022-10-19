package com.Spree;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Create_Address_Using_API_Delete_Using_UI extends Base_Class {
 
String fname;
	
	WebDriver driver;
	
	@Test(priority = 1)
	public void POST_Create_Address() throws IOException, ParseException {

		RestAssured.baseURI = "https://demo.spreecommerce.org";
		RequestSpecification request = RestAssured.given();

		//Create json object of JSONParser class to parse the JSON data
		  JSONParser jsonparser=new JSONParser();
		  //Create object for FileReader class, which help to load and read JSON file
		  FileReader reader = new FileReader(".\\JSON_File\\create_address.json");
		  //Returning/assigning to Java Object
		  Object obj = jsonparser.parse(reader);
		  //Convert Java Object to JSON Object, JSONObject is typecast here
		  JSONObject prodjsonobj = (JSONObject)obj;

		// Add a header stating the Request body is a JSON
		request.header("Content-Type", "application/json");
		request.header("Authorization", "Bearer "+Base_Class.oAuth_Token());
		request.body(prodjsonobj);
		// POST the Response
		Response response = request.post("/api/v2/storefront/account/addresses");
		response.prettyPrint();
		int statusCode = response.getStatusCode();
		// System.out.println(response.asString());
		Assert.assertEquals(statusCode, 200);

		JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		fname = jsonPathEvaluator.get("data.attributes.firstname").toString();
		System.out.println("First Name  =>  " + fname);
		Assert.assertEquals(fname, "Rest");

	}
	
	//Login to UI and Delete the address based on Address created through API
	
	@Test(priority = 2)
	public void login() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		//ChromeOptions option = new ChromeOptions();
		//option.setHeadless(false);
		//option.addArguments("incognito");
		driver = new ChromeDriver();
		driver.get("https://demo.spreecommerce.org/");
		driver.manage().window().maximize();
		
		driver.findElement(By.xpath("//*[@id='account-button']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[text()='LOGIN']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@id='spree_user_email']")).sendKeys("vandana@spree.com");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@name='spree_user[password]']")).sendKeys("vandanayadav");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@name='commit']")).click();
		Thread.sleep(2000);
		
		String ActValue = driver.findElement(By.xpath("//*[text()='My Account']")).getText();
		String ExpValue = "MY ACCOUNT";
		Assert.assertEquals(ActValue,ExpValue);

		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[text()='Logged in successfully']")).isDisplayed();
		
		//Click on Delete Link of particular address created via API
		driver.findElement(By.xpath("//span[contains(text(),'" +fname+ "')]/parent::address/parent::div/following-sibling::div//a[@aria-label='Remove Address']//*[name()='svg']")).click();
		Thread.sleep(5000);
		driver.findElement(By.id("delete-address-popup-confirm")).click();
		driver.findElement(By.xpath("//span[normalize-space()='Address has been successfully removed.']")).isDisplayed();
		driver.close();
	}
	
}
