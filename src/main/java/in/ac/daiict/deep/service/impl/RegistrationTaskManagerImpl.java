package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.service.RegistrationTaskManager;
import in.ac.daiict.deep.service.SystemStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.ScheduledFuture;

@Service
public class RegistrationTaskManagerImpl implements RegistrationTaskManager {
    private final TaskScheduler taskScheduler;
    private ScheduledFuture<?> activeRegistrationTask;
    private SystemStatusService systemStatusService;
    private LocalDate closingDate;

    @Autowired
    @Lazy
    public RegistrationTaskManagerImpl(TaskScheduler taskScheduler, SystemStatusService systemStatusService) {
        this.taskScheduler = taskScheduler;
        this.systemStatusService = systemStatusService;
    }

    @Override
    public void updateCloseRegistrationDate(LocalDate closingDate) {
        System.out.println("Updating date to: "+closingDate);
        this.closingDate=closingDate;
    }

    @Override
    public void startRegistration() {
        if(activeRegistrationTask!=null && !activeRegistrationTask.isCancelled()) return;

        /*//debug
        System.out.println("Starting the Registration!");
        activeRegistrationTask=taskScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Starting the task at: "+LocalDate.now()+" AND Closing Date is: "+closingDate);
                if(LocalDate.now().isAfter(closingDate)){
                    closeRegistration();
                }
            }
        },Duration.ofSeconds(30));*/

        activeRegistrationTask=taskScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(LocalDate.now().isAfter(closingDate)){
                    closeRegistration();
                }
            }
        }, Duration.ofDays(1));
    }

    @Override
    public void closeRegistration() {
        System.out.println("Closing the registration");
        if(activeRegistrationTask!=null && !activeRegistrationTask.isCancelled()){
            activeRegistrationTask.cancel(false);
        }
        activeRegistrationTask=null;
        closingDate=null;
        System.out.println("auto closing started...");
        systemStatusService.autoCloseRegistration();
    }
}
