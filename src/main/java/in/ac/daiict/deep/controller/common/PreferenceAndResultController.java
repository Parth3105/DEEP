package in.ac.daiict.deep.controller.common;

import in.ac.daiict.deep.constant.endpoints.AdminEndpoint;
import in.ac.daiict.deep.constant.response.ResponseMessage;
import in.ac.daiict.deep.constant.response.ResponseStatus;
import in.ac.daiict.deep.constant.endpoints.StudentEndpoint;
import in.ac.daiict.deep.constant.template.AdminTemplate;
import in.ac.daiict.deep.constant.template.StudentTemplate;
import in.ac.daiict.deep.dto.*;
import in.ac.daiict.deep.entity.CoursePref;
import in.ac.daiict.deep.entity.SlotPref;
import in.ac.daiict.deep.service.*;
import in.ac.daiict.deep.utility.Response;
import in.ac.daiict.deep.utility.dataloader.DataLoader;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@AllArgsConstructor
public class PreferenceAndResultController {
    private StudentService studentService;
    private StudentReqService studentReqService;
    private CoursePrefService coursePrefService;
    private SlotPrefService slotPrefService;
    private AllocationResultService allocationResultService;
    private DataLoader dataLoader;

    @GetMapping(StudentEndpoint.PREFERENCE_SUMMARY)
    public String loadMyPreferenceSummary(@CookieValue(name = "student_id", required = false, defaultValue = "202201406") String studentId, Model model){
        return fetchPreferenceSummary(studentId,model,'S');
    }

    @GetMapping(StudentEndpoint.ALLOCATION_RESULT)
    public String loadMyAllocationResult(@CookieValue(name = "student_id", required = false, defaultValue = "202201174") String studentId, Model model){
        return fetchAllocationResult(studentId,model,'S');
    }

    @GetMapping(AdminEndpoint.STUDENT_PREFERENCE_FILTER)
    public String loadSubmittedPreferences(@PathVariable("sid") String studentId, Model model){
        return fetchPreferenceSummary(studentId,model,'A');
    }
    @GetMapping(AdminEndpoint.DOWNLOAD_STUDENT_PREFERENCES)
    public void downloadStudentPreferences(HttpServletResponse httpServletResponse, Model model){
        List<CoursePref> coursePrefList=coursePrefService.fetchAllCoursePrefSortedBySlotAndPref();
        List<SlotPref> slotPrefList=slotPrefService.fetchAllSlotSortedByPref();

        if(coursePrefList.isEmpty() || slotPrefList.isEmpty()){
            model.addAttribute("downloadResponse",new Response(ResponseStatus.NOT_FOUND,ResponseMessage.STUDENT_PREFERENCES_NOT_FOUND));
        }

        ByteArrayOutputStream byteArrayOutputStream=dataLoader.createStudentPrefSheet(coursePrefList,slotPrefList);
        String downloadFilename="Student Preferences.xlsx";

        httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFilename + "\"");
        try {
            httpServletResponse.getOutputStream().write(byteArrayOutputStream.toByteArray());
            httpServletResponse.getOutputStream().flush();
        } catch (IOException e) {
            model.addAttribute("downloadResponse",new Response(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.DOWNLOADING_ERROR));
        }
    }
    @GetMapping(AdminEndpoint.ALLOCATION_RESULTS_FILTER)
    public String loadAllocationResult(@PathVariable("sid") String studentId, Model model){
        return fetchAllocationResult(studentId,model,'A');
    }


    private String fetchPreferenceSummary(String studentId, Model model, char requester){
        // Fetch the semester & program of the student.
        StudentDto student = studentService.fetchStudentDto(studentId);
        if (student == null) {
            // not found student.
            if(requester=='S') model.addAttribute("renderResponse", new Response(ResponseStatus.NOT_FOUND, ResponseMessage.USER_NOT_FOUND));
            else model.addAttribute("renderResponse", new Response(ResponseStatus.NOT_FOUND, ResponseMessage.STUDENT_NOT_FOUND));
            if(requester=='S') return "redirect:"+StudentEndpoint.HOME_PAGE;
            return "redirect:"+AdminEndpoint.STUDENT_PREFERENCE;
        }

        // Fetch the student requirements of the student.
        List<StudentReqDto> studentReqDtoList=studentReqService.findStudentRequirements(studentId);

        // Fetch the course preferences slot-wise.
        List<CoursePrefDto> coursePrefDtoList=coursePrefService.fetchStudentCoursePref(studentId);

        // Fetch the slot preferences.
        List<SlotPrefDto> slotPrefDtoList=slotPrefService.fetchStudentSlotPref(studentId);

        if(studentReqDtoList==null || coursePrefDtoList==null || slotPrefDtoList==null){
            // not found preferences.
            if(requester=='S') model.addAttribute("renderResponse", new Response(ResponseStatus.NOT_FOUND, ResponseMessage.USER_NOT_REGISTERED));
            else model.addAttribute("renderResponse", new Response(ResponseStatus.NOT_FOUND, ResponseMessage.STUDENT_NOT_REGISTERED));
            if(requester=='S') return "redirect:"+StudentEndpoint.HOME_PAGE;
            return "redirect:"+AdminEndpoint.STUDENT_PREFERENCE;
        }

        // Send the preference summary details.
        model.addAttribute("studentInfo", student);
        model.addAttribute("studentRequirements",studentReqDtoList);
        model.addAttribute("coursePreferences",coursePrefDtoList);
        model.addAttribute("slotPreferences",slotPrefDtoList);

        if(requester=='S') return StudentTemplate.PREFERENCE_SUMMARY_PAGE;
        else return AdminTemplate.STUDENTS_PREFERENCES_PAGE;
    }
    private String fetchAllocationResult(String studentId, Model model, char requester){
        StudentDto studentDto=studentService.fetchStudentDto(studentId);
        if (studentDto == null) {
            // not found student.
            if(requester=='S') model.addAttribute("renderResponse", new Response(ResponseStatus.NOT_FOUND, ResponseMessage.USER_NOT_FOUND));
            else model.addAttribute("renderResponse", new Response(ResponseStatus.NOT_FOUND, ResponseMessage.STUDENT_NOT_FOUND));
            if(requester=='S') return "redirect:"+StudentEndpoint.HOME_PAGE;
            return "redirect:"+AdminEndpoint.STUDENT_PREFERENCE;
        }

        List<AllocationResultDto> allocationResultDtoList=allocationResultService.fetchAllocationResult(studentId,studentDto.getProgram());
        if(allocationResultDtoList==null){
            // not found any results.
            model.addAttribute("renderResponse", new Response(ResponseStatus.NOT_FOUND, ResponseMessage.RESULTS_NOT_FOUND));
            return "redirect:"+StudentEndpoint.HOME_PAGE;
        }

        // send allocation result details
        model.addAttribute("studentInfo", studentDto);
        model.addAttribute("allocationResult",allocationResultDtoList);
        if(requester=='S') return StudentTemplate.ALLOCATION_RESULT_PAGE;
        else return AdminTemplate.ALLOCATION_RESULTS_PAGE;
    }
}
