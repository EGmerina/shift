package shift.sellersandtransactions.core.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shift.sellersandtransactions.api.dto.AnalyticsPeriod;
import shift.sellersandtransactions.api.dto.SellerResponseDto;
import shift.sellersandtransactions.api.mapper.SellerMapper;
import shift.sellersandtransactions.core.entity.TransactionEntity;
import shift.sellersandtransactions.core.repository.SellerRepository;
import shift.sellersandtransactions.core.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {

    private final SellerRepository sellerRepository;
    private final TransactionRepository transactionRepository;
    private final SellerService sellerService;
    private final SellerMapper sellerMapper;


    public SellerResponseDto getBestSeller(AnalyticsPeriod period) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = calculateStartDate(period, endDate);

        var topSellers = transactionRepository.findBestSellerForPeriod(
                startDate, endDate, PageRequest.of(0, 1)
        );

        if (topSellers.isEmpty()) {
            throw new EntityNotFoundException("За указанный период транзакций не найдено");
        }

        return sellerMapper.toResponse(topSellers.getFirst());
    }


    public List<SellerResponseDto> getSellersWithTotalSumLessThan(BigDecimal amount) {
        return sellerRepository.findSellersWithTotalTransactionsAmountLessThan(amount)
                .stream()
                .map(sellerMapper::toResponse)
                .toList();
    }

    private LocalDateTime calculateStartDate(AnalyticsPeriod period, LocalDateTime endDate) {
        return switch (period) {
            case AnalyticsPeriod.DAY -> endDate.minusDays(1);
            case AnalyticsPeriod.MONTH -> endDate.minusMonths(1);
            case AnalyticsPeriod.QUARTER -> endDate.minusMonths(3);
            case AnalyticsPeriod.YEAR -> endDate.minusYears(1);
        };
    }

    public String calculateBestPeriod(Long sellerId, LocalDate startDate, LocalDate endDate, AnalyticsPeriod periodType) {

        // Превращаем LocalDate в LocalDateTime (начало и конец дня), если в БД транзакции хранятся с часами и минутами
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        Pageable limitOne = PageRequest.of(0, 1);

        return switch (periodType) {
            case DAY -> transactionRepository.findBestDay(sellerId, start, end, limitOne);
            case MONTH -> transactionRepository.findBestMonth(sellerId, start, end, limitOne);
            case YEAR -> transactionRepository.findBestYear(sellerId, start, end, limitOne);
            default -> throw new IllegalArgumentException("Выберите другой период (день, месяц, год)");
        };
    }
}