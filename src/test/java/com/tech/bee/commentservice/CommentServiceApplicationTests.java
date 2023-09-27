package com.tech.bee.commentservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.liquibase.enabled=false"
})
class CommentServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
