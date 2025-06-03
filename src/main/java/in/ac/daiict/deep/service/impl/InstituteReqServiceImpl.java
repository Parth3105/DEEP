package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.dto.InstituteReqDto;
import in.ac.daiict.deep.entity.InstituteReq;
import in.ac.daiict.deep.repository.InstituteReqRepo;
import in.ac.daiict.deep.service.InstituteReqService;
import in.ac.daiict.deep.utility.InstituteReqLoader;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class InstituteReqServiceImpl implements InstituteReqService {
    private InstituteReqRepo instituteReqRepo;
    private ModelMapper modelMapper;

    @Override
    public void insertAll(byte[] instituteReqData) {
        deleteAll();
        InstituteReqLoader instituteReqLoader=new InstituteReqLoader(new ByteArrayInputStream(instituteReqData));
        List<InstituteReqDto> instituteReqDtos=instituteReqLoader.getInstituteRequirements();
        // TypeToken helps retain generic of list
        List<InstituteReq> instituteReqs=modelMapper.map(instituteReqDtos,new TypeToken<List<InstituteReq>>(){}.getType());
        instituteReqRepo.saveAll(instituteReqs);
    }

    @Override
    public List<InstituteReqDto> getAll() {
        List<InstituteReq> InstituteReqs=instituteReqRepo.findAll();
        return modelMapper.map(InstituteReqs,new TypeToken<List<InstituteReqDto>>(){}.getType());
    }

    @Override
    public void deleteAll() {
        instituteReqRepo.deleteAll();
    }
}
