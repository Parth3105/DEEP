package in.ac.daiict.deep.service;

import in.ac.daiict.deep.dto.AllocationResultDto;
import in.ac.daiict.deep.entity.AllocationResult;

import java.util.List;

public interface AllocationResultService {
    void insertAll(List<AllocationResult> allocationResultList);
    List<AllocationResultDto> fetchAllocationResult(String sid, String program);
    void deleteAll();
}
