package in.ac.daiict.deep.dto;

import in.ac.daiict.deep.constant.status.RegistrationStatusEnum;
import in.ac.daiict.deep.constant.status.UpdateInstanceStatusEnum;
import in.ac.daiict.deep.util.status.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SystemStatusDto {
    private RegistrationStatus registrationStatus=null;
    private UpdateInstanceStatus updateInstanceStatus=null;
    private RegistrationCloseDate registrationCloseDate=null;
    private AllocationStatus allocationStatus=null;
    private ResultStatus resultStatus=null;

    public SystemStatusDto(RegistrationStatusEnum registrationStatusValue, LocalDate closingDate, UpdateInstanceStatusEnum updateInstanceStatusValue){
        this.registrationStatus=new RegistrationStatus(registrationStatusValue);
        this.registrationCloseDate=new RegistrationCloseDate(closingDate);
        this.updateInstanceStatus=new UpdateInstanceStatus(updateInstanceStatusValue);
    }

    public SystemStatusDto(LocalDate closingDate){
        this.registrationCloseDate=new RegistrationCloseDate(closingDate);
    }

    public SystemStatusDto(RegistrationStatusEnum registrationStatusValue){
        this.registrationStatus=new RegistrationStatus(registrationStatusValue);
    }
}