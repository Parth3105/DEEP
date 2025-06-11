package in.ac.daiict.deep.util.status;

import in.ac.daiict.deep.constant.status.AllocationStatusEnum;
import lombok.Getter;

public class AllocationStatus {
    @Getter
    private String statusName;
    private AllocationStatusEnum statusValue;

    public AllocationStatus(AllocationStatusEnum statusValue) {
        this.statusName="allocation_status";
        this.statusValue = statusValue;
    }

    public String getStatusValue() {
        return statusValue.toString();
    }
}
