package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.constant.status.RegistrationStatusEnum;
import in.ac.daiict.deep.dto.SystemStatusDto;
import in.ac.daiict.deep.entity.SystemStatus;
import in.ac.daiict.deep.repository.SystemStatusRepo;
import in.ac.daiict.deep.service.RegistrationTaskManager;
import in.ac.daiict.deep.service.SystemStatusService;
import in.ac.daiict.deep.util.status.RegistrationCloseDate;
import in.ac.daiict.deep.util.status.RegistrationStatus;
import in.ac.daiict.deep.util.status.ResultStatus;
import in.ac.daiict.deep.util.status.UpdateInstanceStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SystemStatusServiceImpl implements SystemStatusService {
    private SystemStatusRepo systemStatusRepo;
    private RegistrationTaskManager registrationTaskManager;

    @Override
    public void updateOnOpeningRegistration(SystemStatusDto systemStatusDto) {
        List<SystemStatus> systemStatusList=new ArrayList<>();
        systemStatusList.add(new SystemStatus(RegistrationStatus.getStatusName(),systemStatusDto.getRegistrationStatus().getStatusValue()));
        systemStatusList.add(new SystemStatus(RegistrationCloseDate.getStatusName(),systemStatusDto.getRegistrationCloseDate().getStringCloseDate()));
        systemStatusList.add(new SystemStatus(UpdateInstanceStatus.getStatusName(),systemStatusDto.getUpdateInstanceStatus().getStatusValue()));
        registrationTaskManager.updateCloseRegistrationDate(systemStatusDto.getRegistrationCloseDate().getCloseDate());
        registrationTaskManager.startRegistration();
        systemStatusRepo.saveAll(systemStatusList);
    }

    @Override
    public void updateOnExtendingRegistrationPeriod(SystemStatusDto systemStatusDto) {
        SystemStatusDto systemStatusDtoCheck=new SystemStatusDto(RegistrationStatusEnum.open);
        SystemStatus systemStatus=systemStatusRepo.findById(RegistrationStatus.getStatusName()).orElse(null);
        if(systemStatus==null || !systemStatus.getStatusValue().equals(systemStatusDtoCheck.getRegistrationStatus().getStatusValue())) return;
        registrationTaskManager.updateCloseRegistrationDate(systemStatusDto.getRegistrationCloseDate().getCloseDate());
        systemStatusRepo.save(new SystemStatus(RegistrationCloseDate.getStatusName(),systemStatusDto.getRegistrationCloseDate().getStringCloseDate()));
    }

    @Override
    public void updateOnClosingRegistration() {
        registrationTaskManager.closeRegistration();
    }

    @Override
    public void autoCloseRegistration() {
        SystemStatusDto systemStatusDto=new SystemStatusDto(RegistrationStatusEnum.close);
        systemStatusRepo.save(new SystemStatus(RegistrationStatus.getStatusName(),systemStatusDto.getRegistrationStatus().getStatusValue()));
    }

    @Override
    public void updateOnDeclaringResults(SystemStatusDto systemStatusDto) {
        List<SystemStatus> systemStatusList=new ArrayList<>();
        systemStatusList.add(new SystemStatus(RegistrationStatus.getStatusName(),systemStatusDto.getRegistrationStatus().getStatusValue()));
        systemStatusList.add(new SystemStatus(ResultStatus.getStatusName(),systemStatusDto.getResultStatus().getStatusValue()));
        systemStatusRepo.saveAll(systemStatusList);
    }

    @Override
    public SystemStatusDto fetchAllStatus() {
        List<SystemStatus> systemStatusList = systemStatusRepo.findAll();
        SystemStatusDto systemStatusDto=new SystemStatusDto();
        for(SystemStatus systemStatus: systemStatusList){
            if(systemStatus.getStatusName().equals(RegistrationStatus.getStatusName())) systemStatusDto.setRegistrationStatus(systemStatus.getStatusValue());
            else if(systemStatus.getStatusName().equals(RegistrationCloseDate.getStatusName())) systemStatusDto.setRegistrationCloseDate(systemStatus.getStatusValue());
            else if(systemStatus.getStatusName().equals(ResultStatus.getStatusName())) systemStatusDto.setResultStatus(systemStatus.getStatusValue());
            else if(systemStatus.getStatusName().equals(UpdateInstanceStatus.getStatusName())) systemStatusDto.setUpdateInstanceStatus(systemStatus.getStatusValue());
        }
        return systemStatusDto;
    }

    @Override
    public String fetchRegistrationStatus() {
        SystemStatus systemStatus= systemStatusRepo.findById(RegistrationStatus.getStatusName()).orElse(null);
        if(systemStatus==null) return null;
        return systemStatus.getStatusValue();
    }

    @Override
    public String fetchResultStatus() {
        SystemStatus systemStatus= systemStatusRepo.findById(ResultStatus.getStatusName()).orElse(null);
        if(systemStatus==null) return null;
        return systemStatus.getStatusValue();
    }


}
