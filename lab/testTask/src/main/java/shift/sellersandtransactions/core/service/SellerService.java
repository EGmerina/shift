package shift.sellersandtransactions.core.service;

import jakarta.validation.Valid;
import shift.sellersandtransactions.api.dto.SellerRequestDto;
import shift.sellersandtransactions.api.dto.SellerResponseDto;
import shift.sellersandtransactions.api.dto.TransactionResponseDto;

import java.util.List;

public class SellerService {
    public List<SellerResponseDto> findAll() {
    }

    public SellerResponseDto findById(Long id) {
    }

    public SellerResponseDto update(Long id, @Valid SellerRequestDto request) {
    }

    public void delete(Long id) {
    }

    public List<TransactionResponseDto> getTransactionsBySellerId(Long id) {
        return null;
    }
}
