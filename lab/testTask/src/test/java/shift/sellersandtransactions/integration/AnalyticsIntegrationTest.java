package shift.sellersandtransactions.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import shift.sellersandtransactions.core.entity.PaymentType;
import shift.sellersandtransactions.core.entity.SellerEntity;
import shift.sellersandtransactions.core.entity.TransactionEntity;
import shift.sellersandtransactions.core.repository.SellerRepository;
import shift.sellersandtransactions.core.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AnalyticsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    @DisplayName("Интеграционный тест: Поиск лучшего продавца за период")
    void shouldReturnBestSeller() throws Exception {
        SellerEntity seller1 = createSeller("Продавец 1");
        SellerEntity seller2 = createSeller("Продавец 2");
        createTransaction(seller1, new BigDecimal("100.00"));
        createTransaction(seller2, new BigDecimal("500.00"));

        mockMvc.perform(get("/api/analytics/best-seller")
                        .param("period", "YEAR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Продавец 2")));
    }

    private SellerEntity createSeller(String name) {
        SellerEntity s = new SellerEntity();
        s.setName(name);
        s.setContactInfo("test@test.ru");
        return sellerRepository.save(s);
    }

    private void createTransaction(SellerEntity seller, BigDecimal amount) {
        TransactionEntity t = new TransactionEntity();
        t.setSeller(seller);
        t.setAmount(amount);
        t.setPaymentType(PaymentType.CARD);
        t.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(t);
    }
}