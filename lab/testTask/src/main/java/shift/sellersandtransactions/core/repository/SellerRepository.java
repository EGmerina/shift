package shift.sellersandtransactions.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shift.sellersandtransactions.core.entity.SellerEntity;


import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SellerRepository extends JpaRepository<SellerEntity, Long> {

    @Query("SELECT s FROM SellerEntity s LEFT JOIN s.transactions t " +
            "GROUP BY s " +
            "HAVING COALESCE(SUM(t.amount), 0) < :amount")
    List<SellerEntity> findSellersWithTotalTransactionsAmountLessThan(@Param("amount") BigDecimal amount);

}