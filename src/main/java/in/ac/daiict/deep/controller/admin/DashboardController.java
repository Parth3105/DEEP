package in.ac.daiict.deep.controller.admin;

import in.ac.daiict.deep.constant.endpoints.AdminEndpoint;
import in.ac.daiict.deep.constant.status.RegistrationStatusEnum;
import in.ac.daiict.deep.constant.status.UpdateInstanceStatusEnum;
import in.ac.daiict.deep.constant.template.AdminTemplate;
import in.ac.daiict.deep.dto.SystemStatusDto;
import in.ac.daiict.deep.service.SystemStatusService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@AllArgsConstructor
public class DashboardController {

    private SystemStatusService systemStatusService;

    @PostMapping(AdminEndpoint.OPEN_REGISTRATION)
    public String openRegistration(@RequestParam("close-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate closeDate){
        SystemStatusDto systemStatusDto=new SystemStatusDto(RegistrationStatusEnum.OPEN,closeDate, UpdateInstanceStatusEnum.NEVER);
        systemStatusService.updateOnOpeningRegistration(systemStatusDto);
        return AdminTemplate.DASHBOARD_PAGE;
    }

    @PostMapping(AdminEndpoint.EXTEND_REGISTRATION_PERIOD)
    public String extendRegistrationPeriod(@RequestParam("close-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate closeDate){
        SystemStatusDto systemStatusDto=new SystemStatusDto(closeDate);
        systemStatusService.updateOnExtendingRegistrationPeriod(systemStatusDto);
        return AdminTemplate.DASHBOARD_PAGE;
    }

    @PostMapping(AdminEndpoint.CLOSE_REGISTRATION)
    public String closeRegistration(){
        SystemStatusDto systemStatusDto=new SystemStatusDto(RegistrationStatusEnum.CLOSE);
        systemStatusService.updateOnClosingRegistration(systemStatusDto);
        return AdminTemplate.DASHBOARD_PAGE;
    }
}

/*
//debug
@GetMapping(AdminEndpoint.OPEN_REGISTRATION)
public String openRegistration(){
    SystemStatusDto systemStatusDto=new SystemStatusDto(RegistrationStatusEnum.OPEN,LocalDate.of(2025,6,11), UpdateInstanceStatusEnum.NEVER);
    systemStatusService.updateOnOpeningRegistration(systemStatusDto);
    return AdminTemplate.DASHBOARD_PAGE;
}

//debug
@GetMapping(AdminEndpoint.EXTEND_REGISTRATION_PERIOD)
public String extendRegistrationPeriod(){
    SystemStatusDto systemStatusDto=new SystemStatusDto(LocalDate.of(2025,6,9));
    systemStatusService.updateOnExtendingRegistrationPeriod(systemStatusDto);
    return AdminTemplate.DASHBOARD_PAGE;
}*/
