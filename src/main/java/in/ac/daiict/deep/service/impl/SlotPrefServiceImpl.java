package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.entity.SlotPref;
import in.ac.daiict.deep.repository.SlotPrefRepo;
import in.ac.daiict.deep.service.SlotPrefService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SlotPrefServiceImpl implements SlotPrefService {

    private SlotPrefRepo slotPrefRepo;

    @Override
    public List<SlotPref> fetchAllSlotSortedByPref() {
        return slotPrefRepo.findAll(Sort.by("sid","pref").ascending());
    }
}
