package shift.sellersandtransactions.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import shift.sellersandtransactions.api.dto.AnalyticsPeriod;
import shift.sellersandtransactions.api.dto.SellerResponseDto;
import shift.sellersandtransactions.core.service.AnalyticsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    public String getBestPeriodForSeller(
            @PathVariable Long sellerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam AnalyticsPeriod periodType
    ) {
        return analyticsService.calculateBestPeriod(sellerId, startDate, endDate, periodType);
    }

    @GetMapping("/sellers/sum-less-than")
    public List<SellerResponseDto> getSellersWithSumLessThan(@RequestParam BigDecimal amount) {
        return analyticsService.getSellersWithTotalSumLessThan(amount);
    }
}