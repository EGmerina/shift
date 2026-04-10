package shift.sellersandtransactions.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import shift.sellersandtransactions.api.dto.TransactionRequestDto;
import shift.sellersandtransactions.api.dto.TransactionResponseDto;
import shift.sellersandtransactions.core.entity.SellerEntity;
import shift.sellersandtransactions.core.entity.TransactionEntity;

@Mapper(componentModel = "spring", uses = {SellerMapper.class})
public interface TransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transactionDate", ignore = true)
    @Mapping(target = "amount", source = "request.amount")
    @Mapping(target = "paymentType", source = "request.paymentType")

    TransactionEntity toEntity(TransactionRequestDto request, SellerEntity seller);

    TransactionResponseDto toResponse(TransactionEntity entity);
}