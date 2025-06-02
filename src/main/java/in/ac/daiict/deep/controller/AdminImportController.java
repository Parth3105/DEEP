package in.ac.daiict.deep.controller;

import in.ac.daiict.deep.dto.StudentDto;
import in.ac.daiict.deep.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class AdminImportController {
    private StudentService studentService;

    private List<MultipartFile> uploadedFiles=null;
    private void initiateFileStorage(){
        uploadedFiles=new ArrayList<>();
    }
    @PostMapping("/upload")
    public void loadFile(@RequestParam("file") MultipartFile file){
        if(uploadedFiles==null) initiateFileStorage();
        uploadedFiles.add(file);
    }
    @PostMapping("/submit")
    public void saveUploadedFiles(){

    }

}
