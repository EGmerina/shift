package shift.sellersandtransactions.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileIdResponseMapper {
    @Mapping(source = "id", target = "fileId")
    FileIdResponseDto map(UploadedFileEntity entity);
}
