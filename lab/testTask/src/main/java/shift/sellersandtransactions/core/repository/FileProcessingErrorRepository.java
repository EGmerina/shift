package shift.sellersandtransactions.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shift.sellersandtransactions.core.model.FileProcessingErrorEntity;

public interface FileProcessingErrorRepository extends JpaRepository<FileProcessingErrorEntity, Long> {
}
