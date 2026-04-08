package shift.sellersandtransactions.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileStatisticMapper {

    @Mapping(source = "validRows", target = "insertedLinesCount")
    @Mapping(source = "invalidRows", target = "errorProcessedLinesCount")
    @Mapping(source = "processedRows", target = "updatedLinesCount")
    FileStatisticDto map(UploadedFileEntity entity);
}
