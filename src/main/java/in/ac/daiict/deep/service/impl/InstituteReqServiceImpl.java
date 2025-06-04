package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.constant.ResponseConstants;
import in.ac.daiict.deep.dto.InstituteReqDto;
import in.ac.daiict.deep.entity.InstituteReq;
import in.ac.daiict.deep.repository.InstituteReqRepo;
import in.ac.daiict.deep.service.InstituteReqService;
import in.ac.daiict.deep.utility.DataLoader;
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
public class InstituteReqServiceImpl implements InstituteReqService {
    private InstituteReqRepo instituteReqRepo;
    private ModelMapper modelMapper;
    DataLoader dataLoader;

    @Override
    public Response insertAll(byte[] instituteReqData) {
        deleteAll();
        List<InstituteReqDto> instituteReqDtos=new ArrayList<>();
        Response status=dataLoader.getInstituteRequirements(new ByteArrayInputStream(instituteReqData),instituteReqDtos);
        if(status.getStatus()!= ResponseConstants.OK) return status;
        // TypeToken helps retain generic of list
        List<InstituteReq> instituteReqs=modelMapper.map(instituteReqDtos,new TypeToken<List<InstituteReq>>(){}.getType());
        instituteReqRepo.saveAll(instituteReqs);
        return new Response(ResponseConstants.OK,"Data Inserted Successfully!");
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
