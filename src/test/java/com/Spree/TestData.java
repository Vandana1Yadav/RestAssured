package com.Spree;

import org.testng.annotations.DataProvider;

public class TestData {

	@DataProvider(name = "Country_Names")
	public Object[][] getCountryName() {
		// Multidimensional Object
		// 3X3 or 4X3 or 4X5
		return new Object[][] {

				{ "IN", "INDIA" },
				{ "US", "UNITED STATES" },
				{ "UA", "UKRAINE" },
				{ "AF", "AFGHANISTAN" },
				{ "RU", "RUSSIA" }};

	}
	

	
}
