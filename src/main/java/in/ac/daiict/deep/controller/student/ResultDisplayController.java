package in.ac.daiict.deep.controller.student;

import in.ac.daiict.deep.constant.response.ResponseMessage;
import in.ac.daiict.deep.constant.response.ResponseStatus;
import in.ac.daiict.deep.constant.endpoints.StudentEndpoint;
import in.ac.daiict.deep.constant.template.StudentTemplate;
import in.ac.daiict.deep.dto.*;
import in.ac.daiict.deep.entity.Student;
import in.ac.daiict.deep.service.*;
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
    private AllocationResultService allocationResultService;

    @GetMapping(StudentEndpoint.PREFERENCE_SUMMARY)
    public String loadPreferenceSummary(@CookieValue(name = "student_id", required = false, defaultValue = "202201174") String studentId, Model model){
        // Fetch the semester & program of the student.
        Student student = studentService.fetchStudentData(studentId);
        if (student == null) {
            // not found student.
            model.addAttribute("renderResponse", new Response(ResponseStatus.NOT_FOUND, ResponseMessage.USER_NOT_FOUND));
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

    @GetMapping(StudentEndpoint.ALLOCATION_RESULT)
    public String loadAllocationResult(@CookieValue(name = "student_id", required = false, defaultValue = "202201174") String studentId, Model model){
        StudentDto studentDto=studentService.fetchStudentDto(studentId);
        if (studentDto == null) {
            // not found student.
            model.addAttribute("renderResponse", new Response(ResponseStatus.NOT_FOUND, ResponseMessage.USER_NOT_FOUND));
            return "redirect:"+StudentEndpoint.HOME_PAGE;
        }

        List<AllocationResultDto> allocationResultDtoList=allocationResultService.fetchAllocationResult(studentId,studentDto.getProgram());
        if(allocationResultDtoList==null){
            // not found any results.
            model.addAttribute("renderResponse", new Response(ResponseStatus.NOT_FOUND, ResponseMessage.RESULTS_NOT_FOUND));
            return "redirect:"+StudentEndpoint.HOME_PAGE;
        }

        // send allocation result details
        model.addAttribute("semester", studentDto.getSemester());
        model.addAttribute("program", studentDto.getProgram());
        model.addAttribute("allocationResult",allocationResultDtoList);
        return StudentTemplate.ALLOCATION_RESULT_PAGE;
    }
}
