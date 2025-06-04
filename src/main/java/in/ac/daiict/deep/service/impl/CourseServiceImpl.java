package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.constant.ResponseConstants;
import in.ac.daiict.deep.dto.CourseDto;
import in.ac.daiict.deep.entity.Course;
import in.ac.daiict.deep.repository.CourseRepo;
import in.ac.daiict.deep.service.CourseService;
import in.ac.daiict.deep.utility.DataLoader;
import in.ac.daiict.deep.utility.Response;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private CourseRepo courseRepo;
    private ModelMapper modelMapper;
    private DataLoader dataLoader;

    @Override
    public Response insertAll(byte[] courseData) {
        deleteAll();
        List<CourseDto> courseDtos=new ArrayList<>();
        Response status=dataLoader.getCourseData(new ByteArrayInputStream(courseData),courseDtos);
        if(status.getStatus()!= ResponseConstants.OK) return status;
        // TypeToken helps retain generic of list
        List<Course> courses=modelMapper.map(courseDtos,new TypeToken<List<Course>>(){}.getType());
        courseRepo.saveAll(courses);
        return new Response(ResponseConstants.OK,"Data Inserted Successfully!");
    }

    @Override
    public List<CourseDto> getAll() {
        List<Course> courses=courseRepo.findAll();
        return modelMapper.map(courses,new TypeToken<List<CourseDto>>(){}.getType());
    }

    @Override
    public void deleteAll() {
        courseRepo.deleteAll();
    }

    @Override
    public boolean isPresent(String cid) {
        return courseRepo.existsById(cid);
    }
}
