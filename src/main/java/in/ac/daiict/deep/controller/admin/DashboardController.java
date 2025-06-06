package in.ac.daiict.deep.controller.admin;

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
        return "redirect:/admin-dashboard";
    }
    @GetMapping("/admin-dashboard")
    public String showDashboard(){
        return "admin/dashboard";
    }
}
