package shift.sellersandtransactions.unit;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shift.sellersandtransactions.api.dto.SellerRequestDto;
import shift.sellersandtransactions.api.dto.SellerResponseDto;
import shift.sellersandtransactions.api.mapper.SellerMapper;
import shift.sellersandtransactions.core.entity.SellerEntity;
import shift.sellersandtransactions.core.repository.SellerRepository;
import shift.sellersandtransactions.core.service.SellerService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerUnitTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private SellerMapper sellerMapper;

    @InjectMocks
    private SellerService sellerService;

    @Test
    @DisplayName("findAll: должен возвращать список DTO продавцов")
    void findAll_ShouldReturnListOfSellerResponseDtos() {

        SellerEntity entity = createDummyEntity();
        SellerResponseDto responseDto = createDummyResponseDto();

        when(sellerRepository.findAll()).thenReturn(List.of(entity));
        when(sellerMapper.toResponse(entity)).thenReturn(responseDto);

        List<SellerResponseDto> result = sellerService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDto.id(), result.getFirst().id());
        verify(sellerRepository, times(1)).findAll();
        verify(sellerMapper, times(1)).toResponse(entity);
    }

    @Test
    @DisplayName("findById: должен возвращать продавца, если он существует")
    void findById_ShouldReturnSeller_WhenExists() {

        Long sellerId = 1L;
        SellerEntity entity = createDummyEntity();
        SellerResponseDto responseDto = createDummyResponseDto();

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(entity));
        when(sellerMapper.toResponse(entity)).thenReturn(responseDto);

        SellerResponseDto result = sellerService.findById(sellerId);
        assertNotNull(result);
        assertEquals(sellerId, result.id());
        verify(sellerRepository).findById(sellerId);
    }

    @Test
    @DisplayName("findById: должен выбрасывать исключение, если продавец не найден")
    void findById_ShouldThrowException_WhenNotFound() {

        Long sellerId = 999L;
        when(sellerRepository.findById(sellerId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> sellerService.findById(sellerId));

        assertTrue(exception.getMessage().contains(sellerId.toString()));
        verify(sellerRepository).findById(sellerId);
        verifyNoInteractions(sellerMapper);
    }

    @Test
    @DisplayName("create: должен сохранять сущность и возвращать DTO")
    void create_ShouldSaveAndReturnDto() {

        SellerRequestDto requestDto = new SellerRequestDto("Новый Продавец", "new@test.com");
        SellerEntity newEntity = new SellerEntity();
        newEntity.setName(requestDto.name());
        newEntity.setContactInfo(requestDto.contactInfo());

        SellerEntity savedEntity = createDummyEntity(); // Сущность с присвоенным ID
        SellerResponseDto responseDto = createDummyResponseDto();

        when(sellerMapper.toEntity(requestDto)).thenReturn(newEntity);
        when(sellerRepository.save(newEntity)).thenReturn(savedEntity);
        when(sellerMapper.toResponse(savedEntity)).thenReturn(responseDto);

        SellerResponseDto result = sellerService.create(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(sellerRepository).save(newEntity);
    }

    @Test
    @DisplayName("update: должен обновлять данные и сохранять сущность")
    void update_ShouldUpdateAndSave_WhenExists() {

        Long sellerId = 1L;
        SellerRequestDto requestDto = new SellerRequestDto("Обновленное Имя", "updated@test.com");

        SellerEntity existingEntity = createDummyEntity();

        SellerEntity updatedEntity = new SellerEntity();
        updatedEntity.setId(sellerId);
        updatedEntity.setName(requestDto.name());
        updatedEntity.setContactInfo(requestDto.contactInfo());

        SellerResponseDto responseDto = new SellerResponseDto(sellerId, requestDto.name(), requestDto.contactInfo(), LocalDateTime.now());

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(existingEntity));
        when(sellerRepository.save(existingEntity)).thenReturn(updatedEntity);
        when(sellerMapper.toResponse(updatedEntity)).thenReturn(responseDto);

        SellerResponseDto result = sellerService.update(sellerId, requestDto);

        assertNotNull(result);
        assertEquals(requestDto.name(), existingEntity.getName()); // Проверяем, что сеттеры вызвались
        assertEquals(requestDto.contactInfo(), existingEntity.getContactInfo());
        verify(sellerRepository).save(existingEntity);
    }

    @Test
    @DisplayName("delete: должен вызывать удаление, если сущность найдена")
    void delete_ShouldCallRepositoryDelete_WhenExists() {
        Long sellerId = 1L;
        SellerEntity entity = createDummyEntity();
        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(entity));

        sellerService.delete(sellerId);

        verify(sellerRepository).delete(entity);
    }

    private SellerEntity createDummyEntity() {
        SellerEntity entity = new SellerEntity();
        entity.setId(1L);
        entity.setName("Тестовый Продавец");
        entity.setContactInfo("test@test.com");
        entity.setRegistrationDate(LocalDateTime.now());
        return entity;
    }

    private SellerResponseDto createDummyResponseDto() {
        return new SellerResponseDto(
                1L,
                "Тестовый Продавец",
                "test@test.com",
                LocalDateTime.now()
        );
    }
}