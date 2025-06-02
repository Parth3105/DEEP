package in.ac.daiict.deep.service;


import in.ac.daiict.deep.dto.CourseDto;

import java.util.List;

public interface CourseService {
    public void insertAll(byte[] courseData);
    public List<CourseDto> getAll();
    public void deleteAll();
}
