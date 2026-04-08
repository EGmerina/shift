package shift.sellersandtransactions.api.contoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shift.sellersandtransactions.api.dto.SellerRequestDto;
import shift.sellersandtransactions.api.dto.SellerResponseDto;
import shift.sellersandtransactions.api.dto.TransactionResponseDto;
import shift.sellersandtransactions.core.service.SellerService;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @GetMapping
    public List<SellerResponseDto> getAllSellers() {
        return sellerService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Возвращаем статус 201 Created при успешном создании
    public SellerResponseDto createSeller(@Valid @RequestBody SellerRequestDto request) {
        return sellerService.create(request);
    }

    @GetMapping("/{id}")
    public SellerResponseDto getSellerById(@PathVariable Long id) {
        return sellerService.findById(id);
    }

    @PutMapping("/{id}")
    public SellerResponseDto updateSeller(@PathVariable Long id, @Valid @RequestBody SellerRequestDto request) {
        return sellerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Возвращаем 204 No Content
    public void deleteSeller(@PathVariable Long id) {
        sellerService.delete(id);
    }

    // Тот самый добавленный метод для получения транзакций конкретного продавца
    @GetMapping("/{id}/transactions")
    public List<TransactionResponseDto> getSellerTransactions(@PathVariable Long id) {
        return sellerService.getTransactionsBySellerId(id);
    }
}