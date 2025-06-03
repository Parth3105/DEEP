package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.entity.Course;
import in.ac.daiict.deep.entity.Database;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepo extends JpaRepository<Course,String> {
    @Override
    @Modifying
    @Query(value = "DELETE FROM "+ Database.COURSE_TABLE,nativeQuery = true)
    void deleteAll();
}
