package shift.sellersandtransactions.api.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shift.sellersandtransactions.api.dto.SellerResponseDto;
import shift.sellersandtransactions.core.service.AnalyticsService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    // В параметрах запроса ожидаем: /api/analytics/best-seller?period=MONTH
    @GetMapping("/best-seller")
    public SellerResponseDto getBestSeller(@RequestParam String period) {
        return analyticsService.getBestSeller(period);
    }

    @GetMapping("/best-time/{sellerId}")
    public Map<String, String> getBestTime(@PathVariable Long sellerId) {
        String productivePeriod = analyticsService.getBestTimeForSeller(sellerId);
        // Возвращаем простой JSON: {"productivePeriod": "14:00 - 15:00"}
        return Map.of("productivePeriod", productivePeriod);
    }

    // В параметрах запроса ожидаем: /api/analytics/sellers/sum-less-than?amount=100000
    @GetMapping("/sellers/sum-less-than")
    public List<SellerResponseDto> getSellersWithSumLessThan(@RequestParam BigDecimal amount) {
        return analyticsService.getSellersWithTotalSumLessThan(amount);
    }
}