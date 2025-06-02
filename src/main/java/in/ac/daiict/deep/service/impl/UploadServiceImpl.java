package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.entity.Upload;
import in.ac.daiict.deep.repository.UploadRepo;
import in.ac.daiict.deep.service.UploadService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class UploadServiceImpl implements UploadService {
    private UploadRepo uploadRepo;

    @Override
    public void insertAll(Map<String, Upload> uploads) {
        List<Upload> uploadList = new ArrayList<>(uploads.values());
        uploadRepo.saveAllAndFlush(uploadList);
    }

    @Override
    public List<Upload> getAll() {
        return List.of();
    }

    @Override
    public void deleteAll() {
        uploadRepo.deleteAll();
    }
}
