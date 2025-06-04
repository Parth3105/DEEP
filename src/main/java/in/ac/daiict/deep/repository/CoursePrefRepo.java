package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.entity.CoursePref;
import in.ac.daiict.deep.entity.compositekeys.CoursePrefPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursePrefRepo extends JpaRepository<CoursePref, CoursePrefPK> {
}
