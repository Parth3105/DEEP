package in.ac.daiict.deep.util.status;

import in.ac.daiict.deep.constant.status.UpdateInstanceStatusEnum;
import lombok.Getter;


public class UpdateInstanceStatus {
    @Getter
    private String statusName;
    private UpdateInstanceStatusEnum statusValue;

    public UpdateInstanceStatus(UpdateInstanceStatusEnum statusValue) {
        this.statusName="update_instance_status";
        this.statusValue = statusValue;
    }

    public String getStatusValue() {
        return statusValue.toString();
    }
}

