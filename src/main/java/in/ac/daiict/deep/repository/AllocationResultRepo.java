package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.entity.AllocationResult;
import in.ac.daiict.deep.entity.compositekeys.AllocationResultPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllocationResultRepo extends JpaRepository<AllocationResult, AllocationResultPK> {
}
