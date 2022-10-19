package com.Spree;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Get_Default_Country_Using_JSONPath {

	@Test
	public void Get_Default_Country() {

		RestAssured.baseURI = "https://demo.spreecommerce.org";
		RequestSpecification httpRequest = RestAssured.given();
		// Response response = httpRequest.get();
		Response response = httpRequest.request(Method.GET, "/api/v2/storefront/countries/default");

		// Now let us print the body of the message to see what response
		// we have recieved from the server
		String responseBody = response.getBody().prettyPrint();
		System.out.println("Response Body is =>  " + responseBody);
		// Status Code Validation
		int statusCode = response.getStatusCode();
		System.out.println("Status code is =>  " + statusCode);
		Assert.assertEquals(200, statusCode);

		// First get the JsonPath object instance from the Response interface
		//Assert.assertEquals(responseBody.contains("UNITED STATES") /* Expected value */, true /* Actual Value */);

		// Verify using JSON Path Value

		JsonPath js = new JsonPath(response.asString());
		String iso_name = js.get("data.attributes.iso_name");
		System.out.println(iso_name);
		Assert.assertEquals(iso_name, "UNITED STATES");

	}
}
