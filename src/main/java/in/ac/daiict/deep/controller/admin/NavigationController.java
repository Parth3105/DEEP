package in.ac.daiict.deep.controller.admin;

import in.ac.daiict.deep.constant.endpoints.AdminEndpoint;
import in.ac.daiict.deep.constant.template.AdminTemplate;
import in.ac.daiict.deep.dto.AdminDashboardReqDto;
import in.ac.daiict.deep.dto.SystemStatusDto;
import in.ac.daiict.deep.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class NavigationController {
    private SystemStatusService systemStatusService;
    private StudentService studentService;
    private StudentReqService studentReqService;
    private AllocationStatusService allocationStatusService;

    @GetMapping(AdminEndpoint.DASHBOARD)
    public String renderDashboardPage(Model model){
        SystemStatusDto systemStatusDto=systemStatusService.fetchAllStatus();
        if(systemStatusDto.getRegistrationStatus()!=null) model.addAttribute("registrationStatus",systemStatusDto.getRegistrationStatus().getStatusValue());
        if(systemStatusDto.getRegistrationCloseDate()!=null) model.addAttribute("updateInstanceStatus",systemStatusDto.getUpdateInstanceStatus().getStatusValue());
        if(systemStatusDto.getResultStatus()!=null) model.addAttribute("resultStatus",systemStatusDto.getResultStatus().getStatusValue());
        if(systemStatusDto.getRegistrationCloseDate()!=null) model.addAttribute("registrationCloseDate",systemStatusDto.getRegistrationCloseDate().getCloseDate());

        List<AdminDashboardReqDto> adminDashboardReqDtoList=new ArrayList<>();
        for(int j=5;j<=8;j++){
            adminDashboardReqDtoList.add(new AdminDashboardReqDto(j,studentService.countBySemester(j),studentReqService.submittedPrefCntBySemester(j),allocationStatusService.checkIfExists(j)));
        }

        model.addAttribute("dashboardRequirement",adminDashboardReqDtoList);
        return AdminTemplate.DASHBOARD_PAGE;
    }

    @GetMapping(AdminEndpoint.DOWNLOAD_REPORTS)
    public String renderDownloadReportsPage(){
        return AdminTemplate.DOWNLOAD_REPORTS_PAGE;
    }

    @GetMapping(AdminEndpoint.STUDENT_PREFERENCE)
    public String renderStudentPreferencesPage(){
        return AdminTemplate.STUDENTS_PREFERENCES_PAGE;
    }

    @GetMapping(AdminEndpoint.ALLOCATION_RESULTS)
    public String renderAllocationResultsPage(){
        return AdminTemplate.ALLOCATION_RESULTS_PAGE;
    }
}
