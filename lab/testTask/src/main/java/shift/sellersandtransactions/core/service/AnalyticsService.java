package shift.sellersandtransactions.core.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Transactional;
import shift.sellersandtransactions.api.dto.SellerResponseDto;
import shift.sellersandtransactions.api.mapper.SellerMapper;
import shift.sellersandtransactions.core.entity.TransactionEntity;
import shift.sellersandtransactions.core.repository.SellerRepository;
import shift.sellersandtransactions.core.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Аналитика ничего не меняет в базе, только читает
public class AnalyticsService {

    private final SellerRepository sellerRepository;
    private final TransactionRepository transactionRepository;
    private final SellerService sellerService;
    private final SellerMapper sellerMapper;

    /**
     * Аналитика: Получить самого продуктивного продавца за период
     */
    public SellerResponseDto getBestSeller(String period) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = calculateStartDate(period, endDate);

        // Используем PageRequest.of(0, 1) вместо LIMIT 1
        var topSellers = transactionRepository.findBestSellerForPeriod(
                startDate, endDate, PageRequest.of(0, 1)
        );

        if (topSellers.isEmpty()) {
            throw new EntityNotFoundException("За указанный период транзакций не найдено");
        }

        return sellerMapper.toResponse(topSellers.get(0));
    }

    /**
     * Аналитика: Получить самое продуктивное время продавца (по сумме транзакций)
     */
    public String getBestTimeForSeller(Long sellerId) {
        // 1. Проверяем, существует ли вообще такой продавец
        sellerService.getSellerOrThrow(sellerId);

        // 2. Достаем все его транзакции
        List<TransactionEntity> transactions = transactionRepository.findAllBySellerId(sellerId);

        if (transactions.isEmpty()) {
            throw new EntityNotFoundException("У продавца пока нет транзакций");
        }

        // 3. Группируем транзакции по часам и суммируем их выручку
        // Ключ: час (например, 14), Значение: общая сумма (например, 5000.00)
        Map<Integer, BigDecimal> salesByHour = transactions.stream()
                .collect(Collectors.toMap(
                        t -> t.getTransactionDate().getHour(), // Берем только час из даты
                        TransactionEntity::getAmount,                // Берем сумму
                        BigDecimal::add                        // Если в один час было несколько транзакций - складываем их
                ));

        // 4. Находим час с максимальной суммой
        int bestHour = salesByHour.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);

        // 5. Форматируем результат в красивую строку "HH:00 - HH:59"
        return String.format("%02d:00 - %02d:59", bestHour, bestHour);
    }

    /**
     * Аналитика: Получить список продавцов с суммой меньше указанной
     */
    public List<SellerResponseDto> getSellersWithTotalSumLessThan(BigDecimal amount) {
        return sellerRepository.findSellersWithTotalTransactionsAmountLessThan(amount)
                .stream()
                .map(sellerMapper::toResponse)
                .collect(Collectors.toList());
    }

    // --- Вспомогательные методы ---

    /**
     * Вычисляет начальную дату на основе переданного периода.
     * Использует современный switch-expression из Java 14+.
     */
    private LocalDateTime calculateStartDate(String period, LocalDateTime endDate) {
        return switch (period.toUpperCase()) {
            case "DAY" -> endDate.minusDays(1);     // За последние 24 часа
            case "MONTH" -> endDate.minusMonths(1); // За последний месяц
            case "QUARTER" -> endDate.minusMonths(3); // За квартал (3 месяца)
            case "YEAR" -> endDate.minusYears(1);   // За последний год
            default -> throw new IllegalArgumentException("Неверный период. Используйте DAY, MONTH, QUARTER или YEAR");
        };
    }
}