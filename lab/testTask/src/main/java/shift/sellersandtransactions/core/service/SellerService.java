package shift.sellersandtransactions.core.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shift.sellersandtransactions.api.dto.SellerRequestDto;
import shift.sellersandtransactions.api.dto.SellerResponseDto;
import shift.sellersandtransactions.api.dto.TransactionResponseDto;
import shift.sellersandtransactions.api.mapper.SellerMapper;
import shift.sellersandtransactions.api.mapper.TransactionMapper;
import shift.sellersandtransactions.core.entity.SellerEntity;
import shift.sellersandtransactions.core.repository.SellerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerService {

    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;
    private final TransactionMapper transactionMapper;

    /**
     * Получить список всех продавцов
     */
    public List<SellerResponseDto> findAll() {
        return sellerRepository.findAll().stream()
                .map(sellerMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получить продавца по ID
     */
    public SellerResponseDto findById(Long id) {
        SellerEntity seller = getSellerOrThrow(id);
        return sellerMapper.toResponse(seller);
    }

    /**
     * Создать нового продавца
     * @Transactional переопределяет readOnly, так как мы меняем данные в БД
     */
    @Transactional
    public SellerResponseDto create(SellerRequestDto request) {
        SellerEntity newSeller = sellerMapper.toEntity(request);
        SellerEntity savedSeller = sellerRepository.save(newSeller);
        return sellerMapper.toResponse(savedSeller);
    }

    /**
     * Обновить данные продавца
     */
    @Transactional
    public SellerResponseDto update(Long id, SellerRequestDto request) {
        SellerEntity existingSeller = getSellerOrThrow(id);

        // Обновляем только разрешенные поля
        existingSeller.setName(request.name());
        existingSeller.setContactInfo(request.contactInfo());

        // Сохраняем обновленную сущность (Hibernate сам поймет, что нужно сделать UPDATE)
        SellerEntity updatedSeller = sellerRepository.save(existingSeller);
        return sellerMapper.toResponse(updatedSeller);
    }

    /**
     * Удалить продавца (Soft Delete)
     */
    @Transactional
    public void delete(Long id) {
        // Проверяем, существует ли он вообще
        SellerEntity seller = getSellerOrThrow(id);

        // Вызываем удаление. Благодаря нашей аннотации @SQLDelete в Entity,
        // физического удаления не произойдет, только проставится is_deleted = true
        sellerRepository.delete(seller);
    }

    /**
     * Получить все транзакции конкретного продавца
     */
    public List<TransactionResponseDto> getTransactionsBySellerId(Long id) {
        SellerEntity seller = getSellerOrThrow(id);

        // Так как транзакции имеют ленивую загрузку (LAZY), Hibernate запросит их
        // из базы только в этот момент (во время вызова getTransactions())
        return seller.getTransactions().stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    // --- Вспомогательные методы (Helper Methods) ---

    /**
     * Выносит дублирующийся код поиска продавца с проверкой на существование.
     */
    public SellerEntity getSellerOrThrow(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Продавец с id " + id + " не найден"));
    }
}