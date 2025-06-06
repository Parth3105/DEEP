package in.ac.daiict.deep.repository;

import in.ac.daiict.deep.entity.SlotPref;
import in.ac.daiict.deep.entity.compositekeys.SlotPrefPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlotPrefRepo extends JpaRepository<SlotPref, SlotPrefPK> {
    List<SlotPref> findBySidOrderByPrefAsc(String sid);
}
