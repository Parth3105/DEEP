package in.ac.daiict.deep.service;

import in.ac.daiict.deep.dto.StudentDto;
import in.ac.daiict.deep.entity.Student;
import in.ac.daiict.deep.utility.Response;

import java.util.List;

public interface StudentService {
    public Response insertAll(byte[] studentData);
    public List<StudentDto> fetchAllStudentDtos();
    public List<Student> fetchAllStudents();
    public void deleteAll();
    public long countBySemester(int semester);
    public StudentDto findStudentData(String sid);
}
