package in.ac.daiict.deep.controller.admin;

import in.ac.daiict.deep.constant.database.DBConstants;
import in.ac.daiict.deep.constant.response.ResponseMessage;
import in.ac.daiict.deep.constant.response.ResponseStatus;
import in.ac.daiict.deep.constant.uploads.UploadConstants;
import in.ac.daiict.deep.constant.endpoints.AdminEndpoint;
import in.ac.daiict.deep.constant.template.AdminTemplate;
import in.ac.daiict.deep.entity.Upload;
import in.ac.daiict.deep.service.*;
import in.ac.daiict.deep.config.DBConfig;
import in.ac.daiict.deep.dto.ResponseDto;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class AllocationInstanceController {
    private StudentService studentService;
    private CourseService courseService;
    private InstituteReqService instituteReqService;
    private CourseOfferingService courseOfferingService;
    private UploadService uploadService;
    private DBConfig schemaSetupService;

    @PostMapping(AdminEndpoint.CREATE_ALLOCATION_INSTANCE)
    public String initiateSetup(@RequestParam String season, @RequestParam String Year){
        if(DBConstants.SAVE_SCHEMA_NAME !=null) {
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

    @PostMapping(AdminEndpoint.SUBMIT_DATA)
    public String saveUploadedFiles(@RequestParam(UploadConstants.STUDENT_DATA) MultipartFile studentData , @RequestParam(UploadConstants.COURSE_DATA) MultipartFile courseData, @RequestParam(UploadConstants.INST_REQ_DATA) MultipartFile instReqData, @RequestParam(UploadConstants.OFFERS_DATA) MultipartFile courseOfferingData, RedirectAttributes redirectAttributes){
        AtomicReference<ResponseDto> errorStatus=new AtomicReference<>(null);
        AtomicReference<ResponseDto> warningStatus=new AtomicReference<>(null);
        AtomicInteger cnt=new AtomicInteger(0);
        Thread u1=new Thread(new Runnable() {
            @Override
            public void run() {
                if(!studentData.isEmpty()){
                    try {
                        ResponseDto status = studentService.insertAll(studentData.getBytes());
                        if(status.getStatus()!= ResponseStatus.OK) errorStatus.set(status);
                        else cnt.set(cnt.get()+1);
                    } catch (IOException e) {
                        redirectAttributes.addFlashAttribute("uploadError", new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.INTERNAL_SERVER_ERROR));
                    }
                }
//                System.out.println("\n\nFinished uploading .......\n\n");
            }
        });
        Thread u2=new Thread(new Runnable() {
            @Override
            public void run() {
                boolean offersUploadedOnce=uploadService.checkIfExists(UploadConstants.OFFERS_DATA);
                boolean isCoursesUploaded=false;
                boolean isOffersUploaded=false;
                if(!courseData.isEmpty()){
                    try {
                        ResponseDto status = courseService.insertAll(courseData.getBytes());
                        if(status.getStatus()!= ResponseStatus.OK) errorStatus.set(status);
                        else {
                            cnt.set(cnt.get() + 1);
                            isCoursesUploaded = true;
                        }
                    } catch (IOException e) {
                        redirectAttributes.addFlashAttribute("uploadError", new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.INTERNAL_SERVER_ERROR));
                    }
                }
                if(!courseOfferingData.isEmpty()){
                    try {
                        ResponseDto status = courseOfferingService.insertAll(courseOfferingData.getBytes());
                        if(status.getStatus()!= ResponseStatus.OK) errorStatus.set(status);
                        else {
                            cnt.set(cnt.get() + 1);
                            isOffersUploaded = true;
                        }
                    } catch (IOException e) {
                        redirectAttributes.addFlashAttribute("uploadError", new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.INTERNAL_SERVER_ERROR));
                    }
                }
                if(isCoursesUploaded && !isOffersUploaded && offersUploadedOnce) warningStatus.set(new ResponseDto(ResponseStatus.WARNING, List.of(ResponseMessage.UPLOAD_OFFERS)));
            }
        });
        Thread u3=new Thread(new Runnable() {
            @Override
            public void run() {
                if(!instReqData.isEmpty()){
                    try {
                        ResponseDto status = instituteReqService.insertAll(instReqData.getBytes());
                        if(status.getStatus()!= ResponseStatus.OK) errorStatus.set(status);
                        else cnt.set(cnt.get()+1);
                    } catch (IOException e) {
                        redirectAttributes.addFlashAttribute("uploadError", new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.INTERNAL_SERVER_ERROR));
                    }
                }
            }
        });
        Thread u4=new Thread(new Runnable() {
            @Override
            public void run() {
                List<Upload> uploads=new ArrayList<>();
                try {
                    if (!studentData.isEmpty()) uploads.add(new Upload(UploadConstants.STUDENT_DATA, studentData.getBytes()));
                    if(!courseData.isEmpty()) uploads.add(new Upload(UploadConstants.COURSE_DATA,courseData.getBytes()));
                    if(!courseOfferingData.isEmpty()) uploads.add(new Upload(UploadConstants.OFFERS_DATA,courseOfferingData.getBytes()));
                    if(!instReqData.isEmpty()) uploads.add(new Upload(UploadConstants.INST_REQ_DATA,instReqData.getBytes()));
                    if(!uploads.isEmpty()) uploadService.insertAll(uploads);
                } catch (IOException e) {
                    redirectAttributes.addFlashAttribute("uploadError", new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.INTERNAL_SERVER_ERROR));
                }
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
