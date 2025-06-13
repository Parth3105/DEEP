package in.ac.daiict.deep.controller.admin;

import in.ac.daiict.deep.constant.database.DBConstants;
import in.ac.daiict.deep.constant.response.ResponseMessage;
import in.ac.daiict.deep.constant.response.ResponseStatus;
import in.ac.daiict.deep.constant.uploads.UploadConstants;
import in.ac.daiict.deep.constant.endpoints.AdminEndpoint;
import in.ac.daiict.deep.constant.template.AdminTemplate;
import in.ac.daiict.deep.constant.uploads.UploadFileNames;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentSkipListMap;
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
    private InstanceNameService instanceNameService;
    private DBConfig instanceSetupConfig;

    @PostMapping(AdminEndpoint.CREATE_ALLOCATION_INSTANCE)
    public String initiateSetup(@RequestParam String season, @RequestParam String Year, RedirectAttributes redirectAttributes){
        String latestInstanceName=instanceNameService.fetchLatestInstance();
        String newInstanceName=(season+"_"+Year).toLowerCase();
        if(instanceNameService.checkIfNewInstanceExists(newInstanceName)){
            redirectAttributes.addFlashAttribute("instanceCreationError",new ResponseDto(ResponseStatus.CONFLICT, ResponseMessage.INSTANCE_ALREADY_EXISTS));
            return "redirect:"+AdminEndpoint.DASHBOARD;
        }
        boolean canCreate=instanceNameService.insertNewInstance(newInstanceName);
        if(!canCreate){
            redirectAttributes.addFlashAttribute("instanceCreationError",new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.INTERNAL_SERVER_ERROR));
            return "redirect:"+AdminEndpoint.DASHBOARD;
        }
        if(latestInstanceName !=null) {
            if(!instanceSetupConfig.createSchemaAndSwitch(latestInstanceName,DBConstants.WORKING_INSTANCE_NAME)){
                CompletableFuture.runAsync(() -> instanceNameService.deleteInstance(newInstanceName));
                redirectAttributes.addFlashAttribute("instanceCreationError",new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.INTERNAL_SERVER_ERROR));
                return "redirect:"+AdminEndpoint.DASHBOARD;
            }
        }
        return "redirect:"+AdminEndpoint.UPDATE_INSTANCE;
    }

    @GetMapping(AdminEndpoint.UPDATE_INSTANCE)
    public String renderUploadPage(Model model, RedirectAttributes redirectAttributes){
        if(instanceNameService.fetchLatestInstance() == null){
            redirectAttributes.addFlashAttribute("updateInstanceError",new ResponseDto(ResponseStatus.BAD_REQUEST,ResponseMessage.ALLOCATION_INSTANCE_NOT_FOUND));
            return "redirect:"+AdminEndpoint.DASHBOARD;
        }

        Map<String, Long> uploadStatus = new ConcurrentSkipListMap<>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int sem = 5; sem <= 8; sem++) {
            int finalSem = sem;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                long studentCnt = studentService.countBySemester(finalSem);
                uploadStatus.put("Semester " + finalSem, studentCnt);
            });
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        model.addAttribute("uploadStatus",uploadStatus);
        return AdminTemplate.UPDATE_INSTANCE_PAGE;
    }

    @PostMapping(AdminEndpoint.SUBMIT_DATA)
    public String saveUploadedFiles(@RequestParam(UploadConstants.STUDENT_DATA) MultipartFile studentData , @RequestParam(UploadConstants.COURSE_DATA) MultipartFile courseData, @RequestParam(UploadConstants.INST_REQ_DATA) MultipartFile instReqData, @RequestParam(UploadConstants.OFFERS_DATA) MultipartFile courseOfferingData, RedirectAttributes redirectAttributes){
        AtomicReference<ResponseDto> errorStatus=new AtomicReference<>(null);
        AtomicReference<ResponseDto> warningStatus=new AtomicReference<>(null);
        AtomicInteger cnt=new AtomicInteger(0);

        // Upload Student Data.
        CompletableFuture<Void> uploadStudentData=CompletableFuture.runAsync(()->{
            if(!studentData.isEmpty()){
                try {
                    ResponseDto status = studentService.insertAll(studentData.getBytes());
                    if(status.getStatus()!= ResponseStatus.OK) errorStatus.set(status);
                    else cnt.set(cnt.get()+1);
                } catch (IOException e) {
                    redirectAttributes.addFlashAttribute("internalServerError", new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.INTERNAL_SERVER_ERROR));
                }
            }
        });

        // Upload Course Data and Course Offering Data.
        CompletableFuture<Void> uploadCourseAndOfferingData=CompletableFuture.runAsync(() -> {
            boolean offersUploadedOnce=courseOfferingService.existsAnyOffer();
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
                    redirectAttributes.addFlashAttribute("internalServerError", new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.INTERNAL_SERVER_ERROR));
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
                    redirectAttributes.addFlashAttribute("internalServerError", new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.INTERNAL_SERVER_ERROR));
                }
            }
            if(isCoursesUploaded && !isOffersUploaded && offersUploadedOnce) warningStatus.set(new ResponseDto(ResponseStatus.WARNING, ResponseMessage.UPLOAD_OFFERS));
        });

        // Upload Institute Requirements.
        CompletableFuture<Void> uploadInstReqData=CompletableFuture.runAsync(() -> {
            if(!instReqData.isEmpty()){
                try {
                    ResponseDto status = instituteReqService.insertAll(instReqData.getBytes());
                    if(status.getStatus()!= ResponseStatus.OK) errorStatus.set(status);
                    else cnt.set(cnt.get()+1);
                } catch (IOException e) {
                    redirectAttributes.addFlashAttribute("internalServerError", new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.INTERNAL_SERVER_ERROR));
                }
            }
        });

        // Saving uploaded Files.
        CompletableFuture.runAsync(() -> {
            List<Upload> uploads=new ArrayList<>();
            try {
                if (!studentData.isEmpty()) uploads.add(new Upload(UploadFileNames.STUDENT_DATA, studentData.getBytes()));
                if(!courseData.isEmpty()) uploads.add(new Upload(UploadFileNames.COURSE_DATA,courseData.getBytes()));
                if(!courseOfferingData.isEmpty()) uploads.add(new Upload(UploadFileNames.OFFERS_DATA,courseOfferingData.getBytes()));
                if(!instReqData.isEmpty()) uploads.add(new Upload(UploadFileNames.INST_REQ_DATA,instReqData.getBytes()));
                if(!uploads.isEmpty()) uploadService.insertAll(uploads);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("uploadError", new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.INTERNAL_SERVER_ERROR));
            }
        });

        try {
            CompletableFuture.allOf(uploadCourseAndOfferingData, uploadInstReqData, uploadStudentData).join();
        }catch (CompletionException completionException){
            redirectAttributes.addFlashAttribute("internalServerError", new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.INTERNAL_SERVER_ERROR));
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
