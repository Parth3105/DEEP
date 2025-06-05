package in.ac.daiict.deep.utility.dataloader;

import in.ac.daiict.deep.dto.CourseDto;
import in.ac.daiict.deep.dto.CourseOfferingDto;
import in.ac.daiict.deep.dto.InstituteReqDto;
import in.ac.daiict.deep.dto.StudentDto;
import in.ac.daiict.deep.utility.Response;

import java.io.InputStream;
import java.util.List;

public interface DataLoader {
    Response getStudentData(InputStream studentData, List<StudentDto> studentDtos);
    Response getCourseData(InputStream courseData, List<CourseDto> courseDtos);
    Response getInstituteRequirements(InputStream instReqData, List<InstituteReqDto> instituteReqDtos);
    Response getCourseForProgram(InputStream offerData, List<CourseOfferingDto> courseOfferingDtos);
}
