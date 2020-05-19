package com.glenworsley.restdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class RestdemoApplicationTests {

	@Autowired
	private CustomerController customerController;

	@Test
	void contextLoads() {
		assertNotNull(customerController);
	}

}
