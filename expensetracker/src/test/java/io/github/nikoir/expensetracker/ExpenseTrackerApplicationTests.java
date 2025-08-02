package io.github.nikoir.expensetracker;

import io.github.nikoir.expensetracker.configuration.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest //поднимает полный контекст Spring-Boot для тестирвоания
@Import(TestSecurityConfig.class)
class ExpenseTrackerApplicationTests {

	@Test
	void contextLoads() {

	}

}
