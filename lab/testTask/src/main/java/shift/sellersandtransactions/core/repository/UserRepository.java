package shift.sellersandtransactions.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("""
    SELECT u FROM UserEntity u
    WHERE (:name IS NULL OR u.firstName = :name)
      AND (:lastName IS NULL OR u.lastName = :lastName)
      AND (:email IS NULL OR u.email = :email)
      AND (:phone IS NULL OR u.phone = :phone)
""")
    List<UserEntity> findByFilters(
            @Param("name") String name,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("phone") String phone,
            Pageable pageable
    );

}
