package in.ac.daiict.deep.service;

import in.ac.daiict.deep.entity.AllocationReport;

public interface AllocationReportService {
    public void insertReport(AllocationReport allocationReport);
    public void deleteReport(String fileName);
}
