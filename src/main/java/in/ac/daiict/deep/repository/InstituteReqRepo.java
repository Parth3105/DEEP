package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.entity.Database;
import in.ac.daiict.deep.entity.InstituteReq;
import in.ac.daiict.deep.entity.compositekeys.InstituteReqPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface InstituteReqRepo extends JpaRepository<InstituteReq, InstituteReqPK> {
    @Override
    @Modifying
    @Query(value = "DELETE FROM "+ Database.INST_REQ_TABLE,nativeQuery = true)
    void deleteAll();
}
