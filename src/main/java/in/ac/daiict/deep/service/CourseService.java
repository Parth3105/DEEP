package in.ac.daiict.deep.service;


import in.ac.daiict.deep.dto.CourseDto;
import in.ac.daiict.deep.utility.Response;

import java.util.List;

public interface CourseService {
    public Response insertAll(byte[] courseData);
    public List<CourseDto> getAll();
    public void deleteAll();
    public boolean isPresent(String cid);
}
