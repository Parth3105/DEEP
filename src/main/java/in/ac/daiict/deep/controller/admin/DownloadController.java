package in.ac.daiict.deep.controller.admin;

import in.ac.daiict.deep.constant.AllocationReportNames;
import in.ac.daiict.deep.constant.DBConstants;
import in.ac.daiict.deep.constant.endpoints.AdminEndpoint;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.*;

@Controller
@AllArgsConstructor
public class DownloadController {
    private JdbcTemplate jdbcTemplate;

//    @GetMapping(AdminEndpoint.DOWNLOAD_REPORTS)
//    public void downloadFile(HttpServletResponse response, @PathVariable("semester") int semester, @PathVariable("filename") String filename) throws IOException {
//        if(filename.equals())
//
//        byte[] file=jdbcTemplate.queryForObject("SELECT file FROM "+ DBConstants.ALLOCATION_REPORT_TABLE +" WHERE name=? and semester=?",byte[].class, fileName);
//
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        response.setHeader("Content-Disposition", "attachment; filename=\"AllocationResult.xlsx\"");
//        response.getOutputStream().write(file);
//        response.getOutputStream().flush();
//    }
}
