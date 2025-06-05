package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.entity.AllocationResult;
import in.ac.daiict.deep.repository.AllocationResultRepo;
import in.ac.daiict.deep.service.AllocationResultService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AllocationResultServiceImpl implements AllocationResultService {
    private AllocationResultRepo allocationResultRepo;

    @Override
    public void insertAll(List<AllocationResult> allocationResultList) {
        allocationResultRepo.saveAll(allocationResultList);
    }
}
