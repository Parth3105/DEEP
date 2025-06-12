package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.entity.InstanceName;
import in.ac.daiict.deep.repository.InstanceNameRepo;
import in.ac.daiict.deep.service.InstanceNameService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@AllArgsConstructor
public class InstanceNameServiceImpl implements InstanceNameService {

    private InstanceNameRepo instanceNameRepo;


    @Override
    public String fetchLatestInstance() {
        InstanceName instanceName=instanceNameRepo.findTopByOrderByCreatedAtDesc().orElse(null);
        if(instanceName==null) return null;
        return instanceName.getInstanceName();
    }

    @Override
    public boolean checkIfNewInstanceExists(String newInstanceName) {
        return instanceNameRepo.existsById(newInstanceName);
    }

    @Override
    public boolean insertNewInstance(String newInstanceName) {
        InstanceName instanceName=instanceNameRepo.save(new InstanceName(newInstanceName));
        return instanceName != null && instanceName.getInstanceName().equals(newInstanceName);
    }

    @Override
    public boolean migrateInstances() {
        File file=new File("C:/flyway-scripts/V4__Instance_Migration_Script.sql");
        try {
            if(!file.exists()) file.createNewFile();
            PrintWriter printWriter=new PrintWriter(new FileWriter(file,false));
            printWriter.println("-- WARNING: This file is critical to application configuration.\n" +
                    "-- Manual changes are strongly discouraged as they may lead to system instability or configuration errors.");
            List<InstanceName> instanceNameList=instanceNameRepo.findAll();
            for(InstanceName instanceName:instanceNameList){
                printWriter.println("INSERT INTO instance_names(instance_name,created_at) VALUES ('"+instanceName.getInstanceName()+"','"+instanceName.getCreatedAt()+"');");
                printWriter.flush();
            }
            printWriter.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void deleteInstance(String instanceName) {
        instanceNameRepo.deleteById(instanceName);
    }
}
