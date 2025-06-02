package in.ac.daiict.deep.service;

import in.ac.daiict.deep.dto.CourseOfferingDto;

import java.util.List;

public interface CourseOfferingService {
    public void insertAll(List<CourseOfferingDto> courseOfferDtos);
    public List<CourseOfferingDto> getAll();
}
