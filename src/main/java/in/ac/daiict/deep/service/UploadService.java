package in.ac.daiict.deep.service;

import in.ac.daiict.deep.entity.Upload;

import java.util.List;
import java.util.Map;

public interface UploadService {
    void insertAll(Map<String,Upload> uploads);
    List<Upload> getAll();
    void deleteAll();
}
