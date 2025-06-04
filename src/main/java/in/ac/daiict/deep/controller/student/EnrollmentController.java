package in.ac.daiict.deep.controller.student;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class EnrollmentController {
    @GetMapping("/enroll")
    public String renderEnrollmentForm(Model model){
        return "registration";
    }
}
