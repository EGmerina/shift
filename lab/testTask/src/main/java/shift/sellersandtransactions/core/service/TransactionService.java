package shift.sellersandtransactions.core.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shift.sellersandtransactions.api.dto.TransactionRequestDto;
import shift.sellersandtransactions.api.dto.TransactionResponseDto;
import shift.sellersandtransactions.api.mapper.TransactionMapper;
import shift.sellersandtransactions.core.entity.SellerEntity;
import shift.sellersandtransactions.core.entity.TransactionEntity;
import shift.sellersandtransactions.core.repository.TransactionRepository;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final SellerService sellerService;

    public List<TransactionResponseDto> findAll() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toResponse)
                .toList();
    }


    public TransactionResponseDto findByTransactionId(Long id) {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Транзакция с id " + id + " не найдена"));
        return transactionMapper.toResponse(transaction);
    }

    @Transactional
    public TransactionResponseDto create(TransactionRequestDto request) {

        SellerEntity seller = sellerService.getSellerOrThrow(request.sellerId());
        TransactionEntity newTransaction = transactionMapper.toEntity(request, seller);
        TransactionEntity savedTransaction = transactionRepository.save(newTransaction);
        return transactionMapper.toResponse(savedTransaction);
    }

    public List<TransactionResponseDto> getTransactionsBySellerId(Long sellerId) {
        List<TransactionEntity> transactions = transactionRepository.findAllBySellerId(sellerId);
        return transactions.stream()
                .map(transactionMapper::toResponse)
                .toList();
    }
}