package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.entity.SlotPref;
import in.ac.daiict.deep.entity.compositekeys.SlotPrefPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SlotPrefRepo extends JpaRepository<SlotPref, SlotPrefPK> {
    List<SlotPref> findBySidOrderByPrefAsc(String sid);

    @Modifying
    @Query("SELECT new SlotPref(slotPref.sid,slotPref.pref,slotPref.slot) FROM SlotPref slotPref JOIN Student student ON slotPref.sid=student.sid WHERE student.semester=:semester ORDER BY slotPref.sid, slotPref.pref ASC")
    List<SlotPref> findBySemesterOrderBySidAscPrefAsc(@Param("semester") int semester);
}
