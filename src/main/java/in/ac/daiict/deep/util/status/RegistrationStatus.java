package in.ac.daiict.deep.util.status;

import in.ac.daiict.deep.constant.status.RegistrationStatusEnum;
import lombok.Getter;

public class RegistrationStatus {
    @Getter
    private String statusName;
    private RegistrationStatusEnum statusValue;

    public RegistrationStatus(RegistrationStatusEnum statusValue) {
        this.statusName="registration_status";
        this.statusValue = statusValue;
    }

    public String getStatusValue() {
        return statusValue.toString();
    }
}
