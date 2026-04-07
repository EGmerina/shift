package shift.sellersandtransactions.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UploadedFileRepository extends JpaRepository<UploadedFileEntity, Long> {
    List<UploadedFileEntity> findByStatus(String status);
}
