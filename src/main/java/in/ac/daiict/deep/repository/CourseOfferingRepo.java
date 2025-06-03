package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.entity.CourseOffering;
import in.ac.daiict.deep.entity.Database;
import in.ac.daiict.deep.entity.compositekeys.CourseOfferingPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CourseOfferingRepo extends JpaRepository<CourseOffering,CourseOfferingPK> {
    @Override
    @Modifying
    @Query(value = "DELETE FROM "+ Database.COURSE_OFFERING_TABLE,nativeQuery = true)
    void deleteAll();
}
