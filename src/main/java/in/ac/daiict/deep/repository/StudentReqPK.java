package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.entity.StudentReq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentReqPK extends JpaRepository<StudentReq, StudentReqPK> {
}
