package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.entity.AllocationReport;
import in.ac.daiict.deep.repository.AllocationReportRepo;
import in.ac.daiict.deep.service.AllocationReportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AllocationReportServiceImpl implements AllocationReportService {
    private AllocationReportRepo allocationReportRepo;

    @Override
    public void insertReport(AllocationReport allocationReport) {
        deleteReport(allocationReport.getName());
        allocationReportRepo.save(allocationReport);
    }

    @Override
    public void deleteReport(String fileName) {
        allocationReportRepo.deleteById(fileName);
    }
}
