package shift.sellersandtransactions.core.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Связь: Много транзакций могут принадлежать одному продавцу
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false) // Имя колонки в базе данных
    private SellerEntity seller;

    @Column(nullable = false, precision = 10, scale = 2) // Ограничиваем формат числа
    private BigDecimal amount;

    @Enumerated(EnumType.STRING) // Сохраняем в базе как текст ('CASH', 'CARD'...), а не как цифру
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @Column(name = "transaction_date", nullable = false, updatable = false)
    private LocalDateTime transactionDate;

    // Автоматическое проставление времени при создании транзакции
    @PrePersist
    protected void onCreate() {
        this.transactionDate = LocalDateTime.now();
    }
}