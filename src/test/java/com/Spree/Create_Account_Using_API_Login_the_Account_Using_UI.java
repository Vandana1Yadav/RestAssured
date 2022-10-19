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
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Create_Account_Using_API_Login_the_Account_Using_UI {
	WebDriver driver;
	String email1;
	String outh_token;
//	String fname;
  @Test(priority=1)
  public void createAccount() throws IOException, ParseException {
	  
	  RestAssured.baseURI = "https://demo.spreecommerce.org";
		RequestSpecification httpRequest = RestAssured.given();
		// Response response = httpRequest.get();
		
		
		 JSONParser jsonparser=new JSONParser();
		  //Create object for FileReader class, which help to load and read JSON file
		  FileReader reader = new FileReader(".\\JSON_File\\create_account.json");
		  //Returning/assigning to Java Object
		  Object obj = jsonparser.parse(reader);
		  //Convert Java Object to JSON Object, JSONObject is typecast here
		  JSONObject prodjsonobj = (JSONObject)obj;
		  
		  httpRequest.header("Content-Type", "application/json");
		  httpRequest.body(prodjsonobj);
		  Response response = httpRequest.request(Method.POST,"/api/v2/storefront/account");
		  response.prettyPrint();
		  int statusCode = response.getStatusCode();
			System.out.println("Status code is =>  " + statusCode);
			Assert.assertEquals(200, statusCode);
			
		JsonPath js = new JsonPath(response.asString());
	 email1 = js.get("data.attributes.email");
		System.out.println(email1);
//		Assert.assertEquals(email1, "vandnew4@spree.com");
		
		
  }
  
  @Test(priority=2)
  public void loginAccountusingUI() throws InterruptedException {
	  WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://demo.spreecommerce.org/");
		driver.manage().window().maximize();
		
		driver.findElement(By.xpath("//*[@id='account-button']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[text()='LOGIN']")).click();
		Thread.sleep(2000);
		System.out.println(email1);
		driver.findElement(By.xpath("//*[@id='spree_user_email']")).sendKeys(email1);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@name='spree_user[password]']")).sendKeys("pass1234");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@name='commit']")).click();
		Thread.sleep(2000);
		
	  
  }
  
  @Test(priority=3)
  public void retrieveAccount() throws IOException, ParseException {
	  
	  RestAssured.baseURI = "https://demo.spreecommerce.org";
		RequestSpecification request = RestAssured.given();

		JSONObject requestParams = new JSONObject();
		 requestParams.put("grant_type", "password");
		 requestParams.put("username", email1);
		 requestParams.put("password", "pass1234");
	 // Add a header stating the Request body is a JSON
		 request.header("Content-Type", "application/json"); 
		 request.body(requestParams.toJSONString());
		 Response response = request.post("/spree_oauth/token");
		 
		 int statusCode1 = response.getStatusCode();
		 System.out.println("Status code1 is =>  " + statusCode1);
		 Assert.assertEquals(statusCode1, 200); 
		 
		 // Now let us print the body of the message to see what response
		  // we have recieved from the server
		  String responseBody = response.getBody().asString();
		  
		 // String responseBody = response.getBody().toString();
		  //System.out.println("Response Body is =>  " + responseBody);
		  JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		  outh_token=jsonPathEvaluator.get("access_token").toString();
		  System.out.println("oAuth Token is =>  " + outh_token);
		  request.header("Authorization", "Bearer "+outh_token);
			
	//	request.body(prodjsonobj);
		// POST the Response
		Response response1 = request.get("/api/v2/storefront/account");
		response.prettyPrint();	
			
		
  }
  
  @Test(priority=4)
  public void updateAccount() throws IOException, ParseException {

	  RestAssured.baseURI = "https://demo.spreecommerce.org";
	  RequestSpecification httpRequest = RestAssured.given();

	 /* JSONObject requestParams = new JSONObject();
	  requestParams.put("grant_type", "password");
	  System.out.println("updateAccount email  =>  "+ email1);
	  requestParams.put("username", email1);
	  requestParams.put("password", "pass1234");*/

	  JSONParser jsonparser=new JSONParser();
	  FileReader reader = new FileReader(".\\JSON_File\\update_account.json");
	  Object obj = jsonparser.parse(reader);
	  JSONObject prodjsonobj = (JSONObject)obj;

	  httpRequest.header("Content-Type", "application/json");
	  System.out.println("outh_token  =>  " + outh_token);
	  httpRequest.header("Authorization", "Bearer "+outh_token);
	  httpRequest.body(prodjsonobj);
	  // POST the Response
	  Response response = httpRequest.request(Method.PATCH,"/api/v2/storefront/account");
	  response.prettyPrint();
	  int statusCode = response.getStatusCode();
	  Assert.assertEquals(statusCode, 200);

	  JsonPath jsonPathEvaluator = response.getBody().jsonPath();
	  String email = jsonPathEvaluator.get("data.attributes.email").toString();
	  System.out.println("Email  =>  " + email);

  }
  
  
}
