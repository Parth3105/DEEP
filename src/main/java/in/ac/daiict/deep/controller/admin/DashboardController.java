package in.ac.daiict.deep.controller.admin;

import in.ac.daiict.deep.constant.endpoints.AdminEndpoint;
import in.ac.daiict.deep.constant.template.AdminTemplate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class DashboardController {

    /**
     * only for current testing purpose.
     * @return admin dashboard
     */
    @GetMapping("/")
    public String test(){
        return "redirect:"+AdminEndpoint.DASHBOARD;
    }
    @GetMapping(AdminEndpoint.DASHBOARD)
    public String showDashboard(){
        return AdminTemplate.DASHBOARD_PAGE;
    }
}
