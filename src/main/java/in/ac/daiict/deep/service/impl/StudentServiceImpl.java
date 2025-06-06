package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.constant.response.ResponseStatus;
import in.ac.daiict.deep.dto.StudentDto;
import in.ac.daiict.deep.entity.Student;
import in.ac.daiict.deep.repository.StudentRepo;
import in.ac.daiict.deep.service.StudentService;
import in.ac.daiict.deep.utility.dataloader.DataLoader;
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
public class StudentServiceImpl implements StudentService {
    private StudentRepo studentRepo;
    private ModelMapper modelMapper;
    private DataLoader dataLoader;

    @Override
    public Response insertAll(byte[] studentData) {
        List<StudentDto> studentDtos=new ArrayList<>();
        Response status=dataLoader.getStudentData(new ByteArrayInputStream(studentData),studentDtos);
        if(status.getStatus()!= ResponseStatus.OK) return status;
        // TypeToken helps retain generic of list
        List<Student> students=modelMapper.map(studentDtos,new TypeToken<List<Student>>(){}.getType());
        studentRepo.saveAll(students);
        return new Response(ResponseStatus.OK,"Data Inserted Successfully!");
    }

    @Override
    public List<StudentDto> fetchAllStudentDtos() {
        List<Student> students=studentRepo.findAll();
        return modelMapper.map(students,new TypeToken<List<StudentDto>>(){}.getType());
    }

    @Override
    public List<Student> fetchAllStudents() {
        return studentRepo.findAll();
    }

    @Override
    public List<Student> fetchStudentsBySemester(int semester) {
        List<Student> studentList=studentRepo.findBySemester(semester);
        if(studentList==null || studentList.isEmpty()) return null;
        return studentList;
    }

    @Override
    public void deleteAll() {
        studentRepo.deleteAll();
    }

    @Override
    public long countBySemester(int semester) {
        return studentRepo.countBySemester(semester);
    }

    @Override
    public long countAllStudents() {
        return studentRepo.count();
    }

    @Override
    public Student fetchStudentData(String sid) {
        return studentRepo.findById(sid).orElse(null);
    }

    @Override
    public StudentDto fetchStudentDto(String sid) {
        Student student=studentRepo.findById(sid).orElse(null);
        if(student==null) return null;
        return modelMapper.map(student, StudentDto.class);
    }
}
