package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.entity.CoursePref;
import in.ac.daiict.deep.repository.CoursePrefRepo;
import in.ac.daiict.deep.service.CoursePrefService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CoursePrefServiceImpl implements CoursePrefService {

    private CoursePrefRepo coursePrefRepo;

    @Override
    public List<CoursePref> fetchAllSlotSortedByPref() {
        return coursePrefRepo.findAll(Sort.by("sid","pref").ascending());
    }
}
