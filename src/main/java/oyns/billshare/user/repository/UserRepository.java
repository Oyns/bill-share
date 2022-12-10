package oyns.billshare.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import oyns.billshare.user.model.User;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(value = "SELECT COUNT(?) FROM users_items WHERE user_id=?", nativeQuery = true)
    Integer findAmountOfItemsForUser(@Param(value = "item") UUID itemId,
                                     @Param(value = "user") UUID userId);
}
