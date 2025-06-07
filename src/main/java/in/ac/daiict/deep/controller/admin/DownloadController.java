package in.ac.daiict.deep.controller.admin;

import in.ac.daiict.deep.constant.downloads.AllocationReportNames;
import in.ac.daiict.deep.constant.downloads.DownloadConstants;
import in.ac.daiict.deep.constant.uploads.UploadConstants;
import in.ac.daiict.deep.constant.uploads.UploadFileNames;
import in.ac.daiict.deep.constant.endpoints.AdminEndpoint;
import in.ac.daiict.deep.constant.response.ResponseMessage;
import in.ac.daiict.deep.constant.response.ResponseStatus;
import in.ac.daiict.deep.constant.template.AdminTemplate;
import in.ac.daiict.deep.entity.AllocationReport;
import in.ac.daiict.deep.entity.Upload;
import in.ac.daiict.deep.service.AllocationReportService;
import in.ac.daiict.deep.service.UploadService;
import in.ac.daiict.deep.utility.Response;
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
    public void downloadFile(HttpServletResponse response, @PathVariable("semester") int semester, @PathVariable("name") String name, Model model) throws IOException {
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
        if(contentType == null) response.sendError(HttpServletResponse.SC_NOT_FOUND);
        AllocationReport allocationReport=allocationReportService.fetchReport(downloadFilename,semester);
        if(allocationReport==null) model.addAttribute("downloadResponse",new Response(ResponseStatus.NOT_FOUND, ResponseMessage.DOWNLOAD_RESULTS_NOT_FOUND));
        else {
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFilename + "\"");
            response.getOutputStream().write(allocationReport.getFile());
            response.getOutputStream().flush();
        }
    }

    @GetMapping(AdminEndpoint.DOWNLOAD_UPLOADED_REPORT_SUBMIT)
    public void downloadFile(HttpServletResponse response, @PathVariable("name") String name, Model model) throws IOException {
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
        if(contentType == null) response.sendError(HttpServletResponse.SC_NOT_FOUND);
        Upload uploadData=uploadService.findFile(downloadFilename);
        if(uploadData==null) model.addAttribute("downloadResponse",new Response(ResponseStatus.NOT_FOUND, ResponseMessage.UPLOAD_DATA_NOT_FOUND));
        else {
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFilename + "\"");
            response.getOutputStream().write(uploadData.getFile());
            response.getOutputStream().flush();
        }
    }
}
