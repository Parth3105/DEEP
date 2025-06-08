package in.ac.daiict.deep.controller.student;

import in.ac.daiict.deep.constant.response.ResponseMessage;
import in.ac.daiict.deep.constant.response.ResponseStatus;
import in.ac.daiict.deep.constant.endpoints.StudentEndpoint;
import in.ac.daiict.deep.constant.template.StudentTemplate;
import in.ac.daiict.deep.dto.AvailableCourseDto;
import in.ac.daiict.deep.dto.InstituteReqDto;
import in.ac.daiict.deep.entity.CoursePref;
import in.ac.daiict.deep.entity.SlotPref;
import in.ac.daiict.deep.entity.Student;
import in.ac.daiict.deep.entity.StudentReq;
import in.ac.daiict.deep.service.*;
import in.ac.daiict.deep.utility.Response;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class EnrollmentController {
    private StudentService studentService;
    private CourseService courseService;
    private InstituteReqService instituteReqService;
    private StudentReqService studentReqService;
    private CoursePrefService coursePrefService;
    private SlotPrefService slotPrefService;

    @GetMapping(StudentEndpoint.ENROLL)
    public String renderEnrollmentForm(@CookieValue(name = "student_id", required = false, defaultValue = "202201406") String studentId, Model model) {
        // Send the semester & program of students and institute requirements.
        Student student = studentService.fetchStudentData(studentId);
        if (student == null) {
            // not found student.
            model.addAttribute("renderResponse", new Response(ResponseStatus.NOT_FOUND, ResponseMessage.USER_NOT_FOUND));
            return "redirect:"+StudentEndpoint.HOME_PAGE;
        }
        model.addAttribute("semester", student.getSemester());
        model.addAttribute("program", student.getProgram());
        List<InstituteReqDto> instituteReqDto = instituteReqService.findInstituteReq(student.getProgram(), student.getSemester());
        model.addAttribute("instituteRequirements", instituteReqDto);

        // Send the available courses to Student with required information
        List<AvailableCourseDto> availableCourses = courseService.fetchAvailableCourses(student.getProgram(), student.getSemester());

        //debug
        // for(AvailableCourseDto availableCourse: availableCourses) System.out.println(availableCourse.getSlot()+"\t"+availableCourse.getCid()+"\t"+availableCourse.getName()+"\t\t\t"+availableCourse.getProgram()+"\t"+availableCourse.getCategory()+"\t"+availableCourse.getCredits());

        model.addAttribute("availableCourses", availableCourses);
        return StudentTemplate.ENROLLMENT_FORM_PAGE;
    }

    @PostMapping(StudentEndpoint.SUBMIT_PREFERENCE)
    public String loadSubmittedPreferences(@CookieValue(name = "student_id", required = false, defaultValue = "202201406") String studentId, @RequestParam String studentRequirements, @RequestParam String coursePreferences, @RequestParam String slotPreferences){
        System.out.println("Student-Requirements: "+studentRequirements);
        System.out.println("Course-Preferences: "+coursePreferences);
        System.out.println("Slot-Preferences: "+slotPreferences);

        Thread recordStudentRequirements=new Thread(new Runnable() {
            @Override
            public void run() {
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
            }
        });

        Thread recordCoursePreferences=new Thread(new Runnable() {
            @Override
            public void run() {
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

            }
        });

        Thread recordSlotPreferences=new Thread(new Runnable() {
            @Override
            public void run() {
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
            }
        });

        recordStudentRequirements.start();
        recordCoursePreferences.start();
        recordSlotPreferences.start();
        try {
            recordStudentRequirements.join();
            recordCoursePreferences.join();
            recordSlotPreferences.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "redirect:"+ StudentEndpoint.PREFERENCE_SUMMARY;
    }
}
