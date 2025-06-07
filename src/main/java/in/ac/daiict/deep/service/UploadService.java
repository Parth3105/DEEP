package in.ac.daiict.deep.service;

import in.ac.daiict.deep.entity.Upload;

import java.util.Map;

public interface UploadService {
    void insertAll(Map<String,Upload> uploads);
    Upload findFile(String name);
    void deleteAll();
}
