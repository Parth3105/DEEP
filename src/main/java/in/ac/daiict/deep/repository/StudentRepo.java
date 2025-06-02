package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepo extends JpaRepository<Student,String>{
}
