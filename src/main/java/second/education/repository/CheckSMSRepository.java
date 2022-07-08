package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import second.education.domain.CheckSMSEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckSMSRepository extends JpaRepository<CheckSMSEntity, Integer> {

    List<CheckSMSEntity> findByPhoneNumber(String phoneNumber);

    Optional<CheckSMSEntity> findByPhoneNumberAndPinfl(String phoneNumber, String pinfl);

    List<CheckSMSEntity> findAllByPinfl(String pinfl);
}
