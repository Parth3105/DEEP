package in.ac.daiict.deep.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class DashboardController {
    @GetMapping("/admin-dashboard")
    public void showDashboard(Model model){
    }
}
