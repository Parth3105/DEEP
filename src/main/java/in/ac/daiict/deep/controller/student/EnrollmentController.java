package in.ac.daiict.deep.controller.student;

import in.ac.daiict.deep.constant.response.ResponseMessage;
import in.ac.daiict.deep.constant.response.ResponseStatus;
import in.ac.daiict.deep.constant.endpoints.StudentEndpoint;
import in.ac.daiict.deep.constant.status.RegistrationStatusEnum;
import in.ac.daiict.deep.constant.template.StudentTemplate;
import in.ac.daiict.deep.entity.CoursePref;
import in.ac.daiict.deep.entity.SlotPref;
import in.ac.daiict.deep.entity.Student;
import in.ac.daiict.deep.entity.StudentReq;
import in.ac.daiict.deep.security.auth.CustomUserDetails;
import in.ac.daiict.deep.service.*;
import in.ac.daiict.deep.dto.ResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Controller
@AllArgsConstructor
public class EnrollmentController {
    private StudentService studentService;
    private CourseService courseService;
    private InstituteReqService instituteReqService;
    private StudentReqService studentReqService;
    private CoursePrefService coursePrefService;
    private SlotPrefService slotPrefService;
    private SystemStatusService systemStatusService;

    @GetMapping(StudentEndpoint.ENROLL)
    public String renderEnrollmentForm(String studentId, Model model, RedirectAttributes redirectAttributes) {
        if(!systemStatusService.fetchRegistrationStatus().equals(RegistrationStatusEnum.open.toString())) return "redirect:"+StudentEndpoint.HOME_PAGE;
        else if(studentReqService.isExist(studentId)) return "redirect:"+StudentEndpoint.PREFERENCE_SUMMARY;

        CustomUserDetails userDetails= (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Send the semester & program of students and institute requirements.
        Student student = studentService.fetchStudentData(userDetails.getUsername());
        if (student == null) {
            // not found student.
            model.addAttribute("renderResponse", new ResponseDto(ResponseStatus.NOT_FOUND, ResponseMessage.USER_NOT_FOUND));
            return "redirect:"+StudentEndpoint.HOME_PAGE;
        }
        model.addAttribute("semester", student.getSemester());
        model.addAttribute("program", student.getProgram());

        CompletableFuture<Void> fetchingInstitutePref =CompletableFuture.supplyAsync(() -> instituteReqService.findInstituteReq(student.getProgram(), student.getSemester()))
                .thenAccept(instituteReqDtoList -> model.addAttribute("instituteRequirements", instituteReqDtoList));

        // Send the available courses to Student with required information
        CompletableFuture<Void> fetchingAvailableCourses =CompletableFuture.supplyAsync(() -> courseService.fetchAvailableCourses(student.getProgram(), student.getSemester()))
                .thenAccept(availableCourseDtoList -> model.addAttribute("availableCourses", availableCourseDtoList));

        try {
            CompletableFuture.allOf(fetchingInstitutePref, fetchingAvailableCourses).join();
        }catch (CompletionException completionException){
            redirectAttributes.addFlashAttribute("internalServerError",new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.INTERNAL_SERVER_ERROR));
            return "redirect:"+StudentEndpoint.HOME_PAGE;
        }
        return StudentTemplate.ENROLLMENT_FORM_PAGE;
    }

    @PostMapping(StudentEndpoint.SUBMIT_PREFERENCE)
    public String loadSubmittedPreferences(@RequestParam String studentRequirements, @RequestParam String coursePreferences, @RequestParam String slotPreferences, RedirectAttributes redirectAttributes){
        if(!systemStatusService.fetchRegistrationStatus().equals(RegistrationStatusEnum.open.toString())){
            redirectAttributes.addFlashAttribute("preferenceSubmissionResponse", new ResponseDto(ResponseStatus.FORBIDDEN,ResponseMessage.LATE_SUBMISSION));
            return "redirect:"+StudentEndpoint.HOME_PAGE;
        }
        CustomUserDetails userDetails= (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String studentId=userDetails.getUsername();

        // record Student Requirements.
        CompletableFuture<Void> recordingStudentReqs =CompletableFuture.runAsync(() -> {
            if(studentRequirements!=null) {
                List<StudentReq> studentReqs = new ArrayList<>();
                String[] categoryCountMap = studentRequirements.split("#");
                for (String keyValue : categoryCountMap)
                    studentReqs.add(new StudentReq(studentId, keyValue.split(":", 2)[0], Integer.parseInt(keyValue.split(":", 2)[1])));

                studentReqService.insertAll(studentReqs);

                    /*
                    // debug
                    for(StudentReq studentReq:studentReqs) System.out.println(studentReq.getCategory()+": "+studentReq.getCourse_cnt());
                    System.out.println("\n");
                     */
            }
        });

        // record Course Preferences.
        CompletableFuture<Void> recordingCoursePrefs =CompletableFuture.runAsync(() -> {
            if(coursePreferences!=null) {
                List<CoursePref> coursePrefs = new ArrayList<>();
                String[] slotCourseListMap = coursePreferences.split("#");
                for (String slotCourseList : slotCourseListMap) {
                    String slot = slotCourseList.split(":", 2)[0];
                    String[] courseList = slotCourseList.split(":", 2)[1].split("\\$");
                    for(int j=0;j<courseList.length;j++) coursePrefs.add(new CoursePref(studentId,slot,j+1,courseList[j]));
                    System.out.println("\n");
                }

                coursePrefService.insertAll(coursePrefs);

                    /*
                    // debug
                    for(CoursePref coursePref:coursePrefs) System.out.println(coursePref.getSlot()+": "+coursePref.getPref()+": "+coursePref.getCid());
                     */
            }
        });

        // record Slot Preferences
        CompletableFuture<Void> recordingSlotPref =CompletableFuture.runAsync(() -> {
            if(slotPreferences!=null){
                List<SlotPref> slotPrefs = new ArrayList<>();
                String[] slotList=slotPreferences.split("\\$");
                for(int j=0;j<slotList.length;j++) slotPrefs.add(new SlotPref(studentId,j+1,slotList[j]));

                slotPrefService.insertAll(slotPrefs);

                    /*
                    // debug
                    for(SlotPref slotPref: slotPrefs) System.out.println(slotPref.getPref()+": "+slotPref.getSlot());
                    System.out.println("\n");
                     */
            }
        });

        try {
            CompletableFuture.allOf(recordingStudentReqs, recordingCoursePrefs, recordingSlotPref).join();
        }catch (CompletionException completionException){
            redirectAttributes.addFlashAttribute("internalServerError", new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.INTERNAL_SERVER_ERROR));
            return "redirect:"+StudentEndpoint.HOME_PAGE;
        }
        return "redirect:"+ StudentEndpoint.PREFERENCE_SUMMARY;
    }
}
