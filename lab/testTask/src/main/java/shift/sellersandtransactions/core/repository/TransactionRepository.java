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

    /**
     * Spring Data JPA сам "догадается", что этот метод должен искать
     * все транзакции, где seller_id равен переданному значению.
     * Нам даже не нужно писать SQL-запрос!
     */
    List<TransactionEntity> findAllBySellerId(Long sellerId);

    /**
     * Аналитика: Найти самого продуктивного продавца за указанный период (от startDate до endDate).
     * Сортируем продавцов по убыванию суммы их транзакций.
     * Чтобы получить только одного (лучшего), мы передадим в этот метод объект Pageable с лимитом 1.
     */
    @Query("SELECT t.seller FROM Transaction t " +
            "WHERE t.transactionDate >= :startDate AND t.transactionDate <= :endDate " +
            "GROUP BY t.seller " +
            "ORDER BY SUM(t.amount) DESC")
    List<SellerEntity> findBestSellerForPeriod(@Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate,
                                               Pageable pageable);

}