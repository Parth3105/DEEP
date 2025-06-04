package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.entity.StudentReq;
import in.ac.daiict.deep.entity.compositekeys.StudentReqPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentReqRepo extends JpaRepository<StudentReq, StudentReqPK> {
}
