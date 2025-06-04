package in.ac.daiict.deep.controller.student;

import in.ac.daiict.deep.constant.ResponseConstants;
import in.ac.daiict.deep.dto.InstituteReqDto;
import in.ac.daiict.deep.dto.StudentDto;
import in.ac.daiict.deep.service.InstituteReqService;
import in.ac.daiict.deep.service.StudentService;
import in.ac.daiict.deep.utility.Response;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class EnrollmentController {
    private StudentService studentService;
    private InstituteReqService instituteReqService;

    @GetMapping("/enroll")
    public String renderEnrollmentForm(@CookieValue(name = "student_id", required = false, defaultValue = "202203029") String studentId,Model model){
        // send the semester of the student & program & instituteReq object
        StudentDto studentDto=studentService.findStudentData(studentId);
        if(studentDto==null){
            // not found student.
            model.addAttribute("renderResponse",new Response(ResponseConstants.NOT_FOUND,"User not found!"));
            return "home-page";
        }
        model.addAttribute("semester",studentDto.getSemester());
        model.addAttribute("program",studentDto.getProgram());
        List<InstituteReqDto> instituteReqDto=instituteReqService.findInstituteReq(studentDto.getProgram(),studentDto.getSemester());
        model.addAttribute("instituteRequirements",instituteReqDto);
        return "student/registration";
    }
}
