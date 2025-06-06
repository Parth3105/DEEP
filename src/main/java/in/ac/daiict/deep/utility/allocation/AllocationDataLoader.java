package in.ac.daiict.deep.utility.allocation;

import in.ac.daiict.deep.entity.*;
import in.ac.daiict.deep.service.*;
import in.ac.daiict.deep.utility.allocation.model.AllocationCourse;
import in.ac.daiict.deep.utility.allocation.model.AllocationStudent;
import in.ac.daiict.deep.utility.allocation.model.CourseOffer;
import in.ac.daiict.deep.utility.allocation.model.InstituteRequirement;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class AllocationDataLoader {
    private StudentService studentService;
    private StudentReqService studentReqService;
    private SlotPrefService slotPrefService;
    private CoursePrefService coursePrefService;
    private CourseService courseService;
    private CourseOfferingService courseOfferingService;
    private InstituteReqService instituteReqService;
    private AllocationResultService allocationResultService;
    private SeatSummaryService seatSummaryService;
    private ModelMapper modelMapper;

    public Map<String, AllocationStudent> getStudentData(int semester, int[] maxRequirement){
        Map<String, AllocationStudent> allocationStudents=new HashMap<>();

        // Fetch and set the student information
        List<Student> studentData=studentService.fetchStudentsBySemester(semester);
        if(studentData==null) return null;
        for(Student s: studentData) allocationStudents.put(s.getSid(),new AllocationStudent(s.getSid(),s.getName(),s.getProgram(),s.getSemester()));

        // Fetch and set the student requirements
        List<StudentReq> studentReqs=studentReqService.fetchAllStudentReqs();
        Map<String,Map<String,Integer>> studentReqMap=new HashMap<>();
        for(StudentReq studentReq: studentReqs) {
            Map<String, Integer> reqs=studentReqMap.getOrDefault(studentReq.getSid(),new HashMap<>());
            reqs.put(studentReq.getCategory(),studentReq.getCourse_cnt());
            studentReqMap.put(studentReq.getSid(),reqs);
        }

        for(Map.Entry<String,Map<String,Integer>> reqEntry: studentReqMap.entrySet()){
            AllocationStudent allocationStudent=allocationStudents.get(reqEntry.getKey());
            allocationStudent.setRequirements(reqEntry.getValue());
            allocationStudents.put(reqEntry.getKey(),allocationStudent);

            int reqCnt=0;
            for(int cnt: reqEntry.getValue().values()) reqCnt+=cnt;
            maxRequirement[0]=Math.max(maxRequirement[0],reqCnt);
        }

        // Fetch and set the slot preferences
        List<SlotPref> slotPrefs=slotPrefService.fetchAllSlotSortedByPref();
        Map<String,List<String>> studentSlotPrefMap=new HashMap<>();
        for(SlotPref slotPref:slotPrefs){
            List<String> pref= studentSlotPrefMap.getOrDefault(slotPref.getSid(),new ArrayList<>());
            pref.add(slotPref.getSlot());
            studentSlotPrefMap.put(slotPref.getSid(),pref);
        }
        for(Map.Entry<String,List<String>> slotPrefEntry: studentSlotPrefMap.entrySet()){
            AllocationStudent allocationStudent=allocationStudents.get(slotPrefEntry.getKey());
            allocationStudent.setSlotPreferences(slotPrefEntry.getValue());
            allocationStudents.put(slotPrefEntry.getKey(),allocationStudent);
        }

        // Fetch and set the course preferences
        List<CoursePref> coursePrefs=coursePrefService.fetchAllCoursePrefSortedByPref();
        Map<String,Map<String,List<String>>> studentCoursePrefMap=new HashMap<>();
        for(CoursePref coursePref:coursePrefs){
            Map<String,List<String>> slotWiseCoursePref=studentCoursePrefMap.getOrDefault(coursePref.getSid(), new HashMap<>());
            List<String> prefs=slotWiseCoursePref.getOrDefault(coursePref.getSlot(),new ArrayList<>());
            prefs.add(coursePref.getCid());
            slotWiseCoursePref.put(coursePref.getSlot(),prefs);
            studentCoursePrefMap.put(coursePref.getSid(),slotWiseCoursePref);
        }
        for(Map.Entry<String,Map<String,List<String>>> coursePrefEntry: studentCoursePrefMap.entrySet()){
            AllocationStudent allocationStudent=allocationStudents.get(coursePrefEntry.getKey());
            allocationStudent.setCoursePreferences(coursePrefEntry.getValue());
            if(allocationStudent.getSid().equals("202201174"));
            allocationStudents.put(coursePrefEntry.getKey(),allocationStudent);
        }
        return allocationStudents;
    }

    public Map<String, AllocationCourse> getCourseData(){
        Map<String,AllocationCourse> courseData=new HashMap<>();
        List<Course> courses=courseService.fetchAllCourses();
        for(Course course: courses) courseData.put(course.getCid(),new AllocationCourse(course.getCid(),course.getName(),course.getCredits(),course.getSlot()));
        return courseData;
    }

    public List<CourseOffer> getCourseOffers(int semester, Map<String,Map<String,String>> categories, Map<String,Map<String,Integer>> availableSeats){
        List<CourseOffering> courseOfferings=courseOfferingService.fetchCourseOfferingBySemester(semester);
        if(courseOfferings==null) return null;
        for(CourseOffering courseOffering: courseOfferings){
            Map<String,String> programCategoryMap=categories.getOrDefault(courseOffering.getCid(),new HashMap<>());
            programCategoryMap.put(courseOffering.getProgram(),courseOffering.getCategory());
            categories.put(courseOffering.getCid(),programCategoryMap);

            Map<String,Integer> courseSeatMap=availableSeats.getOrDefault(courseOffering.getProgram(),new HashMap<>());
            courseSeatMap.put(courseOffering.getCid(),courseOffering.getSeats());
            availableSeats.put(courseOffering.getProgram(),courseSeatMap);
        }
        return modelMapper.map(courseOfferings,new TypeToken<List<CourseOffer>>(){}.getType());
    }

    public List<InstituteRequirement> getInstituteRequirements(int semester){
        List<InstituteReq> instituteReqs=instituteReqService.fetchInstituteReqBySemester(semester);
        if(instituteReqs==null) return null;
        return modelMapper.map(instituteReqs,new TypeToken<List<InstituteRequirement>>(){}.getType());
    }

    public void saveAllocationResult(List<AllocationStudent> allocationStudent){
        List<AllocationResult> allocationResultList=new ArrayList<>();
        for(AllocationStudent student:allocationStudent){
            Set<String> allocatedCourses=student.getAllocatedCourses();
            for(String courseId: allocatedCourses) allocationResultList.add(new AllocationResult(student.getSid(),courseId));
        }
        allocationResultService.insertAll(allocationResultList);
    }

    public void saveSeatSummary(int semester, Map<String,Map<String,Integer>> availableSeats){
        List<SeatSummary> seatSummaryList=new ArrayList<>();
        for(Map.Entry<String,Map<String,Integer>> seatEntry: availableSeats.entrySet()){
            String program=seatEntry.getKey();
            Map<String,Integer> courseSeatMap=seatEntry.getValue();
            for(Map.Entry<String,Integer> courseSeatMapEntry: courseSeatMap.entrySet()) seatSummaryList.add(new SeatSummary(courseSeatMapEntry.getKey(),program,semester,courseSeatMapEntry.getValue()));
        }
        seatSummaryService.insertAll(seatSummaryList);
    }
}
