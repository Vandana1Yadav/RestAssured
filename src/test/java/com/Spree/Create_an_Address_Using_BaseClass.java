package com.Spree;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Create_an_Address_Using_BaseClass extends Base_Class {
	//Global Variable , so that we can use in another method.
		
		
			@Test
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
			String fname = jsonPathEvaluator.get("data.attributes.firstname").toString();
			System.out.println("First Name  =>  " + fname);
			Assert.assertEquals(fname, "Rest");

		}
		
}
