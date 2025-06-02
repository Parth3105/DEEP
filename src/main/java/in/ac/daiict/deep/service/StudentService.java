package in.ac.daiict.deep.service;

import in.ac.daiict.deep.dto.StudentDto;

import java.util.List;

public interface StudentService {
    public void insertAll(List<StudentDto> studentDtos);
    public List<StudentDto> getAll();
}
