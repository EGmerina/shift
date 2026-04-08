package shift.sellersandtransactions.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import shift.sellersandtransactions.api.dto.TransactionRequestDto;
import shift.sellersandtransactions.api.dto.TransactionResponseDto;
import shift.sellersandtransactions.core.entity.SellerEntity;
import shift.sellersandtransactions.core.entity.TransactionEntity;

// Параметр uses говорит MapStruct: "Если тебе нужно замаппить Seller в SellerResponse, используй этот класс"
@Mapper(componentModel = "spring", uses = {SellerMapper.class})
public interface TransactionMapper {

    /**
     * Так как аргументов два, мы явно указываем, откуда брать данные:
     * source = "request.amount" означает "возьми amount из параметра request".
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transactionDate", ignore = true)
    @Mapping(target = "amount", source = "request.amount")
    @Mapping(target = "paymentType", source = "request.paymentType")
    @Mapping(target = "seller", source = "seller") // Берем переданный объект Seller целиком
    TransactionEntity toEntity(TransactionRequestDto request, SellerEntity seller);

    TransactionResponseDto toResponse(TransactionEntity entity);
}