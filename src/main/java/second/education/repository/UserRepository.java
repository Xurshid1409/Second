package second.education.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @EntityGraph(attributePaths = "role")
    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query( value = "select u from User u join EnrolleeInfo ei on u.id = ei.user.id " +
            "where u.phoneNumber=?1 or ei.pinfl=?2")
    Optional<User> findByPhoneNumberOrPinfl(String phoneNumber, String pinfl);


    @Query("select u from User u where u.phoneNumber = ?1")
    List<User> findAllByPhoneNumber(String phoneNumber);

}
