package in.ac.daiict.deep.controller.admin;

import in.ac.daiict.deep.constant.endpoints.AdminEndpoint;
import in.ac.daiict.deep.constant.response.ResponseStatus;
import in.ac.daiict.deep.constant.status.RegistrationStatusEnum;
import in.ac.daiict.deep.constant.template.AdminTemplate;
import in.ac.daiict.deep.entity.AllocationStatus;
import in.ac.daiict.deep.service.AllocationStatusService;
import in.ac.daiict.deep.service.StudentService;
import in.ac.daiict.deep.dto.ResponseDto;
import in.ac.daiict.deep.service.SystemStatusService;
import in.ac.daiict.deep.util.allocation.AllocationSystem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.CompletableFuture;

@Controller
@AllArgsConstructor
public class AllocationSystemController {
    private AllocationSystem allocationSystem;
    private StudentService studentService;
    private SystemStatusService systemStatusService;
    private AllocationStatusService allocationStatusService;

    @GetMapping(AdminEndpoint.RUN_ALLOCATION)
    public String renderRunAllocationPage(Model model){
        CompletableFuture<Void> statusFetchFuture=CompletableFuture.supplyAsync(() -> systemStatusService.fetchRegistrationStatus())
                .thenAccept(registrationStatus -> model.addAttribute("registrationStatus",registrationStatus));
        CompletableFuture<Void> allStatusFetchFuture=CompletableFuture.supplyAsync(() -> allocationStatusService.fetchAll())
                .thenAccept(allocationStatusDtoList -> model.addAttribute("allocationStatus",allocationStatusDtoList));

        CompletableFuture.allOf(statusFetchFuture,allStatusFetchFuture).join();
        return AdminTemplate.RUN_ALLOCATION_PAGE;
    }

    @PostMapping(AdminEndpoint.EXECUTE_ALLOCATION)
    public String initiateAllocation(@PathVariable("semester") int semester, @RequestParam("registrationStatus") String registrationStatus, RedirectAttributes redirectAttributes){
        CompletableFuture.runAsync(() -> {
            if (registrationStatus.equals(RegistrationStatusEnum.open.toString()))
                systemStatusService.updateOnClosingRegistration();
        });

        long[] unmetReqCnt=new long[1];
        ResponseDto allocationResponse=allocationSystem.initiateAllocation(semester,unmetReqCnt);

        // Send the successfully allocated count.
        long totalStudents=studentService.countAllStudents();
        long allocatedCount=ResponseStatus.OK==allocationResponse.getStatus()?totalStudents-unmetReqCnt[0]:0;

        allocationStatusService.insertAllocationStatus(new AllocationStatus(semester,allocationResponse.getStatus(),(int)allocatedCount,(int)unmetReqCnt[0]));

        redirectAttributes.addFlashAttribute("semester",semester);
        return "redirect:"+AdminEndpoint.RUN_ALLOCATION;
    }
}
