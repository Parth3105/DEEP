package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.entity.Course;
import in.ac.daiict.deep.constant.DBConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepo extends JpaRepository<Course,String> {
    @Override
    @Modifying
    @Query(value = "DELETE FROM "+ DBConstants.COURSE_TABLE,nativeQuery = true)
    void deleteAll();
    boolean existsById(String cid);
}
