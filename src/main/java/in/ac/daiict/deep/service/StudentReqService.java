package in.ac.daiict.deep.service;

import in.ac.daiict.deep.dto.StudentReqDto;
import in.ac.daiict.deep.entity.StudentReq;

import java.util.List;

public interface StudentReqService {
    List<StudentReq> fetchAllStudentReqs();
    void insertAll(List<StudentReq> studentReqList);
    List<StudentReqDto> findStudentRequirements(String sid);
}
