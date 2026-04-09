package shift.sellersandtransactions.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import shift.sellersandtransactions.api.dto.SellerRequestDto;
import shift.sellersandtransactions.core.repository.SellerRepository;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc // Позволяет дергать эндпоинты через mockMvc
@ActiveProfiles("test") // Использует настройки H2 из application-test.yaml
@Transactional // Откатывает базу после каждого теста
class SellerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SellerRepository sellerRepository;

    @Test
    @DisplayName("Интеграционный тест: Успешное создание продавца через API")
    void shouldCreateSellerSuccessfully() throws Exception {
        // 1. Готовим JSON запрос
        SellerRequestDto request = new SellerRequestDto("ООО Ромашка", "romashka@test.ru");

        // 2. Выполняем POST запрос к нашему контроллеру
        mockMvc.perform(post("/api/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // 3. Проверяем HTTP статус и JSON в ответе
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("ООО Ромашка")));

        // 4. Финальная проверка: лезем в реальную БД и смотрим, сколько там записей
        assertEquals(1, sellerRepository.count());
    }
}