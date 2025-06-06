package in.ac.daiict.deep.service;

import in.ac.daiict.deep.dto.StudentDto;
import in.ac.daiict.deep.entity.Student;
import in.ac.daiict.deep.utility.Response;

import java.util.List;

public interface StudentService {
    Response insertAll(byte[] studentData);
    List<StudentDto> fetchAllStudentDtos();
    List<Student> fetchAllStudents();
    List<Student> fetchStudentsBySemester(int semester);
    void deleteAll();
    long countBySemester(int semester);
    StudentDto findStudentData(String sid);
}
