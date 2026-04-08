package shift.sellersandtransactions.core.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sellers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Переопределяем команду DELETE: вместо удаления строки, ставим флаг is_deleted = true
@SQLDelete(sql = "UPDATE sellers SET is_deleted = true WHERE id=?")
// При любом SELECT запросе (например, findAll) будут возвращаться только неудаленные записи
@SQLRestriction("is_deleted = false")
public class SellerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоинкремент
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "contact_info", nullable = false)
    private String contactInfo;

    @Column(name = "registration_date", nullable = false, updatable = false)
    private LocalDateTime registrationDate;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false; // Флаг для историчности данных

    // Связь: Один продавец может иметь много транзакций
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<TransactionEntity> transactions = new ArrayList<>();

    // Этот метод автоматически выполнится перед первым сохранением в БД
    @PrePersist
    protected void onCreate() {
        this.registrationDate = LocalDateTime.now();
    }
}