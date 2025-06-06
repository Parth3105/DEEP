package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.constant.DBConstants;
import in.ac.daiict.deep.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student,String>{
    @Override
    @Modifying
    @Query(value = "DELETE FROM "+ DBConstants.STUDENT_TABLE,nativeQuery = true)
    void deleteAll();
    long countBySemester(int semester);
    Optional<Student> findById(String sid);
    List<Student> findBySemester(int semester);
}
