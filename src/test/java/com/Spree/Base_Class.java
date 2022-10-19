package com.Spree;

import org.json.simple.JSONObject;
import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Base_Class {
	
	
	public static String oAuth_Token() {
		  
		 RestAssured.baseURI ="https://demo.spreecommerce.org";
		 RequestSpecification request = RestAssured.given();
		 
		 JSONObject requestParams = new JSONObject();
		 requestParams.put("grant_type", "password");
		 requestParams.put("username", "vandana@spree.com");
		 requestParams.put("password", "vandanayadav");
	 // Add a header stating the Request body is a JSON
		 request.header("Content-Type", "application/json"); 
		 request.body(requestParams.toJSONString());
		 Response response = request.post("/spree_oauth/token");
		 //response.prettyPrint();
		 int statusCode = response.getStatusCode();
		 //System.out.println(response.asString());
		 Assert.assertEquals(statusCode, 200); 
		 
		 // Now let us print the body of the message to see what response
		  // we have recieved from the server
		  String responseBody = response.getBody().asString();
		  
		 // String responseBody = response.getBody().toString();
		  //System.out.println("Response Body is =>  " + responseBody);
		  JsonPath jsonPathEvaluator = response.getBody().jsonPath();
		  String outh_token=jsonPathEvaluator.get("access_token").toString();
		  System.out.println("oAuth Token is =>  " + outh_token);
		  // First get the JsonPath object instance from the Response interface
		  //Assert.assertEquals(responseBody.contains("Product was created with UI.") /*Expected value*/, true /*Actual Value*/, "Response body contains ");
		  return outh_token;
		 }
}
