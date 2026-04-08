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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    // Инжектим SellerService, чтобы не дублировать логику поиска продавца
    private final SellerService sellerService;

    /**
     * Получить список всех транзакций
     */
    public List<TransactionResponseDto> findAll() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получить транзакцию по ID
     */
    public TransactionResponseDto findById(Long id) {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Транзакция с id " + id + " не найдена"));
        return transactionMapper.toResponse(transaction);
    }

    /**
     * Создать новую транзакцию
     */
    @Transactional
    public TransactionResponseDto create(TransactionRequestDto request) {
        // 1. Проверяем, существует ли продавец, и достаем его из БД
        // Если его нет, метод сам выбросит наше ResourceNotFoundException (404)
        SellerEntity seller = sellerService.getSellerOrThrow(request.sellerId());

        // 2. Превращаем DTO в Entity, передавая найденного продавца
        // MapStruct сам аккуратно свяжет их вместе
        TransactionEntity newTransaction = transactionMapper.toEntity(request, seller);

        // 3. Сохраняем готовую транзакцию в базу данных
        TransactionEntity savedTransaction = transactionRepository.save(newTransaction);

        // 4. Возвращаем клиенту красивый ответ (с вложенной информацией о продавце)
        return transactionMapper.toResponse(savedTransaction);
    }
}