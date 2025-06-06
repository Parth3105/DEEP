package in.ac.daiict.deep.service;

import in.ac.daiict.deep.entity.CoursePref;

import java.util.List;

public interface CoursePrefService {
    List<CoursePref> fetchAllSlotSortedByPref();
}
