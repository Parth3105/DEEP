package in.ac.daiict.deep.utility.dataloader;

import in.ac.daiict.deep.dto.CourseDto;
import in.ac.daiict.deep.dto.CourseOfferingDto;
import in.ac.daiict.deep.dto.InstituteReqDto;
import in.ac.daiict.deep.dto.StudentDto;
import in.ac.daiict.deep.entity.CoursePref;
import in.ac.daiict.deep.entity.SlotPref;
import in.ac.daiict.deep.utility.Response;
import in.ac.daiict.deep.utility.allocation.model.AllocationCourse;
import in.ac.daiict.deep.utility.allocation.model.AllocationStudent;
import in.ac.daiict.deep.utility.allocation.model.CourseOffer;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface DataLoader {
    Response getStudentData(InputStream studentData, List<StudentDto> studentDtos);
    Response getCourseData(InputStream courseData, List<CourseDto> courseDtos);
    Response getInstituteRequirements(InputStream instReqData, List<InstituteReqDto> instituteReqDtos);
    Response getCourseForProgram(InputStream offerData, List<CourseOfferingDto> courseOfferingDtos);
    ByteArrayOutputStream createStudentPrefSheet(List<CoursePref> coursePrefList, List<SlotPref> slotPrefList);
    ByteArrayOutputStream createResultSheet(Map<String, AllocationStudent> students, Map<String, AllocationCourse> courses, Map<String, Map<String, String>> courseCategories);
    ByteArrayOutputStream createSeatSummary(List<CourseOffer> openFor, Map<String, AllocationCourse> courses, Map<String, Map<String, Integer>> availableSeats);
    ByteArrayOutputStream createCourseWiseAllocation(Map<String,AllocationCourse> courses, Map<String,AllocationStudent> students);
}
