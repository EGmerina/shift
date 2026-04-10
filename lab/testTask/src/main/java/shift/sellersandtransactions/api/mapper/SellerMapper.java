package shift.sellersandtransactions.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import shift.sellersandtransactions.api.dto.SellerRequestDto;
import shift.sellersandtransactions.api.dto.SellerResponseDto;
import shift.sellersandtransactions.core.entity.SellerEntity;

@Mapper(componentModel = "spring")
public interface SellerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    SellerEntity toEntity(SellerRequestDto request);

    SellerResponseDto toResponse(SellerEntity entity);
}