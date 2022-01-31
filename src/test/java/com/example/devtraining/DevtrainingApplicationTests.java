package com.example.devtraining;

import com.example.devtraining.controller.EmployeeController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DevtrainingApplicationTests {

	@Autowired
	EmployeeController employeeController;

	@Test
	void contextLoads() {
		Assertions.assertThat(employeeController).isNotNull();
	}

}
