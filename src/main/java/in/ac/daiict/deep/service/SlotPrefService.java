package in.ac.daiict.deep.service;

import in.ac.daiict.deep.entity.SlotPref;

import java.util.List;

public interface SlotPrefService {
    List<SlotPref> fetchAllSlotSortedByPref();
}
