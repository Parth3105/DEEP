package in.ac.daiict.deep.controller.admin;

import in.ac.daiict.deep.constant.database.DBConstants;
import in.ac.daiict.deep.constant.uploads.UploadFileNames;
import in.ac.daiict.deep.constant.response.ResponseMessage;
import in.ac.daiict.deep.constant.response.ResponseStatus;
import in.ac.daiict.deep.constant.uploads.UploadConstants;
import in.ac.daiict.deep.constant.endpoints.AdminEndpoint;
import in.ac.daiict.deep.constant.template.AdminTemplate;
import in.ac.daiict.deep.entity.Upload;
import in.ac.daiict.deep.service.*;
import in.ac.daiict.deep.config.DBConfig;
import in.ac.daiict.deep.dto.ResponseDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Controller
public class AllocationInstanceController {
    private StudentService studentService;
    private CourseService courseService;
    private InstituteReqService instituteReqService;
    private CourseOfferingService courseOfferingService;
    private UploadService uploadService;
    private DBConfig schemaSetupService;
    private JdbcTemplate jdbcTemplate;

    public AllocationInstanceController(StudentService studentService, CourseService courseService, InstituteReqService instituteReqService, CourseOfferingService courseOfferingService, UploadService uploadService, DBConfig schemaSetupService, JdbcTemplate jdbcTemplate) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.instituteReqService = instituteReqService;
        this.courseOfferingService = courseOfferingService;
        this.uploadService = uploadService;
        this.schemaSetupService = schemaSetupService;
        this.jdbcTemplate = jdbcTemplate;
    }

    private Map<String, Upload> uploads=null;
    private boolean offersUploadedOnce;
    @PostMapping(AdminEndpoint.CREATE_ALLOCATION_INSTANCE)
    public String initiateSetup(@RequestParam String season, @RequestParam String Year){
        uploads=new HashMap<>();
        offersUploadedOnce=false;
        if(DBConstants.SAVE_SCHEMA_NAME !=null) {
            String sql = String.format("ALTER SCHEMA %s RENAME TO %s", DBConstants.WORKING_SCHEMA_NAME, DBConstants.SAVE_SCHEMA_NAME);
            jdbcTemplate.execute(sql);
            schemaSetupService.createSchemaAndSwitch(DBConstants.WORKING_SCHEMA_NAME);
        }
        DBConstants.SAVE_SCHEMA_NAME= season+"_"+Year;
        return "redirect:"+AdminEndpoint.UPDATE_INSTANCE;
    }

    @GetMapping(AdminEndpoint.UPDATE_INSTANCE)
    public String renderUploadPage(Model model){
        if(DBConstants.SAVE_SCHEMA_NAME == null) return "redirect:/admin-dashboard";

        Map<String,Long> uploadStatus=new TreeMap<>();
        uploadStatus.put("Semester 5",studentService.countBySemester(5));
        uploadStatus.put("Semester 6",studentService.countBySemester(6));
        uploadStatus.put("Semester 7",studentService.countBySemester(7));
        uploadStatus.put("Semester 8",studentService.countBySemester(8));
        model.addAttribute("uploadStatus",uploadStatus);
        return AdminTemplate.UPDATE_INSTANCE_PAGE;
    }

    @PostMapping("/upload/{type}")
    @ResponseBody
    public void loadFile(@RequestParam("file") MultipartFile file, @PathVariable("type") String name){
        String[] names={UploadConstants.STUDENT_DATA,UploadConstants.COURSE_DATA,UploadConstants.INST_REQ_DATA,UploadConstants.OFFERS_DATA};
        String[] fileNames={UploadFileNames.STUDENT_DATA,UploadFileNames.COURSE_DATA,UploadFileNames.INST_REQ_DATA,UploadFileNames.OFFERS_DATA};
        try {
            for (int j=0;j<names.length;j++) {
                if (names[j].equals(name)) {
                    uploads.put(names[j], new Upload(fileNames[j], file.getBytes()));
                    break;
                }
            }
        } catch (IOException e) {
            // error handling.
        }
    }
    @PostMapping(AdminEndpoint.SUBMIT_DATA)
    public String saveUploadedFiles(RedirectAttributes redirectAttributes){
        /* debug
        for (String name : names) {
            if (!uploads.containsKey(name)) {
                System.out.println("Not exist: "+name);
                return "admin/update-instance";
            }
        }
        */

        AtomicReference<ResponseDto> errorStatus=new AtomicReference<>(null);
        AtomicReference<ResponseDto> warningStatus=new AtomicReference<>(null);
        AtomicInteger cnt=new AtomicInteger(0);
        Thread u1=new Thread(new Runnable() {
            @Override
            public void run() {
                if(uploads.containsKey(UploadConstants.STUDENT_DATA)){
                    ResponseDto status=studentService.insertAll(uploads.get(UploadConstants.STUDENT_DATA).getFile());
                    if(status.getStatus()!= ResponseStatus.OK) errorStatus.set(status);
                    else cnt.set(cnt.get()+1);
                }
                System.out.println("\n\nFinished uploading .......\n\n");
            }
        });
        Thread u2=new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isCoursesUploaded=false;
                boolean isOffersUploaded=false;
                if(uploads.containsKey(UploadConstants.COURSE_DATA)){
                    ResponseDto status=courseService.insertAll(uploads.get(UploadConstants.COURSE_DATA).getFile());
                    if(status.getStatus()!= ResponseStatus.OK) errorStatus.set(status);
                    else {
                        cnt.set(cnt.get() + 1);
                        isCoursesUploaded = true;
                    }
                }
                if(uploads.containsKey(UploadConstants.OFFERS_DATA)){
                    ResponseDto status=courseOfferingService.insertAll(uploads.get(UploadConstants.OFFERS_DATA).getFile());
                    if(status.getStatus()!= ResponseStatus.OK) errorStatus.set(status);
                    else {
                        cnt.set(cnt.get() + 1);
                        offersUploadedOnce = true;
                        isOffersUploaded = true;
                    }
                }
                if(isCoursesUploaded && !isOffersUploaded && offersUploadedOnce) warningStatus.set(new ResponseDto(ResponseStatus.WARNING, List.of(ResponseMessage.UPLOAD_OFFERS)));
            }
        });
        Thread u3=new Thread(new Runnable() {
            @Override
            public void run() {
                if(uploads.containsKey(UploadConstants.INST_REQ_DATA)){
                    ResponseDto status=instituteReqService.insertAll(uploads.get(UploadConstants.INST_REQ_DATA).getFile());
                    if(status.getStatus()!= ResponseStatus.OK) errorStatus.set(status);
                    else cnt.set(cnt.get()+1);
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
            uploads.clear();
        } catch (InterruptedException e) {
            // handle error
            throw new RuntimeException(e);
        }
        if(cnt.get()==0){
            ResponseDto warnings=warningStatus.get();
            if(warnings==null) warnings=new ResponseDto(ResponseStatus.WARNING,new ArrayList<>());
            warnings.addWarning(ResponseMessage.NO_FILES_UPLOADED);
            warningStatus.set(warnings);
        }
        if(errorStatus.get()!=null) redirectAttributes.addFlashAttribute("uploadError",errorStatus.get());
        else if(warningStatus.get()!=null) redirectAttributes.addFlashAttribute("uploadWarning",warningStatus.get());
        if(cnt.get()>0) {
            ResponseMessage.UPLOAD_COUNT=cnt.get();
            redirectAttributes.addFlashAttribute("uploadSuccess", new ResponseDto(ResponseStatus.OK, ResponseMessage.getUploadSuccessMessage()));
        }

        return "redirect:"+AdminEndpoint.UPDATE_INSTANCE;
    }

}
