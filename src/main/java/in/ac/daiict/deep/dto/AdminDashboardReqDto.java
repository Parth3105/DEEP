package in.ac.daiict.deep.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AdminDashboardReqDto {
    private int semester;
    private long totalStudents;
    private long submittedPrefCnt;
    private boolean allocationStatus;
}
