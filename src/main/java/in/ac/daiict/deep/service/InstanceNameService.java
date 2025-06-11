package in.ac.daiict.deep.service;


public interface InstanceNameService {
    String fetchLatestInstance();
    boolean checkIfNewInstanceExists(String newInstanceName);
    boolean insertNewInstance(String newInstanceName);
    boolean migrateInstances();
}
