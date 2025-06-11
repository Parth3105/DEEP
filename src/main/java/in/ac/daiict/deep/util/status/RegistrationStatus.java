package in.ac.daiict.deep.util.status;

import in.ac.daiict.deep.constant.status.RegistrationStatusEnum;
import lombok.Getter;
import lombok.Setter;

public class RegistrationStatus {
    @Getter
    private static String statusName;
    private RegistrationStatusEnum statusValue;

    public RegistrationStatus() {
        statusName = "registration_status";
    }

    public RegistrationStatus(RegistrationStatusEnum statusValue) {
        statusName="registration_status";
        this.statusValue = statusValue;
    }

    public String getStatusValue() {
        return statusValue.toString();
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = RegistrationStatusEnum.valueOf(statusValue);
    }

}
