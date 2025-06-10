package in.ac.daiict.deep.util.status;


import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RegistrationCloseDate {
    private String statusName;
    private LocalDate closeDate;

    public RegistrationCloseDate(LocalDate closeDate) {
        this.statusName="registration_close_date";
        this.closeDate = closeDate;
    }

    public String getStringCloseDate() {
        return closeDate.toString();
    }
}
