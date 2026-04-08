package shift.sellersandtransactions.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import shift.sellersandtransactions.api.ProcessingErrorCode;
import shift.sellersandtransactions.core.entity.FileProcessingErrorEntity;

@Mapper(componentModel = "spring")
public interface ProcessingErrorMapper {
    @Mapping(source = "rowNumber", target = "lineNumber")
    @Mapping(source = "errorCode", target = "errorCode", qualifiedByName = "mapCode")
    @Mapping(source = "errorMessage", target = "errorMessage")
    ProcessingErrorDto map(FileProcessingErrorEntity entity);

    @Named("mapCode")
    default ProcessingErrorCode mapCode(String errorCode) {
        return errorCode != null ? ProcessingErrorCode.valueOf(errorCode) : null;
    }
}
