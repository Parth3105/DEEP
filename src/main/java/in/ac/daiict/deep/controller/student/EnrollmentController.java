package in.ac.daiict.deep.controller.student;

import in.ac.daiict.deep.constant.ResponseConstants;
import in.ac.daiict.deep.dto.AvailableCourseDto;
import in.ac.daiict.deep.dto.InstituteReqDto;
import in.ac.daiict.deep.dto.StudentDto;
import in.ac.daiict.deep.entity.CoursePref;
import in.ac.daiict.deep.entity.SlotPref;
import in.ac.daiict.deep.entity.StudentReq;
import in.ac.daiict.deep.service.CourseService;
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
    private CourseService courseService;
    private InstituteReqService instituteReqService;

    @GetMapping("/enroll")
    public String renderEnrollmentForm(@CookieValue(name = "student_id", required = false, defaultValue = "202201174") String studentId, Model model) {
        // Send the semester & program of students and institute requirements.
        StudentDto studentDto = studentService.findStudentData(studentId);
        if (studentDto == null) {
            // not found student.
            model.addAttribute("renderResponse", new Response(ResponseConstants.NOT_FOUND, "User not found!"));
            return "home-page";
        }
        model.addAttribute("semester", studentDto.getSemester());
        model.addAttribute("program", studentDto.getProgram());
        List<InstituteReqDto> instituteReqDto = instituteReqService.findInstituteReq(studentDto.getProgram(), studentDto.getSemester());
        model.addAttribute("instituteRequirements", instituteReqDto);

        // Send the available courses to Student with required information
        List<AvailableCourseDto> availableCourses = courseService.fetchAvailableCourses(studentDto.getProgram(),studentDto.getSemester());

        //debug
        // for(AvailableCourseDto availableCourse: availableCourses) System.out.println(availableCourse.getSlot()+"\t"+availableCourse.getCid()+"\t"+availableCourse.getName()+"\t\t\t"+availableCourse.getProgram()+"\t"+availableCourse.getCategory()+"\t"+availableCourse.getCredits());

        model.addAttribute("availableCourses", availableCourses);
        return "student/registration";
    }

    @PostMapping("/submit-preferences")
    public String loadSubmittedPreferences(@CookieValue(name = "student_id", required = false, defaultValue = "202201174") String studentId, @RequestParam String studentRequirements, @RequestParam String coursePreferences, @RequestParam String slotPreferences){
        System.out.println("Student-Requirements: "+studentRequirements);
        System.out.println("Course-Preferences: "+coursePreferences);
        System.out.println("Slot-Preferences: "+slotPreferences);

        if(studentRequirements!=null) {
            List<StudentReq> studentReqs = new ArrayList<>();
            String[] categoryCountMap = studentRequirements.split("#");
            for (String keyValue : categoryCountMap)
                studentReqs.add(new StudentReq(studentId, keyValue.split(":", 2)[0], Integer.valueOf(keyValue.split(":", 2)[1])));

            /*
            // debug
            for(StudentReq studentReq:studentReqs) System.out.println(studentReq.getCategory()+": "+studentReq.getCourse_cnt());
            System.out.println("\n");
             */
        }

        if(coursePreferences!=null) {
            List<CoursePref> coursePrefs = new ArrayList<>();
            String[] slotCourseListMap = coursePreferences.split("#");
            for (String slotCourseList : slotCourseListMap) {
                String slot = slotCourseList.split(":", 2)[0];
                String[] courseList = slotCourseList.split(":", 2)[1].split("\\$");
                for(int j=0;j<courseList.length;j++) coursePrefs.add(new CoursePref(studentId,slot,j+1,courseList[j]));
                System.out.println("\n");
            }

            /*
            // debug
            for(CoursePref coursePref:coursePrefs) System.out.println(coursePref.getSlot()+": "+coursePref.getPref()+": "+coursePref.getCid());
             */
        }

        if(slotPreferences!=null){
            List<SlotPref> slotPrefs = new ArrayList<>();
            String[] slotList=slotPreferences.split("\\$");
            for(int j=0;j<slotList.length;j++) slotPrefs.add(new SlotPref(studentId,j+1,slotList[j]));

            /*
            // debug
            for(SlotPref slotPref: slotPrefs) System.out.println(slotPref.getPref()+": "+slotPref.getSlot());
            System.out.println("\n");
             */
        }
        return "redirect:/admin-dashboard";
    }
}
