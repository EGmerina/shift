package shift.sellersandtransactions.unit;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shift.sellersandtransactions.api.dto.TransactionRequestDto;
import shift.sellersandtransactions.api.dto.TransactionResponseDto;
import shift.sellersandtransactions.api.mapper.TransactionMapper;
import shift.sellersandtransactions.core.entity.SellerEntity;
import shift.sellersandtransactions.core.entity.TransactionEntity;
import shift.sellersandtransactions.core.repository.TransactionRepository;
import shift.sellersandtransactions.core.service.SellerService;
import shift.sellersandtransactions.core.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static shift.sellersandtransactions.core.entity.PaymentType.CARD;

@ExtendWith(MockitoExtension.class)
class TransactionUnitTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private SellerService sellerService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("findAll: должен возвращать список DTO транзакций")
    void findAll_ShouldReturnListOfTransactionResponseDtos() {

        TransactionEntity entity = createDummyTransaction();
        TransactionResponseDto responseDto = createDummyResponseDto();

        when(transactionRepository.findAll()).thenReturn(List.of(entity));
        when(transactionMapper.toResponse(entity)).thenReturn(responseDto);


        List<TransactionResponseDto> result = transactionService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDto.id(), result.getFirst().id());
        verify(transactionRepository, times(1)).findAll();
        verify(transactionMapper, times(1)).toResponse(entity);
    }

    @Test
    @DisplayName("findByTransactionId: должен возвращать транзакцию, если она существует")
    void findByTransactionId_ShouldReturnTransaction_WhenExists() {
        Long transactionId = 100L;
        TransactionEntity entity = createDummyTransaction();
        TransactionResponseDto responseDto = createDummyResponseDto();

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(entity));
        when(transactionMapper.toResponse(entity)).thenReturn(responseDto);

        TransactionResponseDto result = transactionService.findByTransactionId(transactionId);

        assertNotNull(result);
        assertEquals(transactionId, result.id());
        verify(transactionRepository).findById(transactionId);
    }

    @Test
    @DisplayName("findByTransactionId: должен выбрасывать исключение, если транзакция не найдена")
    void findByTransactionId_ShouldThrowException_WhenNotFound() {
        Long transactionId = 999L;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> transactionService.findByTransactionId(transactionId));

        assertTrue(exception.getMessage().contains(transactionId.toString()));
        verify(transactionRepository).findById(transactionId);
        verifyNoInteractions(transactionMapper);
    }

    @Test
    @DisplayName("create: должен получать продавца, сохранять транзакцию и возвращать DTO")
    void create_ShouldFetchSellerSaveTransactionAndReturnDto() {
        Long sellerId = 1L;
        TransactionRequestDto requestDto = new TransactionRequestDto(sellerId, BigDecimal.valueOf(1500.50), CARD);

        SellerEntity sellerEntity = createDummySeller();
        TransactionEntity newEntity = new TransactionEntity();
        TransactionEntity savedEntity = createDummyTransaction();
        TransactionResponseDto responseDto = createDummyResponseDto();

        when(sellerService.getSellerOrThrow(sellerId)).thenReturn(sellerEntity);
        when(transactionMapper.toEntity(requestDto, sellerEntity)).thenReturn(newEntity);
        when(transactionRepository.save(newEntity)).thenReturn(savedEntity);
        when(transactionMapper.toResponse(savedEntity)).thenReturn(responseDto);

        TransactionResponseDto result = transactionService.create(requestDto);
        assertNotNull(result);
        assertEquals(100L, result.id());
        verify(sellerService).getSellerOrThrow(sellerId);
        verify(transactionRepository).save(newEntity);
    }

    @Test
    @DisplayName("getTransactionsBySellerId: должен возвращать список транзакций конкретного продавца")
    void getTransactionsBySellerId_ShouldReturnListOfDtosForSeller() {
        Long sellerId = 1L;
        TransactionEntity entity = createDummyTransaction();
        TransactionResponseDto responseDto = createDummyResponseDto();

        when(transactionRepository.findAllBySellerId(sellerId)).thenReturn(List.of(entity));
        when(transactionMapper.toResponse(entity)).thenReturn(responseDto);

        List<TransactionResponseDto> result = transactionService.getTransactionsBySellerId(sellerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(transactionRepository).findAllBySellerId(sellerId);
        verify(transactionMapper).toResponse(entity);
    }

    private SellerEntity createDummySeller() {
        SellerEntity seller = new SellerEntity();
        seller.setId(1L);
        seller.setName("Тестовый Продавец");
        seller.setContactInfo("test@test.com");
        return seller;
    }

    private TransactionEntity createDummyTransaction() {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setId(100L);
        transaction.setSeller(createDummySeller());
        transaction.setAmount(BigDecimal.valueOf(1500.50));
        transaction.setPaymentType(CARD);
        transaction.setTransactionDate(LocalDateTime.now());
        return transaction;
    }

    private TransactionResponseDto createDummyResponseDto() {
        return new TransactionResponseDto(
                100L,
                null,
                BigDecimal.valueOf(1500.50),
                CARD,
                LocalDateTime.now()
        );
    }
}