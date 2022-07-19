package second.education.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.User;

import java.awt.print.Pageable;
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

    @Query("select u from User u where u.role.id = 3")
    Page<User> findAllByRole(Pageable pageable);

}
