package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.entity.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadRepo extends JpaRepository<Upload,String> {
}
