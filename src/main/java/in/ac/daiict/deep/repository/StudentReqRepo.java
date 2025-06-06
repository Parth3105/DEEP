package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.entity.StudentReq;
import in.ac.daiict.deep.entity.compositekeys.StudentReqPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentReqRepo extends JpaRepository<StudentReq, StudentReqPK> {
    List<StudentReq> findBySid(String sid);
}
