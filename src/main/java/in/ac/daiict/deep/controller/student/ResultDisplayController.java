package in.ac.daiict.deep.controller.student;

import in.ac.daiict.deep.constant.response.ResponseMessage;
import in.ac.daiict.deep.constant.response.ResponseStatus;
import in.ac.daiict.deep.constant.endpoints.StudentEndpoint;
import in.ac.daiict.deep.constant.template.StudentTemplate;
import in.ac.daiict.deep.dto.CoursePrefDto;
import in.ac.daiict.deep.dto.SlotPrefDto;
import in.ac.daiict.deep.dto.StudentReqDto;
import in.ac.daiict.deep.entity.Student;
import in.ac.daiict.deep.service.CoursePrefService;
import in.ac.daiict.deep.service.SlotPrefService;
import in.ac.daiict.deep.service.StudentReqService;
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
public class ResultDisplayController {
    private StudentService studentService;
    private StudentReqService studentReqService;
    private CoursePrefService coursePrefService;
    private SlotPrefService slotPrefService;

    @GetMapping(StudentEndpoint.PREFERENCE_SUMMARY)
    public String displayPreferenceSummary(@CookieValue(name = "student_id", required = false, defaultValue = "202201174") String studentId, Model model){
        // Fetch the semester & program of the student.
        Student student = studentService.findStudentData(studentId);
        if (student == null) {
            // not found student.
            model.addAttribute("renderResponse", new Response(ResponseStatus.NOT_FOUND, ResponseMessage.NOT_FOUND));
            return "redirect:"+StudentEndpoint.HOME_PAGE;
        }

        // Fetch the student requirements of the student.
        List<StudentReqDto> studentReqDtoList=studentReqService.findStudentRequirements(studentId);

        // Fetch the course preferences slot-wise.
        List<CoursePrefDto> coursePrefDtoList=coursePrefService.fetchStudentCoursePref(studentId);

        // Fetch the slot preferences.
        List<SlotPrefDto> slotPrefDtoList=slotPrefService.fetchStudentSlotPref(studentId);

        if(studentReqDtoList==null || coursePrefDtoList==null || slotPrefDtoList==null){
            // not found preferences.
            model.addAttribute("renderResponse", new Response(ResponseStatus.NOT_FOUND, ResponseMessage.NOT_REGISTERED));
            return "redirect:"+StudentEndpoint.HOME_PAGE;
        }

        // Send the preference summary details.
        model.addAttribute("semester", student.getSemester());
        model.addAttribute("program", student.getProgram());
        model.addAttribute("studentRequirements",studentReqDtoList);
        model.addAttribute("coursePreferences",coursePrefDtoList);
        model.addAttribute("slotPreferences",slotPrefDtoList);

        return StudentTemplate.PREFERENCE_SUMMARY_PAGE;
    }
}
