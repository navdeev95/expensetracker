package io.github.nikoir.expensetracker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest //поднимает полный контекст Spring-Boot для тестирвоания
@ActiveProfiles("dev") //TODO: передавать через параметры gradle
class ExpenseTrackerApplicationTests {

	@Test
	void contextLoads() {

	}

}
