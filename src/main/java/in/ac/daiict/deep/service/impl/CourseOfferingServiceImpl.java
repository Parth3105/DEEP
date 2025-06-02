package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.dto.CourseOfferingDto;
import in.ac.daiict.deep.entity.CourseOffering;
import in.ac.daiict.deep.repository.CourseOfferingRepo;
import in.ac.daiict.deep.service.CourseOfferingService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseOfferingServiceImpl implements CourseOfferingService {
    private CourseOfferingRepo courseOfferingRepo;
    private ModelMapper modelMapper;

    @Override
    public void insertAll(List<CourseOfferingDto> courseOfferDtos) {
        // TypeToken helps retain generic of list
        List<CourseOffering> courseOffers=modelMapper.map(courseOfferDtos,new TypeToken<List<CourseOffering>>(){}.getType());
        courseOfferingRepo.saveAllAndFlush(courseOffers);
    }

    @Override
    public List<CourseOfferingDto> getAll() {
        List<CourseOffering> courseOffers=courseOfferingRepo.findAll();
        return modelMapper.map(courseOffers,new TypeToken<List<CourseOfferingDto>>(){}.getType());
    }
}
