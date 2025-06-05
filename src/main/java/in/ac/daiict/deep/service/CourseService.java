package in.ac.daiict.deep.service;


import in.ac.daiict.deep.dto.AvailableCourseDto;
import in.ac.daiict.deep.dto.CourseDto;
import in.ac.daiict.deep.entity.Course;
import in.ac.daiict.deep.utility.Response;

import java.util.List;
import java.util.Map;

public interface CourseService {
    public Response insertAll(byte[] courseData);

    public List<Course> fetchAllCourses();

    public List<CourseDto> fetchAllCourseDtos();

    public void deleteAll();

    public boolean isPresent(String cid);

    public List<AvailableCourseDto> fetchAvailableCourses(String program, int semester);
}
