package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.dto.SlotPrefDto;
import in.ac.daiict.deep.entity.SlotPref;
import in.ac.daiict.deep.repository.SlotPrefRepo;
import in.ac.daiict.deep.service.SlotPrefService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SlotPrefServiceImpl implements SlotPrefService {

    private SlotPrefRepo slotPrefRepo;
    private ModelMapper modelMapper;

    @Override
    public List<SlotPref> fetchAllSlotSortedByPref() {
        return slotPrefRepo.findAll(Sort.by("sid","pref").ascending());
    }

    @Override
    public List<SlotPrefDto> fetchStudentSlotPref(String sid) {
        List<SlotPref> slotPrefList=slotPrefRepo.findBySidOrderByPrefAsc(sid);
        if(slotPrefList==null || slotPrefList.isEmpty()) return null;
        return modelMapper.map(slotPrefList,new TypeToken<List<SlotPrefDto>>(){}.getType());
    }

    @Override
    public List<SlotPref> fetchSlotBySemesterSortedBySidAndPref(int semester) {
        return slotPrefRepo.findBySemesterOrderBySidAscPrefAsc(semester);
    }

    @Override
    public void insertAll(List<SlotPref> slotPrefList) {
        slotPrefRepo.saveAll(slotPrefList);
    }
}
