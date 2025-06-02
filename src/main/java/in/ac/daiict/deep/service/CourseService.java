package in.ac.daiict.deep.service;


import in.ac.daiict.deep.dto.CourseDto;

import java.util.List;

public interface CourseService {
    public void insertAll(List<CourseDto> courseDtos);
    public List<CourseDto> getAll();
}
