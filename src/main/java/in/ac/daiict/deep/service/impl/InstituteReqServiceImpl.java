package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.dto.InstituteReqDto;
import in.ac.daiict.deep.entity.InstituteReq;
import in.ac.daiict.deep.repository.InstituteReqRepo;
import in.ac.daiict.deep.service.InstituteReqService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InstituteReqServiceImpl implements InstituteReqService {
    private InstituteReqRepo instituteReqRepo;
    private ModelMapper modelMapper;

    @Override
    public void insertAll(List<InstituteReqDto> instituteReqDtos) {
        // TypeToken helps retain generic of list
        List<InstituteReq> InstituteReqs=modelMapper.map(instituteReqDtos,new TypeToken<List<InstituteReq>>(){}.getType());
        instituteReqRepo.saveAllAndFlush(InstituteReqs);
    }

    @Override
    public List<InstituteReqDto> getAll() {
        List<InstituteReq> InstituteReqs=instituteReqRepo.findAll();
        return modelMapper.map(InstituteReqs,new TypeToken<List<InstituteReqDto>>(){}.getType());
    }
}
