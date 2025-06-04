package in.ac.daiict.deep.service;

import in.ac.daiict.deep.dto.InstituteReqDto;
import in.ac.daiict.deep.utility.Response;

import java.util.List;

public interface InstituteReqService {
    public Response insertAll(byte[] instituteData);
    public List<InstituteReqDto> getAll();
    public void deleteAll();
    public List<InstituteReqDto> findInstituteReq(String program, int semester);
}
