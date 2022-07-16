package second.education.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.classificator.Direction;
import second.education.model.response.GetStatisByDirection;

import java.util.List;

@Repository
public interface DirectionRepository extends JpaRepository<Direction, Integer> {

    @Query(nativeQuery = true, value = "select * from direction d where d.future_institution_id=?1")
    List<Direction> findAllByFutureInstitutionId(Integer futureInstitution_id);


    @Query("Select d from Direction d where d.name LIKE  %?1% or d.futureInstitution.name LIKE %?1%")
    Page<Direction> findDirectionByNameLike(String name, Pageable pageable);


    @Query(nativeQuery = true, value = "select d.id as directionId, d.name as directionName from second_education.public.direction d where d.future_institution_id = ?1")
    List<GetStatisByDirection> getAllByFutureInstitutionId(Integer futureInstitution_id);
}
