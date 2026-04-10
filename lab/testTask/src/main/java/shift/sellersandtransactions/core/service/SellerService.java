package shift.sellersandtransactions.core.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shift.sellersandtransactions.api.dto.SellerRequestDto;
import shift.sellersandtransactions.api.dto.SellerResponseDto;
import shift.sellersandtransactions.api.mapper.SellerMapper;
import shift.sellersandtransactions.core.entity.SellerEntity;
import shift.sellersandtransactions.core.repository.SellerRepository;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerService {

    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;

    public List<SellerResponseDto> findAll() {
        return sellerRepository.findAll().stream()
                .map(sellerMapper::toResponse)
                .toList();
    }

    public SellerResponseDto findById(Long id) {
        SellerEntity seller = getSellerOrThrow(id);
        return sellerMapper.toResponse(seller);
    }

    @Transactional
    public SellerResponseDto create(SellerRequestDto request) {
        SellerEntity newSeller = sellerMapper.toEntity(request);
        SellerEntity savedSeller = sellerRepository.save(newSeller);
        return sellerMapper.toResponse(savedSeller);
    }

    @Transactional
    public SellerResponseDto update(Long id, SellerRequestDto request) {
        SellerEntity existingSeller = getSellerOrThrow(id);

        existingSeller.setName(request.name());
        existingSeller.setContactInfo(request.contactInfo());

        SellerEntity updatedSeller = sellerRepository.save(existingSeller);
        return sellerMapper.toResponse(updatedSeller);
    }


    @Transactional
    public void delete(Long id) {
        SellerEntity seller = getSellerOrThrow(id);
        sellerRepository.delete(seller);
    }

    public SellerEntity getSellerOrThrow(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Продавец с id " + id + " не найден"));
    }
}