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

import shift.sellersandtransactions.api.dto.TransactionRequestDto;
import shift.sellersandtransactions.core.entity.SellerEntity;
import shift.sellersandtransactions.core.repository.SellerRepository;
import shift.sellersandtransactions.core.repository.TransactionRepository;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shift.sellersandtransactions.core.entity.PaymentType.CARD;
import static shift.sellersandtransactions.core.entity.PaymentType.CASH;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TransactionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Test
    @DisplayName("Интеграционный тест: Успешное создание транзакции и привязка к продавцу")
    void shouldCreateTransactionSuccessfully() throws Exception {
        SellerEntity seller = new SellerEntity();
        seller.setName("Магазин Электроники");
        seller.setContactInfo("electro@shift.ru");
        SellerEntity savedSeller = sellerRepository.save(seller);

        TransactionRequestDto request = new TransactionRequestDto(
                savedSeller.getId(),
                new BigDecimal("25000.50"),
                CARD
        );

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.seller.id", is(savedSeller.getId().intValue())))
                .andExpect(jsonPath("$.amount", is(25000.50)))
                .andExpect(jsonPath("$.paymentType", is("CARD")));

        assertEquals(1, transactionRepository.count());
    }

    @Test
    @DisplayName("Интеграционный тест: Получение списка транзакций по ID продавца")
    void shouldReturnTransactionsBySellerId() throws Exception {
        SellerEntity seller = new SellerEntity();
        seller.setName("Кофейня");
        seller.setContactInfo("coffee@shift.ru");
        SellerEntity savedSeller = sellerRepository.save(seller);
        TransactionRequestDto request1 = new TransactionRequestDto(savedSeller.getId(), new BigDecimal("350.00"), CASH);
        TransactionRequestDto request2 = new TransactionRequestDto(savedSeller.getId(), new BigDecimal("450.00"), CARD);

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)));
        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)));

        mockMvc.perform(get("/api/transactions")
                        .param("seller.id", savedSeller.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].amount", is(350.00)))
                .andExpect(jsonPath("$[1].amount", is(450.00)));
    }
}