package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.dto.CourseDto;
import in.ac.daiict.deep.entity.Course;
import in.ac.daiict.deep.repository.CourseRepo;
import in.ac.daiict.deep.service.CourseService;
import in.ac.daiict.deep.utility.CourseLoader;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private CourseRepo courseRepo;
    private ModelMapper modelMapper;

    @Override
    public void insertAll(byte[] courseData) {
        deleteAll();
        CourseLoader courseLoader=new CourseLoader(new ByteArrayInputStream(courseData));
        List<CourseDto> courseDtos=courseLoader.getCourseData();
        // TypeToken helps retain generic of list
        List<Course> courses=modelMapper.map(courseDtos,new TypeToken<List<Course>>(){}.getType());
        courseRepo.saveAllAndFlush(courses);
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
}
