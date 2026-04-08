package shift.sellersandtransactions.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import shift.sellersandtransactions.api.FileStatus;

@Mapper(componentModel = "spring", uses = FileStatisticMapper.class)
public interface FileResponseMapper {
    @Mapping(source = "id", target = "fileId")
    @Mapping(source = "status", target = "status", qualifiedByName = "mapStatus")
    @Mapping(source = "entity", target = "statistic")
    FileResponseDto map(UploadedFileEntity entity);

    @Named("mapStatus")
    default FileStatus mapStatus(String status) {
        return status != null ? FileStatus.valueOf(status) : null;
    }
}
