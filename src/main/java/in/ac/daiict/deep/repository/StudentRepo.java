package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.entity.Database;
import in.ac.daiict.deep.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepo extends JpaRepository<Student,String>{
    @Override
    @Modifying
    @Query(value = "DELETE FROM "+ Database.STUDENT_TABLE,nativeQuery = true)
    void deleteAll();
}
