package io.github.nikoir.expensetracker.controller;
import com.c4_soft.springaddons.security.oauth2.test.annotations.WithJwt;
import com.c4_soft.springaddons.security.oauth2.test.annotations.WithMockAuthentication;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.nikoir.expensetracker.configuration.TestSecurityConfig;
import io.github.nikoir.expensetracker.dto.request.CurrencyModifyDto;
import io.github.nikoir.expensetracker.dto.response.CurrencyViewDto;
import io.github.nikoir.expensetracker.security.SecurityConfig;
import io.github.nikoir.expensetracker.service.CurrencyService;
import io.github.nikoir.expensetracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class) //изолированно загружает Spring MVC контроллеры без полного контекста Spring
@Import({TestSecurityConfig.class, SecurityConfig.class})
public class CurrencyControllerTest {
    @MockitoBean
    public UserService userService; //этот гребаный WebMvcTest все равно пытается поднять KeycloalConverter TODO: выпилить нахрен отсюда, когда выпилю из keycloak

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CurrencyService currencyService;

    @Test
    void testUnauthorized() throws Exception {
        mockMvc.perform(get("/currencies"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockAuthentication()
    void testGetAll() throws Exception {

        List<CurrencyViewDto> currencies = List.of(
                new CurrencyViewDto("USD", "US Dollar", "$"),
                new CurrencyViewDto("RUB", "Russian Ruble", "₽"),
                new CurrencyViewDto("EUR", "Euro", "€")
        );

        when(currencyService.getAll()).thenReturn(currencies);

        mockMvc.perform(get("/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].code").value("USD"))
                .andExpect(jsonPath("$[0].name").value("US Dollar"))
                .andExpect(jsonPath("$[0].symbol").value("$"))
                .andExpect(jsonPath("$[1].code").value("RUB"))
                .andExpect(jsonPath("$[2].name").value("Euro"));
    }

    @Test
    @WithJwt("admin_user_token.json")
    void testGetByCode() throws Exception {
        String testCode = "USD";
        CurrencyViewDto mockDto = new CurrencyViewDto(
                testCode,
                "US Dollar",
                "$");

        when(currencyService.getByCode(testCode)).thenReturn(mockDto);

        mockMvc.perform(get("/currencies/by-code/{code}", testCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(testCode))
                .andExpect(jsonPath("$.name").value("US Dollar"))
                .andExpect(jsonPath("$.symbol").value("$"));
    }


    @Test
    @WithMockAuthentication
    void testForbidden() throws Exception {
        CurrencyModifyDto modifyDto = new CurrencyModifyDto("USD", "US Dollar", "$");
        mockMvc.perform(put("/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(modifyDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithJwt("admin_user_token.json")
    void testCreateSuccessAsAdmin() throws Exception {
        CurrencyModifyDto modifyDto = new CurrencyModifyDto("USD", "US Dollar", "$");
        CurrencyViewDto viewDto = new CurrencyViewDto("USD", "US Dollar", "$");

        when(currencyService.create(any(CurrencyModifyDto.class))).thenReturn(viewDto);

        mockMvc.perform(post("/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(modifyDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("USD"))
                .andExpect(jsonPath("$.name").value("US Dollar"))
                .andExpect(jsonPath("$.symbol").value("$"));
    }

    @Test
    @WithJwt("admin_user_token.json")
    void testUpdateSuccessAsAdmin() throws Exception {
        String currencyId = "USD";
        CurrencyModifyDto modifyDto = new CurrencyModifyDto(currencyId, "US Dollar Updated", "$");
        CurrencyViewDto viewDto = new CurrencyViewDto(currencyId, "US Dollar Updated", "$");

        when(currencyService.update(any(CurrencyModifyDto.class))).thenReturn(viewDto);

        mockMvc.perform(put("/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(modifyDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("US Dollar Updated"));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
