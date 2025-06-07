package in.ac.daiict.deep.controller.admin;

import in.ac.daiict.deep.constant.endpoints.AdminEndpoint;
import in.ac.daiict.deep.constant.response.ResponseMessage;
import in.ac.daiict.deep.constant.response.ResponseStatus;
import in.ac.daiict.deep.constant.template.AdminTemplate;
import in.ac.daiict.deep.service.StudentService;
import in.ac.daiict.deep.utility.Response;
import in.ac.daiict.deep.utility.allocation.AllocationSystem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AllocationSystemController {
    private AllocationSystem allocationSystem;
    private StudentService studentService;
    Map<Integer,Long> allocatedStudentsData;
    Map<Integer,Long> unallocatedStudentsData;
    Map<Integer,Response> allocationStatus;

    public AllocationSystemController(AllocationSystem allocationSystem, StudentService studentService) {
        this.allocationSystem = allocationSystem;
        this.studentService = studentService;
        allocatedStudentsData=new HashMap<>();
        unallocatedStudentsData=new HashMap<>();
        allocationStatus=new HashMap<>();
    }

    @GetMapping(AdminEndpoint.RUN_ALLOCATION)
    public String renderRunAllocationPage(Model model){
        model.addAttribute("allocationResponse",allocationStatus);
        model.addAttribute("allocatedCount",allocatedStudentsData);
        model.addAttribute("unallocatedCount",unallocatedStudentsData);
        return AdminTemplate.RUN_ALLOCATION_PAGE;
    }

    @PostMapping(AdminEndpoint.EXECUTE_ALLOCATION)
    public String initiateAllocation(@PathVariable("semester") int semester){
        long[] unmetReqCnt=new long[1];
        Response allocationResponse=allocationSystem.initiateAllocation(semester,unmetReqCnt);
        allocationStatus.put(semester,allocationResponse);

        // Send the successfully allocated count.
        long totalStudents=studentService.countAllStudents();
        long allocatedCount=ResponseStatus.OK==allocationResponse.getStatus()?totalStudents-unmetReqCnt[0]:0;

        allocatedStudentsData.put(semester,allocatedCount);
        unallocatedStudentsData.put(semester,unmetReqCnt[0]);
        return "redirect:"+AdminEndpoint.RUN_ALLOCATION;
    }
}
