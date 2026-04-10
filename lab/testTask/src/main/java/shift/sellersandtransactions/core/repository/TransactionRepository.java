package shift.sellersandtransactions.core.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shift.sellersandtransactions.core.entity.SellerEntity;
import shift.sellersandtransactions.core.entity.TransactionEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findAllBySellerId(Long sellerId);

    @Query("SELECT t.seller FROM TransactionEntity t " +
            "WHERE t.transactionDate >= :startDate AND t.transactionDate <= :endDate " +
            "GROUP BY t.seller " +
            "ORDER BY SUM(t.amount) DESC")
    List<SellerEntity> findBestSellerForPeriod(@Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate,
                                               Pageable pageable);

    String findBestDay(Long sellerId, LocalDateTime start, LocalDateTime end, Pageable limitOne);

    String findBestMonth(Long sellerId, LocalDateTime start, LocalDateTime end, Pageable limitOne);

    String findBestYear(Long sellerId, LocalDateTime start, LocalDateTime end, Pageable limitOne);
}