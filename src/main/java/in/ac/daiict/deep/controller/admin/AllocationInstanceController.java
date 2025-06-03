package in.ac.daiict.deep.controller.admin;

import in.ac.daiict.deep.entity.Database;
import in.ac.daiict.deep.entity.Upload;
import in.ac.daiict.deep.service.*;
import in.ac.daiict.deep.service.impl.InstanceCreationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Controller
@AllArgsConstructor
public class AllocationInstanceController {
    private StudentService studentService;
    private CourseService courseService;
    private InstituteReqService instituteReqService;
    private CourseOfferingService courseOfferingService;
    private UploadService uploadService;
    private InstanceCreationService schemaSetupService;
    private JdbcTemplate jdbcTemplate;

    private Map<String, Upload> uploads=null;
    @PostMapping("/create-instance")
    public String initiateSetup(@RequestParam String season, @RequestParam String Year, Model model){
        uploads=new HashMap<>();
        if(Database.SAVE_SCHEMA_NAME !=null) {
            String sql = String.format("ALTER SCHEMA %s RENAME TO %s", Database.WORKING_SCHEMA_NAME, Database.SAVE_SCHEMA_NAME);
            jdbcTemplate.execute(sql);
            schemaSetupService.createSchemaAndSwitch(Database.WORKING_SCHEMA_NAME);
        }
        Database.SAVE_SCHEMA_NAME= season+"_"+Year;
        return "redirect:/update-instance";
    }

    @GetMapping("/update-instance")
    public String showUploadPage(Model model){
        Map<String,Long> uploadStatus=new TreeMap<>();
        uploadStatus.put("Semester 5",studentService.countBySemester(5));
        uploadStatus.put("Semester 6",studentService.countBySemester(6));
        uploadStatus.put("Semester 7",studentService.countBySemester(7));
        uploadStatus.put("Semester 8",studentService.countBySemester(8));
        model.addAttribute("uploadStatus",uploadStatus);
        return "admin/update-instance";
    }

    @PostMapping("/upload/{type}")
    @ResponseBody
    public void loadFile(@RequestParam("file") MultipartFile file, @PathVariable("type") String name, @Value("${upload.file}") String fileNames){
        String[] names=fileNames.split(",");
        try {
            for(int j=0;j<names.length;j++) {
                if (names[j].toUpperCase().contains(name.toUpperCase())) {
                    uploads.put(names[j], new Upload(names[j], file.getBytes()));
                    break;
                }
            }
        } catch (IOException e) {
            // error handling.
        }
    }
    @PostMapping("/submit-data")
    public String saveUploadedFiles(@Value("${upload.file}") String fileNames){
        String[] names=fileNames.split(",");

        /* debug
        for (String name : names) {
            if (!uploads.containsKey(name)) {
                System.out.println("Not exist: "+name);
                return "admin/update-instance";
            }
        }
        */

        Thread u1=new Thread(new Runnable() {
            @Override
            public void run() {
                if(uploads.containsKey(names[0])) studentService.insertAll(uploads.get(names[0]).getFile());
                System.out.println("\n\nFinished uploading .......\n\n");
            }
        });
        Thread u2=new Thread(new Runnable() {
            @Override
            public void run() {
                if(uploads.containsKey(names[1])) courseService.insertAll(uploads.get(names[1]).getFile());
            }
        });
        Thread u3=new Thread(new Runnable() {
            @Override
            public void run() {
                if(uploads.containsKey(names[2])) instituteReqService.insertAll(uploads.get(names[2]).getFile());
            }
        });
        Thread u4=new Thread(new Runnable() {
            @Override
            public void run() {
                if(uploads.containsKey(names[3])) courseOfferingService.insertAll(uploads.get(names[3]).getFile());
            }
        });
        Thread u5=new Thread(new Runnable() {
            @Override
            public void run() {
                if(uploads.containsKey(names[4]))uploadService.insertAll(uploads);
            }
        });
        u1.start();
        u2.start();
        u3.start();
        u4.start();
        u5.start();
        try {
            u2.join();
            u3.join();
            u4.join();
            u5.join();
            u1.join();
        } catch (InterruptedException e) {
            // handle error
            throw new RuntimeException(e);
        }
        return "redirect:/update-instance";
    }

}
