package in.ac.daiict.deep.service;

import in.ac.daiict.deep.dto.CourseOfferingDto;

import java.util.List;

public interface CourseOfferingService {
    public void insertAll(byte[] courseOfferData);
    public List<CourseOfferingDto> getAll();
    public void deleteAll();
}
