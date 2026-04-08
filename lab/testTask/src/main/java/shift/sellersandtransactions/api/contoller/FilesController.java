package shift.sellersandtransactions.api.contoller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shift.sellersandtransactions.api.FileStatus;
import shift.sellersandtransactions.core.service.FileService;

import java.util.List;

@RestController
@RequestMapping(Paths.FILES_PREFIX)
public class FilesController {

    private final FileService fileService;

    public FilesController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(Paths.STATISTICS)
    public ResponseEntity<List<FileResponseDto>> getFilesInfo(@RequestParam String status) {
        List<FileResponseDto> fileStatistics = fileService.getFilesInfo(FileStatus.valueOf(status));
        return ResponseEntity.ok(fileStatistics);
    }

    @GetMapping(Paths.ID_STATISTICS)
    public ResponseEntity<DetailedFileStatisticDto> getDetailedFileInfo(@PathVariable String fileId) {
        DetailedFileStatisticDto filesStatistics = fileService.getDetailedFileInfo(fileId);
        return ResponseEntity.ok(filesStatistics);
    }

    @PostMapping
    public ResponseEntity<FileIdResponseDto> uploadFile(@RequestParam("file")MultipartFile file){
        FileIdResponseDto fileId = fileService.uploadFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(fileId);
    }

    @PostMapping(Paths.ID_PROCESSING)
    public void processFile(@PathVariable String fileId){
        fileService.processFile(fileId);
    }
}
