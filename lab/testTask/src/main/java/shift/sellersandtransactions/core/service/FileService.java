package shift.sellersandtransactions.core.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shift.sellersandtransactions.api.FileStatus;
import shift.sellersandtransactions.api.dto.DetailedFileStatisticDto;
import shift.sellersandtransactions.api.dto.FileIdResponseDto;
import shift.sellersandtransactions.api.dto.FileResponseDto;
import shift.sellersandtransactions.api.mapper.FileResponseMapper;
import shift.sellersandtransactions.core.repository.UploadedFileRepository;

import java.util.List;

@Service
public class FileService {

    private final UploadedFileRepository uploadedFileRepository;
    private final FileResponseMapper fileResponseMapper;

    public FileService(UploadedFileRepository uploadedFileRepository,
                       FileResponseMapper fileResponseMapper) {
        this.uploadedFileRepository = uploadedFileRepository;
        this.fileResponseMapper = fileResponseMapper;
    }

    public List<FileResponseDto> getFilesInfo(FileStatus status) {
        List<UploadedFileEntity> files;

        if (status != null) {
            files = uploadedFileRepository.findByStatus(status.name());
        } else {
            files = uploadedFileRepository.findAll();
        }

        return files.stream()
                .map(fileResponseMapper::map)
                .toList();
    }

    public DetailedFileStatisticDto getDetailedFileInfo(String fileId) {
        return null;
    }

    public FileIdResponseDto uploadFile(MultipartFile file) {
        return null;
    }

    public void processFile(String fileId) {

    }
}
