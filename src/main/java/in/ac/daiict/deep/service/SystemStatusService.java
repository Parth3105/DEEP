package in.ac.daiict.deep.service;

import in.ac.daiict.deep.dto.SystemStatusDto;
import in.ac.daiict.deep.entity.SystemStatus;

public interface SystemStatusService {
    void updateOnOpeningRegistration(SystemStatusDto systemStatusDto);
    void updateOnExtendingRegistrationPeriod(SystemStatusDto systemStatusDto);
    void updateOnClosingRegistration();
    void autoCloseRegistration();
    SystemStatusDto fetchAllStatus();
    String fetchRegistrationStatus();
    String fetchResultStatus();
}
