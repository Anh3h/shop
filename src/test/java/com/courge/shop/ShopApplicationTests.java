package com.courge.shop;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
		{ TestCustomer.class,
		TestProduct.class }
)
public class ShopApplicationTests {

	@Test
	public void contextLoads() {
	}

}