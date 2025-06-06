package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.constant.ResponseConstants;
import in.ac.daiict.deep.dto.CourseOfferingDto;
import in.ac.daiict.deep.entity.CourseOffering;
import in.ac.daiict.deep.repository.CourseOfferingRepo;
import in.ac.daiict.deep.service.CourseOfferingService;
import in.ac.daiict.deep.utility.dataloader.DataLoader;
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
public class CourseOfferingServiceImpl implements CourseOfferingService {
    private CourseOfferingRepo courseOfferingRepo;
    private ModelMapper modelMapper;
    private DataLoader dataLoader;

    @Override
    public Response insertAll(byte[] courseOfferData) {
        deleteAll();
        List<CourseOfferingDto> courseOfferDtos=new ArrayList<>();
        Response status=dataLoader.getCourseForProgram(new ByteArrayInputStream(courseOfferData),courseOfferDtos);
        if(status.getStatus()!= ResponseConstants.OK) return status;
        // TypeToken helps retain generic of list
        List<CourseOffering> courseOffers=modelMapper.map(courseOfferDtos,new TypeToken<List<CourseOffering>>(){}.getType());
        courseOfferingRepo.saveAll(courseOffers);
        return new Response(ResponseConstants.OK,"Data Inserted Successfully!");
    }

    @Override
    public List<CourseOffering> fetchAllCourseOfferings() {
        return courseOfferingRepo.findAll();
    }

    @Override
    public List<CourseOfferingDto> fetchAllCourseOfferingDtos() {
        List<CourseOffering> courseOffers=courseOfferingRepo.findAll();
        return modelMapper.map(courseOffers,new TypeToken<List<CourseOfferingDto>>(){}.getType());
    }

    @Override
    public List<CourseOffering> fetchCourseOfferingBySemester(int semester) {
        List<CourseOffering> courseOfferingList=courseOfferingRepo.findBySemester(semester);
        if(courseOfferingList==null || courseOfferingList.isEmpty()) return null;
        return courseOfferingList;
    }

    @Override
    public void deleteAll() {
        courseOfferingRepo.deleteAll();
    }
}
