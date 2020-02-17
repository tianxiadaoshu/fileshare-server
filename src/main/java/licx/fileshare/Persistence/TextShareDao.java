package licx.fileshare.Persistence;

import licx.fileshare.Domain.TextShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TextShareDao extends JpaRepository<TextShare, Integer>, JpaSpecificationExecutor<TextShare> {

    List<TextShare> findAllByTimestampAfterAndTimestampBeforeOrderByIdDesc(Timestamp after, Timestamp before);
    void deleteById(Integer id);
    boolean existsById(Integer id);
    TextShare getById(Integer id);

    @Modifying
    @Transactional
    @Query("update TextShare t set t.shareText = ?2, t.timestamp = ?3, t.textPreview = ?4 where t.id = ?1")
    void modifyTextByID(Integer id, String shareText, Timestamp timestamp, String textPreview);
}
