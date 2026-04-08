package shift.sellersandtransactions.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import shift.sellersandtransactions.api.dto.SellerRequestDto;
import shift.sellersandtransactions.api.dto.SellerResponseDto;
import shift.sellersandtransactions.core.entity.SellerEntity;

@Mapper(componentModel = "spring")
public interface SellerMapper {

    /**
     * MapStruct сам найдет поля name и contactInfo.
     * Мы явно указываем игнорировать поля, которых нет в DTO,
     * чтобы при сборке не сыпались предупреждения (warnings).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    SellerEntity toEntity(SellerRequestDto request);

    SellerResponseDto toResponse(SellerEntity entity);
}