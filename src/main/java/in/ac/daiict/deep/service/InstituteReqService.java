package in.ac.daiict.deep.service;

import in.ac.daiict.deep.dto.InstituteReqDto;

import java.util.List;

public interface InstituteReqService {
    public void insertAll(List<InstituteReqDto> instituteReqDtos);
    public List<InstituteReqDto> getAll();
}
