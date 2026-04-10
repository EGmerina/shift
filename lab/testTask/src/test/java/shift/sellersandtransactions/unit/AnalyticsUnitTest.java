package shift.sellersandtransactions.unit;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import shift.sellersandtransactions.api.dto.AnalyticsPeriod;
import shift.sellersandtransactions.api.dto.BestPeriodResponseDto;
import shift.sellersandtransactions.api.dto.SellerResponseDto;
import shift.sellersandtransactions.api.mapper.SellerMapper;
import shift.sellersandtransactions.core.entity.SellerEntity;
import shift.sellersandtransactions.core.repository.SellerRepository;
import shift.sellersandtransactions.core.repository.TransactionRepository;
import shift.sellersandtransactions.core.service.AnalyticsService;
import shift.sellersandtransactions.core.service.SellerService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsUnitTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private SellerMapper sellerMapper;

    @Mock
    private SellerService sellerService;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    @DisplayName("getBestSeller: должен возвращать лучшего продавца")
    void getBestSeller_ShouldReturnTopSeller() {
        SellerEntity seller = new SellerEntity();
        SellerResponseDto response = new SellerResponseDto(1L, "Top Seller", "info", LocalDateTime.now());

        when(transactionRepository.findBestSellerForPeriod(any(), any(), any(PageRequest.class)))
                .thenReturn(List.of(seller));
        when(sellerMapper.toResponse(seller)).thenReturn(response);


        SellerResponseDto result = analyticsService.getBestSeller(AnalyticsPeriod.MONTH);

        assertNotNull(result);
        assertEquals("Top Seller", result.name());
        verify(transactionRepository).findBestSellerForPeriod(any(), any(), any());
    }

    @Test
    @DisplayName("getBestSeller: должен кидать ошибку, если транзакций нет")
    void getBestSeller_ShouldThrowException_WhenNoTransactions() {
        when(transactionRepository.findBestSellerForPeriod(any(), any(), any()))
                .thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> analyticsService.getBestSeller(AnalyticsPeriod.DAY));
    }

    @Test
    @DisplayName("calculateBestPeriod: должен найти период с максимальным количеством транзакций")
    void calculateBestPeriod_ShouldFindCorrectWindow() {

        Long sellerId = 1L;
        int periodDays = 3;

        LocalDateTime day1 = LocalDateTime.of(2023, 10, 1, 10, 0);
        LocalDateTime day2 = LocalDateTime.of(2023, 10, 2, 10, 0);
        LocalDateTime day2Later = LocalDateTime.of(2023, 10, 2, 15, 0);
        LocalDateTime day10 = LocalDateTime.of(2023, 10, 10, 10, 0);

        List<LocalDateTime> dates = List.of(day1, day2, day2Later, day10);

        when(transactionRepository.findAllTransactionDatesBySellerId(sellerId)).thenReturn(dates);

        BestPeriodResponseDto result = analyticsService.calculateBestPeriod(sellerId, periodDays);

        assertNotNull(result);
        assertEquals(3, result.transactionCount()); // Должен найти 3 транзакции (с 1 по 3 октября)
        assertEquals(LocalDate.of(2023, 10, 1), result.startDate());
        verify(sellerService).getSellerOrThrow(sellerId);
    }

    @Test
    @DisplayName("calculateBestPeriod: должен кидать ошибку, если у продавца нет транзакций")
    void calculateBestPeriod_ShouldThrowException_WhenNoDates() {
        Long sellerId = 1L;
        when(transactionRepository.findAllTransactionDatesBySellerId(sellerId)).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> analyticsService.calculateBestPeriod(sellerId, 7));
    }

    @Test
    @DisplayName("getSellersWithTotalSumLessThan: должен возвращать список продавцов")
    void getSellersWithTotalSumLessThan_ShouldReturnList() {
        BigDecimal limit = BigDecimal.valueOf(1000);
        SellerEntity seller = new SellerEntity();
        when(sellerRepository.findSellersWithTotalTransactionsAmountLessThan(limit))
                .thenReturn(List.of(seller));
        when(sellerMapper.toResponse(seller))
                .thenReturn(new SellerResponseDto(1L, "Small Seller", "info", LocalDateTime.now()));

        List<SellerResponseDto> result = analyticsService.getSellersWithTotalSumLessThan(limit);

        assertEquals(1, result.size());
        assertEquals("Small Seller", result.getFirst().name());
        verify(sellerRepository).findSellersWithTotalTransactionsAmountLessThan(limit);
    }
}