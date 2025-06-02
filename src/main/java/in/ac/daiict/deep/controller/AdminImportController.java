package in.ac.daiict.deep.controller;

import in.ac.daiict.deep.entity.Upload;
import in.ac.daiict.deep.service.StudentService;
import in.ac.daiict.deep.service.UploadService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Controller
@AllArgsConstructor
public class AdminImportController {
    private StudentService studentService;
    private UploadService uploadService;

    Map<String, Upload> uploads;
    private void initiateStorage(){
        uploads=new HashMap<>();
    }
    @PostMapping("/upload/{type}")
    public void loadFile(@RequestParam("file") MultipartFile file, @PathVariable("type") String name, @Value("${upload.file}") String fileNames){
        if(uploads==null) initiateStorage();

        String[] names=fileNames.split(",");
        try {
            for(int j=0;j<names.length;j++) {
                if (names[j].equalsIgnoreCase(name)) uploads.put(names[j], new Upload(names[j], file.getBytes()));
            }
        } catch (IOException e) {
            // error handling.
        }
    }
    @PostMapping("/submit")
    public void saveUploadedFiles(){
        uploadService.insertAll(uploads);
    }

}
