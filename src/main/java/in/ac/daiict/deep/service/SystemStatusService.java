package in.ac.daiict.deep.service;

import in.ac.daiict.deep.dto.SystemStatusDto;

import java.util.List;

public interface SystemStatusService {
    void updateOnOpeningRegistration(SystemStatusDto systemStatusDto);
    void updateOnExtendingRegistrationPeriod(SystemStatusDto systemStatusDto);
    void updateOnClosingRegistration(SystemStatusDto systemStatusDto);
    void autoCloseRegistration();
    SystemStatusDto fetchAllStatus();
}
