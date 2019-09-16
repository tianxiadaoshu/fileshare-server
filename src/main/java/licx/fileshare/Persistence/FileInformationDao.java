package licx.fileshare.Persistence;

import licx.fileshare.Domain.FileInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FileInformationDao
        extends JpaRepository<FileInformation,String>, JpaSpecificationExecutor<FileInformation> {

    FileInformation findByAbsolutelyUrl(String absolutelyUrl);

    List<FileInformation> findByName(String name);

    @Modifying
    @Transactional
    void removeByAbsolutelyUrl(String absolutelyUrl);
}
