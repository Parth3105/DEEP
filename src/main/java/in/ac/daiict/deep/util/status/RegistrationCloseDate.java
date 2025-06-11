package in.ac.daiict.deep.util.status;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


public class RegistrationCloseDate {
    @Getter
    private static String statusName;
    @Getter
    private LocalDate closeDate;

    public RegistrationCloseDate() {
        statusName = "registration_close_date";
    }

    public RegistrationCloseDate(LocalDate closeDate) {
        statusName="registration_close_date";
        this.closeDate = closeDate;
    }

    public String getStringCloseDate() {
        return closeDate.toString();
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = LocalDate.parse(closeDate);
    }
}
