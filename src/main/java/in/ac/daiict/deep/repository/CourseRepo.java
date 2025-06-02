package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepo extends JpaRepository<Course,String> {
}
