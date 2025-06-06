package in.ac.daiict.deep.controller.admin;

import in.ac.daiict.deep.constant.endpoints.AdminEndpoint;
import in.ac.daiict.deep.utility.allocation.AllocationSystem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class AllocationSystemController {
    private AllocationSystem allocationSystem;

    @PostMapping(AdminEndpoint.EXECUTE_ALLOCATION)
    public String initiateAllocation(){
        allocationSystem.initiateAllocation(6);
        return "redirect:"+AdminEndpoint.DASHBOARD;
    }
}
