package shift.sellersandtransactions.core.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shift.sellersandtransactions.api.dto.AnalyticsPeriod;
import shift.sellersandtransactions.api.dto.BestPeriodResponseDto;
import shift.sellersandtransactions.api.dto.SellerResponseDto;
import shift.sellersandtransactions.api.mapper.SellerMapper;
import shift.sellersandtransactions.core.repository.SellerRepository;
import shift.sellersandtransactions.core.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {

    private final SellerRepository sellerRepository;
    private final TransactionRepository transactionRepository;
    private final SellerMapper sellerMapper;
    private final SellerService sellerService;


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

    public BestPeriodResponseDto calculateBestPeriod(Long sellerId, int periodInDays) {

        sellerService.getSellerOrThrow(sellerId);
        List<LocalDateTime> dates = transactionRepository.findAllTransactionDatesBySellerId(sellerId);

        if (dates.isEmpty()) {
            throw new EntityNotFoundException("У продавца нет транзакций");
        }
        int maxTransactions = 0;
        LocalDateTime bestWindowStart = dates.getFirst();

        int left = 0;

        for (int right = 0; right < dates.size(); right++) {
            LocalDateTime rightDate = dates.get(right);
            while (rightDate.isAfter(dates.get(left).plusDays(periodInDays))) {
                left++;
            }

            int currentWindowSize = right - left + 1;
            if (currentWindowSize > maxTransactions) {
                maxTransactions = currentWindowSize;
                bestWindowStart = dates.get(left);
            }
        }

        return new BestPeriodResponseDto(
                sellerId,
                periodInDays,
                bestWindowStart.toLocalDate(),
                bestWindowStart.plusDays(periodInDays).toLocalDate(),
                maxTransactions
        );
    }
}