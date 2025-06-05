package in.ac.daiict.deep.service;

import in.ac.daiict.deep.dto.StudentReqDto;
import in.ac.daiict.deep.entity.StudentReq;

import java.util.List;

public interface StudentReqService {
    public List<StudentReq> fetchAllStudentReqs();
}
