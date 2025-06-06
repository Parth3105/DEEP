package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.dto.CoursePrefDto;
import in.ac.daiict.deep.entity.CoursePref;
import in.ac.daiict.deep.entity.compositekeys.CoursePrefPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoursePrefRepo extends JpaRepository<CoursePref, CoursePrefPK> {

    @Query("SELECT new in.ac.daiict.deep.dto.CoursePrefDto(cpref.slot,cpref.pref,course.name,cpref.cid) FROM Course course JOIN CoursePref cpref ON course.cid=cpref.cid WHERE cpref.sid=:sid ORDER BY cpref.slot, cpref.pref ASC")
    List<CoursePrefDto> findStudentCoursePref(@Param("sid") String sid);
}
