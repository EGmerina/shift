package shift.sellersandtransactions.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shift.sellersandtransactions.api.dto.AnalyticsPeriod;
import shift.sellersandtransactions.api.dto.BestPeriodResponseDto;
import shift.sellersandtransactions.api.dto.SellerResponseDto;
import shift.sellersandtransactions.core.service.AnalyticsService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/best-seller")
    public SellerResponseDto getBestSeller(@RequestParam AnalyticsPeriod period) {
        return analyticsService.getBestSeller(period);
    }

    @GetMapping("/{sellerId}/best-period")
    public BestPeriodResponseDto getBestPeriodForSeller(
            @PathVariable Long sellerId,
            @RequestParam(defaultValue = "7") int periodInDays) {
        return analyticsService.calculateBestPeriod(sellerId, periodInDays);
    }

    @GetMapping("/sellers/sum-less-than")
    public List<SellerResponseDto> getSellersWithSumLessThan(@RequestParam BigDecimal amount) {
        return analyticsService.getSellersWithTotalSumLessThan(amount);
    }
}