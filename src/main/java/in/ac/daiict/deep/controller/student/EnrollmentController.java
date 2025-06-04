package in.ac.daiict.deep.controller.student;

import in.ac.daiict.deep.constant.ResponseConstants;
import in.ac.daiict.deep.dto.InstituteReqDto;
import in.ac.daiict.deep.dto.StudentDto;
import in.ac.daiict.deep.dto.StudentReqDto;
import in.ac.daiict.deep.service.InstituteReqService;
import in.ac.daiict.deep.service.StudentService;
import in.ac.daiict.deep.utility.Response;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class EnrollmentController {
    private StudentService studentService;
    private InstituteReqService instituteReqService;

    @GetMapping("/enroll")
    public String renderEnrollmentForm(@CookieValue(name = "student_id", required = false, defaultValue = "202201174") String studentId,Model model){
        // Send the semester & program of students and institute requirements.
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

        // Setup model to fetch student requirements.
        List<StudentReqDto> studentReqDtos=new ArrayList<>();
        for(int j=0;j<instituteReqDto.size();j++) studentReqDtos.add(new StudentReqDto(studentId));
        model.addAttribute("studentRequirements",studentReqDtos);

        return "registration";
    }
}
