package in.ac.daiict.deep.service;

import in.ac.daiict.deep.entity.Upload;

import java.util.List;
import java.util.Map;

public interface UploadService {
    public void insertAll(Map<String,Upload> uploads);
    public List<Upload> getAll();
    public void deleteAll();
}
