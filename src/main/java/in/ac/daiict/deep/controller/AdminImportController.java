package in.ac.daiict.deep.controller;

import in.ac.daiict.deep.entity.Upload;
import in.ac.daiict.deep.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@AllArgsConstructor
public class AdminImportController {
    private StudentService studentService;
    private CourseService courseService;
    private InstituteReqService instituteReqService;
    private CourseOfferingService courseOfferingService;
    private UploadService uploadService;

    Map<String, Upload> uploads=null;
    private void initiateStorage(){
        uploads=new HashMap<>();
    }
    @GetMapping("/")
    public String showUploadPage(){
        return "admin/update-instance";
    }

    @PostMapping("/upload/{type}")
    @ResponseBody
    public void loadFile(@RequestParam("file") MultipartFile file, @PathVariable("type") String name, @Value("${upload.file}") String fileNames){
        if(uploads==null) initiateStorage();

        String[] names=fileNames.split(",");
        try {
            for(int j=0;j<names.length;j++) {
                if (names[j].toUpperCase().contains(name.toUpperCase())) uploads.put(names[j], new Upload(names[j], file.getBytes()));
            }
        } catch (IOException e) {
            // error handling.
        }
    }
    @PostMapping("/submit-data")
    public String saveUploadedFiles(@Value("upload.file") String fileNames){
        uploadService.insertAll(uploads);
        String[] names=fileNames.split(",");
        studentService.insertAll(uploads.get(names[0]).getFile());

        courseService.insertAll(uploads.get(names[1]).getFile());
        instituteReqService.insertAll(uploads.get(names[2]).getFile());
        courseOfferingService.insertAll(uploads.get(names[3]).getFile());
        return "admin/update-instance";
    }

}
