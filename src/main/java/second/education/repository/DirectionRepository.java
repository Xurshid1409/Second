package second.education.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.classificator.Direction;
import second.education.model.response.StatisDirectionResponse;
import second.education.model.response.StatisDirectionResponseByFutureInst;

import java.util.List;

@Repository
public interface DirectionRepository extends JpaRepository<Direction, Integer> {

    @Query(nativeQuery = true, value = "select * from direction d where d.future_institution_id=?1")
    List<Direction> findAllByFutureInstitutionId(Integer futureInstitution_id);

    @Query("Select d from Direction d where d.name LIKE  %?1% or d.futureInstitution.name LIKE %?1%")
    Page<Direction> findDirectionByNameLike(String name, Pageable pageable);

    @Query(nativeQuery = true, value = "select d.id as directionId, d.name directionName from direction d where d.future_institution_id=?1 ")
    List<StatisDirectionResponse> getAllDirectionByFutureInst(Integer futureInstId);

    @Query(nativeQuery = true, value = "select d.id as directionId, d.name as directionName, fi.name as futureInstName from direction d " +
            " join future_institution fi on fi.id = d.future_institution_id where fi.id=?1 ")
    List<StatisDirectionResponseByFutureInst> getAllDirectionByFutureInstitutions(Integer futureInstId);

}
