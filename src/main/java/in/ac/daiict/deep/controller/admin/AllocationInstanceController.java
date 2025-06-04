package in.ac.daiict.deep.controller.admin;

import in.ac.daiict.deep.constant.DBConstants;
import in.ac.daiict.deep.constant.ResponseConstants;
import in.ac.daiict.deep.constant.UploadConstants;
import in.ac.daiict.deep.entity.Upload;
import in.ac.daiict.deep.service.*;
import in.ac.daiict.deep.config.DBConfig;
import in.ac.daiict.deep.utility.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@AllArgsConstructor
public class AllocationInstanceController {
    private StudentService studentService;
    private CourseService courseService;
    private InstituteReqService instituteReqService;
    private CourseOfferingService courseOfferingService;
    private UploadService uploadService;
    private DBConfig schemaSetupService;
    private JdbcTemplate jdbcTemplate;

    private Map<String, Upload> uploads=null;
    @PostMapping("/create-instance")
    public String initiateSetup(@RequestParam String season, @RequestParam String Year, Model model){
        uploads=new HashMap<>();
        if(DBConstants.SAVE_SCHEMA_NAME !=null) {
            String sql = String.format("ALTER SCHEMA %s RENAME TO %s", DBConstants.WORKING_SCHEMA_NAME, DBConstants.SAVE_SCHEMA_NAME);
            jdbcTemplate.execute(sql);
            schemaSetupService.createSchemaAndSwitch(DBConstants.WORKING_SCHEMA_NAME);
        }
        DBConstants.SAVE_SCHEMA_NAME= season+"_"+Year;
        return "redirect:/update-instance";
    }

    @GetMapping("/update-instance")
    public String renderUploadPage(Model model){
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
    public void loadFile(@RequestParam("file") MultipartFile file, @PathVariable("type") String name){
        String[] names={UploadConstants.studentData,UploadConstants.courseData,UploadConstants.instReqData,UploadConstants.offeringData};
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
    public String saveUploadedFiles(RedirectAttributes redirectAttributes){
        /* debug
        for (String name : names) {
            if (!uploads.containsKey(name)) {
                System.out.println("Not exist: "+name);
                return "admin/update-instance";
            }
        }
        */

        AtomicReference<Response> errorStatus=new AtomicReference<>(null);
        Thread u1=new Thread(new Runnable() {
            @Override
            public void run() {
                if(uploads.containsKey(UploadConstants.studentData)){
                    Response status=studentService.insertAll(uploads.get(UploadConstants.studentData).getFile());
                    if(status.getStatus()!= ResponseConstants.OK) errorStatus.set(status);
                }
                System.out.println("\n\nFinished uploading .......\n\n");
            }
        });
        Thread u2=new Thread(new Runnable() {
            @Override
            public void run() {
                if(uploads.containsKey(UploadConstants.courseData)){
                    Response status=courseService.insertAll(uploads.get(UploadConstants.courseData).getFile());
                    if(status.getStatus()!= ResponseConstants.OK) errorStatus.set(status);
                }
                if(uploads.containsKey(UploadConstants.offeringData)){
                    Response status=courseOfferingService.insertAll(uploads.get(UploadConstants.offeringData).getFile());
                    if(status.getStatus()!= ResponseConstants.OK) errorStatus.set(status);
                }
            }
        });
        Thread u3=new Thread(new Runnable() {
            @Override
            public void run() {
                if(uploads.containsKey(UploadConstants.instReqData)){
                    Response status=instituteReqService.insertAll(uploads.get(UploadConstants.instReqData).getFile());
                    if(status.getStatus()!= ResponseConstants.OK) errorStatus.set(status);
                }
            }
        });
        Thread u4=new Thread(new Runnable() {
            @Override
            public void run() {
                if(!uploads.isEmpty()) uploadService.insertAll(uploads);
            }
        });
        u1.start();
        u2.start();
        u3.start();
        u4.start();
        try {
            u2.join();
            u3.join();
            u4.join();
            u1.join();
        } catch (InterruptedException e) {
            // handle error
            throw new RuntimeException(e);
        }
        if(errorStatus.get()!=null) redirectAttributes.addFlashAttribute("uploadResponse",errorStatus.get());
        else redirectAttributes.addFlashAttribute("uploadResponse",new Response(ResponseConstants.OK,"You're all set! Your records have been successfully uploaded and saved."));
        return "redirect:/update-instance";
    }

}
