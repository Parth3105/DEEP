package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.dto.StudentDto;
import in.ac.daiict.deep.entity.Student;
import in.ac.daiict.deep.repository.StudentRepo;
import in.ac.daiict.deep.service.StudentService;
import in.ac.daiict.deep.utility.StudentLoader;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private StudentRepo studentRepo;
    private ModelMapper modelMapper;

    @Override
    public void insertAll(byte[] studentData) {
        StudentLoader studentLoader=new StudentLoader(new ByteArrayInputStream(studentData));
        List<StudentDto> studentDtos=studentLoader.getStudentData();
        // TypeToken helps retain generic of list
        List<Student> students=modelMapper.map(studentDtos,new TypeToken<List<Student>>(){}.getType());
        studentRepo.saveAllAndFlush(students);
    }

    @Override
    public List<StudentDto> getAll() {
        List<Student> students=studentRepo.findAll();
        return modelMapper.map(students,new TypeToken<List<StudentDto>>(){}.getType());
    }

    @Override
    public void deleteAll() {
        studentRepo.deleteAll();
    }
}
