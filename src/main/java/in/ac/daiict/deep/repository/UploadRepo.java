package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.entity.Database;
import in.ac.daiict.deep.entity.Upload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UploadRepo extends JpaRepository<Upload,String> {
    @Override
    @Modifying
    @Query(value = "DELETE FROM "+ Database.UPLOAD_TABLE,nativeQuery = true)
    void deleteAll();
}
