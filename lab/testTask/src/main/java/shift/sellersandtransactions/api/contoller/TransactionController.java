package shift.sellersandtransactions.api.contoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shift.sellersandtransactions.api.dto.TransactionRequestDto;
import shift.sellersandtransactions.api.dto.TransactionResponseDto;
import shift.sellersandtransactions.core.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<TransactionResponseDto> getAllTransactions() {
        return transactionService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponseDto createTransaction(@Valid @RequestBody TransactionRequestDto request) {
        return transactionService.create(request);
    }

    @GetMapping("/{id}")
    public TransactionResponseDto getTransactionById(@PathVariable Long id) {
        return transactionService.findById(id);
    }
}