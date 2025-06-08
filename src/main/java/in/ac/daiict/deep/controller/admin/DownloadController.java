package in.ac.daiict.deep.controller.admin;

import in.ac.daiict.deep.constant.downloads.AllocationReportNames;
import in.ac.daiict.deep.constant.downloads.DownloadConstants;
import in.ac.daiict.deep.constant.uploads.UploadConstants;
import in.ac.daiict.deep.constant.uploads.UploadFileNames;
import in.ac.daiict.deep.constant.endpoints.AdminEndpoint;
import in.ac.daiict.deep.constant.response.ResponseMessage;
import in.ac.daiict.deep.constant.response.ResponseStatus;
import in.ac.daiict.deep.entity.AllocationReport;
import in.ac.daiict.deep.entity.Upload;
import in.ac.daiict.deep.service.AllocationReportService;
import in.ac.daiict.deep.service.UploadService;
import in.ac.daiict.deep.dto.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.*;

@Controller
@AllArgsConstructor
public class DownloadController {
    private AllocationReportService allocationReportService;
    private UploadService uploadService;

    @GetMapping(AdminEndpoint.DOWNLOAD_REPORT_SUBMIT)
    public void downloadReport(HttpServletResponse httpServletResponse, @PathVariable("semester") int semester, @PathVariable("name") String name, Model model) {
        String contentType=null;
        String downloadFilename=null;
        switch (name) {
            case DownloadConstants.ALLOCATION_RESULTS -> {
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                downloadFilename = AllocationReportNames.ALLOCATION_RESULT;
            }
            case DownloadConstants.SEAT_SUMMARY -> {
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                downloadFilename = AllocationReportNames.SEAT_SUMMARY;
            }
            case DownloadConstants.FAILURE_LOG -> {
                contentType = "text/plain";
                downloadFilename = AllocationReportNames.ALLOCATION_FAILURE_LOG;
            }
            case DownloadConstants.COURSE_WISE_ALLOCATION -> {
                contentType = "application/zip";
                downloadFilename = AllocationReportNames.COURSE_WISE_ALLOCATION;
            }
        }
        if(contentType == null){
            model.addAttribute("downloadResponse",new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.DOWNLOADING_ERROR));
            return;
        }
        AllocationReport allocationReport=allocationReportService.fetchReport(downloadFilename,semester);
        if(allocationReport==null) model.addAttribute("downloadResponse",new ResponseDto(ResponseStatus.NOT_FOUND, ResponseMessage.DOWNLOAD_RESULTS_NOT_FOUND));
        else {
            httpServletResponse.setContentType(contentType);
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFilename + "\"");
            try {
                httpServletResponse.getOutputStream().write(allocationReport.getFile());
                httpServletResponse.getOutputStream().flush();
            } catch (IOException e) {
                model.addAttribute("downloadResponse",new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.DOWNLOADING_ERROR));
            }
        }
    }

    @GetMapping(AdminEndpoint.DOWNLOAD_UPLOADED_REPORT_SUBMIT)
    public void downloadUploadedData(HttpServletResponse httpServletResponse, @PathVariable("name") String name, Model model){
        String contentType=null;
        String downloadFilename=null;
        String[] names={UploadConstants.COURSE_DATA,UploadConstants.INST_REQ_DATA,UploadConstants.OFFERS_DATA};
        String[] fileNames={UploadFileNames.COURSE_DATA,UploadFileNames.INST_REQ_DATA,UploadFileNames.OFFERS_DATA};
        for (int j=0;j<names.length;j++) {
            if (names[j].equals(name)) {
                contentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                downloadFilename=fileNames[j];
            }
        }
        if(contentType == null){
            model.addAttribute("downloadResponse",new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.DOWNLOADING_ERROR));
            return;
        }
        Upload uploadData=uploadService.findFile(downloadFilename);
        if(uploadData==null) model.addAttribute("downloadResponse",new ResponseDto(ResponseStatus.NOT_FOUND, ResponseMessage.UPLOAD_DATA_NOT_FOUND));
        else {
            httpServletResponse.setContentType(contentType);
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFilename + "\"");
            try {
                httpServletResponse.getOutputStream().write(uploadData.getFile());
                httpServletResponse.getOutputStream().flush();
            } catch (IOException e) {
                model.addAttribute("downloadResponse",new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR,ResponseMessage.DOWNLOADING_ERROR));
            }
        }
    }
}
