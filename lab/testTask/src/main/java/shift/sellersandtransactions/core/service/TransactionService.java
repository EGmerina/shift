package shift.sellersandtransactions.core.service;

import jakarta.validation.Valid;
import shift.sellersandtransactions.api.dto.TransactionRequestDto;
import shift.sellersandtransactions.api.dto.TransactionResponseDto;

import java.util.List;

public class TransactionService {
    public List<TransactionResponseDto> findAll() {
    }

    public TransactionResponseDto create(@Valid TransactionRequestDto request) {
    }

    public TransactionResponseDto findById(Long id) {
    }
}
