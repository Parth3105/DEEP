package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.entity.StudentReq;
import in.ac.daiict.deep.repository.StudentReqRepo;
import in.ac.daiict.deep.service.StudentReqService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentReqServiceImpl implements StudentReqService {
    private StudentReqRepo studentReqRepo;

    @Override
    public List<StudentReq> fetchAllStudentReqs() {
        return studentReqRepo.findAll();
    }
}
