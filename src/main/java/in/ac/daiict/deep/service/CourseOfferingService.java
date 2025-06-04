package in.ac.daiict.deep.service;

import in.ac.daiict.deep.dto.CourseOfferingDto;
import in.ac.daiict.deep.utility.Response;

import java.util.List;

public interface CourseOfferingService {
    public Response insertAll(byte[] courseOfferData);
    public List<CourseOfferingDto> getAll();
    public void deleteAll();
}
