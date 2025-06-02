package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.entity.CourseOffering;
import in.ac.daiict.deep.entity.compositekeys.CourseOfferingPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseOfferingRepo extends JpaRepository<CourseOffering,CourseOfferingPK> {
}
