package second.education.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @EntityGraph(attributePaths = "role")
    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query(nativeQuery = true, value = "select * from users u join enrollee_info ei on u.id = ei.user_id " +
            "where u.phone_number=?1 or ei.pinfl=?2")
    Optional<User> findByPhoneNumberOrPinfl(String phoneNumber, String pinfl);
}
