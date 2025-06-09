package in.ac.daiict.deep.controller.student;

import in.ac.daiict.deep.constant.endpoints.StudentEndpoint;
import in.ac.daiict.deep.constant.template.StudentTemplate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class HomePageController {

    @GetMapping(StudentEndpoint.HOME_PAGE)
    public String renderStudentHomePage(){
        return StudentTemplate.HOME_PAGE;
    }
}
