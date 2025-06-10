package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.constant.status.RegistrationStatusEnum;
import in.ac.daiict.deep.dto.SystemStatusDto;
import in.ac.daiict.deep.entity.SystemStatus;
import in.ac.daiict.deep.repository.SystemStatusRepo;
import in.ac.daiict.deep.service.RegistrationTaskManager;
import in.ac.daiict.deep.service.SystemStatusService;
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
        systemStatusList.add(new SystemStatus(systemStatusDto.getRegistrationStatus().getStatusName(),systemStatusDto.getRegistrationStatus().getStatusValue()));
        systemStatusList.add(new SystemStatus(systemStatusDto.getRegistrationCloseDate().getStatusName(),systemStatusDto.getRegistrationCloseDate().getStringCloseDate()));
        systemStatusList.add(new SystemStatus(systemStatusDto.getUpdateInstanceStatus().getStatusName(),systemStatusDto.getUpdateInstanceStatus().getStatusValue()));
        registrationTaskManager.updateCloseRegistrationDate(systemStatusDto.getRegistrationCloseDate().getCloseDate());
        registrationTaskManager.startRegistration();
        systemStatusRepo.saveAll(systemStatusList);
    }

    @Override
    public void updateOnExtendingRegistrationPeriod(SystemStatusDto systemStatusDto) {
        registrationTaskManager.updateCloseRegistrationDate(systemStatusDto.getRegistrationCloseDate().getCloseDate());
        systemStatusRepo.save(new SystemStatus(systemStatusDto.getRegistrationCloseDate().getStatusName(),systemStatusDto.getRegistrationCloseDate().getStringCloseDate()));
    }

    @Override
    public void updateOnClosingRegistration(SystemStatusDto systemStatusDto) {
        registrationTaskManager.closeRegistration();
        systemStatusRepo.save(new SystemStatus(systemStatusDto.getRegistrationStatus().getStatusName(),systemStatusDto.getRegistrationStatus().getStatusValue()));
    }

    @Override
    public void autoCloseRegistration() {
        SystemStatusDto systemStatusDto=new SystemStatusDto(RegistrationStatusEnum.CLOSE);
        systemStatusRepo.save(new SystemStatus(systemStatusDto.getRegistrationStatus().getStatusName(),systemStatusDto.getRegistrationStatus().getStatusValue()));
    }


}
