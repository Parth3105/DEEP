package in.ac.daiict.deep.utility.allocation;

import in.ac.daiict.deep.constant.ResponseConstants;
import in.ac.daiict.deep.utility.Response;
import in.ac.daiict.deep.utility.allocation.model.AllocationCourse;
import in.ac.daiict.deep.utility.allocation.model.AllocationStudent;
import in.ac.daiict.deep.utility.allocation.model.CourseOffer;
import in.ac.daiict.deep.utility.allocation.model.InstituteRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AllocationSystem {
    private AllocationDataLoader allocationDataLoader;
    private Map<String, AllocationStudent> students; // key=studentID,value=Student Object
    private Map<String, AllocationCourse> courses; // key=courseID,value=Course Object
    private List<CourseOffer> openFor;
    private List<InstituteRequirement> instituteRequirements;
    private Map<Integer, List<AllocationStudent>> priorityGroups; // key=priority(integer),Student list.
    private Map<String, Map<String, Integer>> availableSeats; // key=program, value= map with key=courseID,value=seats
    private Map<String, Map<String, String>> courseCategories; // key=courseID,value=map with key=program,value=category
    private int[] maxRequirement;
    private int semester;

    @Autowired
    public AllocationSystem(AllocationDataLoader allocationDataLoader){
        this.allocationDataLoader=allocationDataLoader;
    }

    public void initializeSetup(int semester){
        this.semester=semester;
        students=allocationDataLoader.getStudentData(semester,maxRequirement);
        courses=allocationDataLoader.getCourseData();

        courseCategories = new HashMap<>();
        availableSeats = new HashMap<>();
        openFor=allocationDataLoader.getCourseOffers(semester,courseCategories,availableSeats);

        instituteRequirements=allocationDataLoader.getInstituteRequirements(semester);
        priorityGroups = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                return i2 - i1;
            }
        });
        maxRequirement = new int[1];
    }

    /**
     * @return total no. of courses, from a particular elective category (ex. TE,ICT,HASSE,SE), in a semester of a program.
     */
    private int getInstituteRequirement(String program, int semester, String category) {
        for (InstituteRequirement req : instituteRequirements) {
            if (req.getProgram().equals(program) && req.getSemester() == semester && req.getCategory().equals(category))
                return req.getCourse_cnt();
        }
        return 0;
    }
    /**
     * Allocate courses to students in two phases.
     * First phase: where no courses are allocated to students. This phase takes care of allocation of courses according to the institute requirements.
     * Second phase: where institute requirements are fulfilled and extra/overload courses are allocated according to the student requirements, if any.
     */
    public Response allocationInPhase(int semester) {
        // Initialize the setup to load data
        initializeSetup(semester);
        if(students==null || openFor==null || instituteRequirements==null) new Response(ResponseConstants.BAD_REQUEST,"Error: No Student Found");

        int[] unmetReqCnt = new int[1];
        allocationPhase(true);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Phase-1 finished");
        unmetReqCnt[0] = 0;
//        System.out.println("All Students allocated? " + isStudentReqFulfilled(true, unmetReqCnt));
        System.out.println("Not allocated in phase-1: " + unmetReqCnt[0]);
        System.out.println("--------------------------------------------------------------------------------");

        allocationPhase(false);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Phase-2 finished");
        unmetReqCnt[0] = 0;
//        System.out.println("All Students allocated? " + isStudentReqFulfilled(false, unmetReqCnt));
        System.out.println("Not allocated in phase-2: " + unmetReqCnt[0]);
        System.out.println("--------------------------------------------------------------------------------");

        saveOutput();
        return new Response(ResponseConstants.OK,"Successfully allocated");
    }

    /**
     * Allocate Courses to students in the descending order of their priority.
     *
     * @param isPhaseOne - if Phase to fulfill institute-requirement or extra-requirements.
     */
    private void allocationPhase(boolean isPhaseOne) {
        List<AllocationStudent> studentList = new ArrayList<>(students.values());
        Collections.shuffle(studentList);

        for (int i = 0; i < maxRequirement[0]; i++) {
            updatePriorityGroups();

            Set<Integer> priorities = priorityGroups.keySet();

            for (int priority : priorities) {
                for (AllocationStudent student : priorityGroups.get(priority)) {
                    courseAllocation(student, isPhaseOne, false);
                }
            }
        }
    }

    /**
     * As name suggests, it groups based on their priority value and order the students in group based on cumulative priority.
     * Stores them in priorityGroups variable.
     */
    private void updatePriorityGroups() {
        priorityGroups.clear();
        List<AllocationStudent> studentList = new ArrayList<>(students.values());
        Collections.shuffle(studentList);

        for (AllocationStudent student : studentList) {
            List<AllocationStudent> studentsByPriority;
            studentsByPriority = priorityGroups.getOrDefault(student.getPriority(), new ArrayList<>());
            studentsByPriority.add(student);
            priorityGroups.put(student.getPriority(), studentsByPriority);
        }

        Set<Integer> priorities = priorityGroups.keySet();
        for (int priority : priorities) {
            List<AllocationStudent> studentsByPriority = priorityGroups.getOrDefault(priority, new ArrayList<>());
            studentsByPriority.sort(new Comparator<AllocationStudent>() {
                @Override
                public int compare(AllocationStudent s1, AllocationStudent s2) {
                    return s2.getCumulativePriority() - s1.getCumulativePriority();
                }
            });
            priorityGroups.put(priority, studentsByPriority);
        }
    }

    /**
     * Course allocation main logic which is based on preference.
     * One course is allocated to a student based on availability of the most preferred course in order of the slot
     * preference. i.e. for preference-1 all slots are checked and if not available check for pref-2 in all slots and so on
     *
     * @param student:    contains information of a student
     * @param isPhaseOne: whether phase-1 or 2.
     */
    private void courseAllocation(AllocationStudent student, boolean isPhaseOne, boolean isErrorPhase) {
        int maxPrefIndex = -1;
        for (List<String> coursePrefBySlot : student.getCoursePreferences().values()) {
            maxPrefIndex=coursePrefBySlot.size();
        }

        for (int prefIndex = 0; prefIndex < maxPrefIndex; prefIndex++) {
            for (String slot : student.getSlotPreferences()) {
                if (student.getAllocatedSlots().contains(slot)) continue;

                List<String> coursePrefInSlot = student.getCoursePreferences().getOrDefault(slot,new ArrayList<>());
                String coursePref = null;
                if(coursePrefInSlot.size()>prefIndex) coursePref=coursePrefInSlot.get(prefIndex);
                if (coursePref == null) continue;
                String courseID = coursePref;

//                if(isErrorPhase) System.out.println(">>> Trying course: "+courseID+" in Slot-"+slot);

                if (canAllocateCourse(student, courseID, isPhaseOne, isErrorPhase)) {
                    allocateCourse(student, courseID);
                    student.setPriority(prefIndex + 1);
                    student.setCumulativePriority(student.getCumulativePriority() + student.getPriority());
                    return;
                }
            }
        }
    }
    /**
     * Function checks the availability of the course for the student based on certain factors like seat-matrix, allocated-slots etc.
     *
     * @param student:    information of student
     * @param courseID:   id of the course for which availability needs to be checked
     * @param isPhaseOne - phase-1 or 2. which becomes a deciding factor whether it comes under institute requirement or extra during phase-1
     * @return whether given course can be allocated to the student or not
     */
    private boolean canAllocateCourse(AllocationStudent student, String courseID, boolean isPhaseOne, boolean isErrorPhase) {
        boolean flag = false;
        for (CourseOffer openCourse : openFor) {
            if (openCourse.getCid().equals(courseID) && openCourse.getProgram().equals(student.getProgram()) && openCourse.getSemester() == student.getSemester()) {
                flag = true;
                break;
            }
        }
        if (!flag) {
//            if (isErrorPhase) System.out.println(" " + courseID + ": Course is not available to student"); //Debug
            return false;
        }

        if (!availableSeats.containsKey(student.getProgram())) {
//            if (isErrorPhase) System.out.println("Reason: No seats for the program: " + student.program); //Debug
            return false;
        } else if (availableSeats.get(student.getProgram()).getOrDefault(courseID, 0) <= 0) {
//            if (isErrorPhase) System.out.println(" " + courseID + ": All seats allocated"); //Debug
            return false;
        }
        String courseCategory = courseCategories.get(courseID).get(student.getProgram());
        int instituteReq = getInstituteRequirement(student.getProgram(), student.getSemester(), courseCategory);

        int reqCourseCnt;
        if (isPhaseOne) reqCourseCnt = Math.min(student.getRequirements().getOrDefault(courseCategory, 0), instituteReq);
        else reqCourseCnt = student.getRequirements().getOrDefault(courseCategory, 0);

        if (student.getAllocatedCategories().getOrDefault(courseCategory, 0) < reqCourseCnt) return true;
        else {
            if (isErrorPhase)
                System.out.println(" Reason::::: allocatedCategories for " + courseCategory + ": " + student.getAllocatedCategories().getOrDefault(courseCategory, 0) + " >= " + "reqCourseCnt: " + reqCourseCnt); //Debug
            return false;
        }
    }

    /**
     * The course is allocated to the student and crucial updates are managed here like decreasing seat-matrix,
     * updating student's allocation data etc.
     *
     * @param student:  information of student.
     * @param courseID: id of the course to be allocated to the student
     */
    private void allocateCourse(AllocationStudent student, String courseID) {
        AllocationCourse course = courses.get(courseID);
        student.addAllocatedCourse(courseID);
        student.addAllocatedSlot(course.getSlot());

        int allocatedCategoryCnt = student.getAllocatedCategories().getOrDefault(courseCategories.get(courseID).get(student.getProgram()), 0);
        student.addAllocatedCategory(courseCategories.get(courseID).get(student.getProgram()), allocatedCategoryCnt + 1);

        int seats = availableSeats.get(student.getProgram()).getOrDefault(courseID, 0);
        Map<String, Integer> programSpecificSeats = availableSeats.getOrDefault(student.getProgram(), new HashMap<>());
        programSpecificSeats.put(courseID, seats - 1);
        availableSeats.put(student.getProgram(), programSpecificSeats);
    }

    private boolean isStudentReqFulfilled(boolean isPhaseOne, int[] unmetReqCnt) {
        boolean flag = true;
        for (AllocationStudent student : students.values()) {
            for (Map.Entry<String, Integer> requirement : student.getRequirements().entrySet()) {
                String category = String.valueOf(requirement.getKey());
                int studentReq = requirement.getValue();
                int instituteReq = getInstituteRequirement(student.getProgram(), student.getSemester(), category);

                int reqCourseCnt;
                if (isPhaseOne) reqCourseCnt = Math.min(studentReq, instituteReq);
                else reqCourseCnt = studentReq;

                if (student.getAllocatedCategories().getOrDefault(category, 0) < reqCourseCnt) {
//                    if(!isPhaseOne) pendingRequirements.add(student.id);
                    unmetReqCnt[0]++;
                    flag = false;
                }
            }
        }
        return flag;
    }

    private void saveOutput(){
        Thread recordAllocationResult=new Thread(new Runnable() {
            @Override
            public void run() {
                allocationDataLoader.saveAllocationResult((List<AllocationStudent>)students.values());
            }
        });
        Thread recordSeatSummary=new Thread(new Runnable() {
            @Override
            public void run() {
                allocationDataLoader.saveSeatSummary(semester, availableSeats);
            }
        });
        recordAllocationResult.start();
        recordSeatSummary.start();
    }
}
