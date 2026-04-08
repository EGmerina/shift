package shift.sellersandtransactions.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ProcessingErrorMapper.class)
public interface DetailedFileStatisticMapper {
    @Mapping(source = "validRows", target = "insertedLinesCount")
    @Mapping(source = "processedRows", target = "updatedLinesCount")
    @Mapping(source = "errors", target = "errors")
    DetailedFileStatisticDto map(UploadedFileEntity uploadedFileEntity);
}
