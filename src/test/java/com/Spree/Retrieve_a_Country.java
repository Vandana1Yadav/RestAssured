package com.Spree;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Retrieve_a_Country {
  

  @Test(priority = 1, dataProvider = "Country_Names",dataProviderClass=TestData.class)
public void retrieve_Country(String iso, String iso_name) throws IOException, ParseException {

	  RestAssured.baseURI ="https://demo.spreecommerce.org";
		 RequestSpecification request = RestAssured.given();
		 
	Response response = request.get("api/v2/storefront/countries/"+iso);
	response.prettyPrint();
	int statusCode = response.getStatusCode();
	// System.out.println(response.asString());
	Assert.assertEquals(statusCode, 200);

	JsonPath jsonPathEvaluator = response.getBody().jsonPath();
	String cname = jsonPathEvaluator.get("data.attributes.iso_name").toString();
	System.out.println("Country Name  =>  " + cname);
	Assert.assertEquals(cname, iso_name);

}
}
