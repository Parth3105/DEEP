package in.ac.daiict.deep.service;

import in.ac.daiict.deep.dto.StudentDto;

import java.util.List;

public interface StudentService {
    public void insertAll(byte[] studentData);
    public List<StudentDto> getAll();
    public void deleteAll();
    public long countBySemester(int semester);
}
