package second.education.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import second.education.domain.Document;

import javax.print.Doc;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    List<Document> findAllByDiplomaId(Integer diploma_id);

    @Query("select d from Document as d where d.diploma.enrolleeInfo.id=?")
    List<Document> getAllDocumentByEnrollId(Integer enrollId);
}
