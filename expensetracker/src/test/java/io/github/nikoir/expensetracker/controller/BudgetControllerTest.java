package io.github.nikoir.expensetracker.controller;

import com.c4_soft.springaddons.security.oauth2.test.annotations.WithMockAuthentication;
import io.github.nikoir.expensetracker.configuration.TestSecurityConfig;
import io.github.nikoir.expensetracker.dto.request.BudgetCreateDto;
import io.github.nikoir.expensetracker.dto.request.BudgetSearchRequestDto;
import io.github.nikoir.expensetracker.dto.response.BudgetViewDto;
import io.github.nikoir.expensetracker.security.SecurityConfig;
import io.github.nikoir.expensetracker.service.BudgetService;
import io.github.nikoir.expensetracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static io.github.nikoir.expensetracker.util.JsonUtil.toJson;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO: добавить тестирование валидации
@WebMvcTest(BudgetController.class)
@Import({TestSecurityConfig.class, SecurityConfig.class})
public class BudgetControllerTest {
    @MockitoBean
    public UserService userService; //WebMvcTest пытается поднять KeycloalConverter TODO: выпилить отсюда, когда выпилю из keycloak

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BudgetService budgetService;

    @Test
    void testUnauthorized() throws Exception {
        mockMvc.perform(get("/budgets"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockAuthentication
    void testGetAll() throws Exception {
        Instant now = Instant.now();
        PagedModel<BudgetViewDto> mockPage = new PagedModel<>(new PageImpl<>(List.of(
            new BudgetViewDto(
                    1L,
                    "Еда",
                    "Месяц",
                    BigDecimal.valueOf(30000),
                    "RUB",
                    now,
                    now.plus(30, ChronoUnit.DAYS)
            ),
            new BudgetViewDto(
                    2L,
                    "Транспорт",
                    "Месяц",
                    BigDecimal.valueOf(15000),
                    "RUB",
                    now,
                    now.plus(30, ChronoUnit.DAYS))
        )));

        when(budgetService.getAllBudgets(any(BudgetSearchRequestDto.class)))
                .thenReturn(mockPage);

        // When/Then
        mockMvc.perform(get("/budgets")
                        .param("zoneId", "UTC")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].categoryName").value("Еда"))
                .andExpect(jsonPath("$.content[0].periodName").value("Месяц"))
                .andExpect(jsonPath("$.content[0].amount").value(30000))
                .andExpect(jsonPath("$.content[0].currency").value("RUB"))
                .andExpect(jsonPath("$.content[0].startDate", notNullValue()))
                .andExpect(jsonPath("$.content[0].endDate", notNullValue()))
                .andExpect(jsonPath("$.content[1].id").value("2"))
                .andExpect(jsonPath("$.content[1].categoryName").value("Транспорт"))
                .andExpect(jsonPath("$.content[1].amount").value(15000));
    }

    @Test
    @WithMockAuthentication
    void testCreate() throws Exception {
        Instant now = Instant.now();

        when(budgetService.createBudget(any(BudgetCreateDto.class))).thenReturn(new BudgetViewDto(
                1L,
                "Еда",
                "Месяц",
                BigDecimal.valueOf(30000),
                "RUB",
                now,
                now.plus(30, ChronoUnit.DAYS)
        ));

        mockMvc.perform(post("/budgets")
                .param("budgetCreateDto", toJson(BudgetCreateDto.builder().build())));

    }
}
