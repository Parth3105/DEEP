package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.entity.InstituteReq;
import in.ac.daiict.deep.entity.compositekeys.InstituteReqPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstituteReqRepo extends JpaRepository<InstituteReq, InstituteReqPK> {
}
