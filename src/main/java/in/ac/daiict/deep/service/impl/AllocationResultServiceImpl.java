package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.dto.AllocationResultDto;
import in.ac.daiict.deep.entity.AllocationResult;
import in.ac.daiict.deep.repository.AllocationResultRepo;
import in.ac.daiict.deep.service.AllocationResultService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AllocationResultServiceImpl implements AllocationResultService {
    private AllocationResultRepo allocationResultRepo;

    @Override
    public void insertAll(List<AllocationResult> allocationResultList) {
        deleteAll();
        allocationResultRepo.saveAll(allocationResultList);
    }

    @Override
    public List<AllocationResultDto> fetchAllocationResult(String sid, String program) {
        List<AllocationResultDto> allocationResultDtoList=allocationResultRepo.fetchAllocationResultBySid(sid, program);
        if(allocationResultDtoList==null || allocationResultDtoList.isEmpty()) return null;
        return allocationResultDtoList;
    }

    @Override
    public List<AllocationResult> fetchCourseWiseAllocation(String cid) {
        return allocationResultRepo.findByCidOrderBySid(cid);
    }

    @Override
    public void deleteAll() {
        allocationResultRepo.deleteAll();
    }
}
