package in.ac.daiict.deep.utility.dataloader.impl;

import in.ac.daiict.deep.constant.ResponseConstants;
import in.ac.daiict.deep.dto.CourseDto;
import in.ac.daiict.deep.dto.CourseOfferingDto;
import in.ac.daiict.deep.dto.InstituteReqDto;
import in.ac.daiict.deep.dto.StudentDto;
import in.ac.daiict.deep.service.CourseService;
import in.ac.daiict.deep.utility.Response;
import in.ac.daiict.deep.utility.dataloader.DataLoader;
import in.ac.daiict.deep.utility.dataloader.headers.CourseOfferSheetHeader;
import in.ac.daiict.deep.utility.dataloader.headers.CourseSheetHeader;
import in.ac.daiict.deep.utility.dataloader.headers.InstituteReqSheetHeader;
import in.ac.daiict.deep.utility.dataloader.headers.StudentSheetHeader;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

@Component
public class ExcelDataLoader implements DataLoader {
    private CourseService courseService;

    @Autowired
    @Lazy
    public ExcelDataLoader(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     *  Load the studentData from the sheet.
     */
    public Response getStudentData(InputStream studentData, List<StudentDto> studentDtos) {
        XSSFWorkbook studentWorkbook= null;
        XSSFSheet studentSheet=null;
        try {
            studentWorkbook = new XSSFWorkbook(studentData);
            studentSheet=studentWorkbook.getSheetAt(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DecimalFormat formatStudentID=new DecimalFormat("#");
        Iterator<Row> studentIterator = studentSheet.rowIterator();
        StudentSheetHeader studentHeader = new StudentSheetHeader(studentSheet.getRow(studentSheet.getFirstRowNum()));

        // Extracting the Data from the sheet.
        studentIterator.next();
        while (studentIterator.hasNext()) {
            Row studentRow = studentIterator.next();
            String studentID = formatStudentID.format(studentRow.getCell(studentHeader.STUDENT_ID).getNumericCellValue());
            String studentName = studentRow.getCell(studentHeader.NAME).getStringCellValue();
            String program = studentRow.getCell(studentHeader.PROGRAM).getStringCellValue();
            int semester = (int) studentRow.getCell(studentHeader.SEMESTER).getNumericCellValue();
            studentDtos.add(new StudentDto(studentID, studentName, program, semester));
        }

        return new Response(ResponseConstants.OK,"Student Data Saved Successfully!");
    }

    /**
     * Load the courseData from the sheet.
     */
    public Response getCourseData(InputStream courseData, List<CourseDto> courseDtos) {
        XSSFWorkbook courseWorkbook;
        XSSFSheet courseSheet;
        try {
            courseWorkbook = new XSSFWorkbook(courseData);
            courseSheet= courseWorkbook.getSheetAt(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Iterator<Row> courseIterator = courseSheet.iterator();
        CourseSheetHeader courseHeader = new CourseSheetHeader(courseSheet.getRow(courseSheet.getFirstRowNum()));

        courseIterator.next();
        while (courseIterator.hasNext()) {
            Row row = courseIterator.next();
            String courseID = row.getCell(courseHeader.COURSE_ID).getStringCellValue();
            String courseName = row.getCell(courseHeader.COURSE_NAME).getStringCellValue();
            int credits = (int) row.getCell(courseHeader.CREDITS).getNumericCellValue();
            String slot = String.valueOf((int) row.getCell(courseHeader.SLOT).getNumericCellValue());

            courseDtos.add(new CourseDto(courseID, courseName, credits, slot));
        }

        return new Response(ResponseConstants.OK,"Course Data Saved Successfully!");
    }

    /**
     * Load the institute-requirements from the sheet.
     */
    public Response getInstituteRequirements(InputStream instReqData, List<InstituteReqDto> instituteReqDtos) {
        XSSFWorkbook instReqWorkbook;
        XSSFSheet instReqSheet;
        try {
            instReqWorkbook=new XSSFWorkbook(instReqData);
            instReqSheet=instReqWorkbook.getSheetAt(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Iterator<Row> instituteReqIterator = instReqSheet.rowIterator();
        InstituteReqSheetHeader instituteReqHeader = new InstituteReqSheetHeader(instReqSheet.getRow(instReqSheet.getFirstRowNum()));

        instituteReqIterator.next();
        while (instituteReqIterator.hasNext()) {
            Row row = instituteReqIterator.next();
            String program = row.getCell(instituteReqHeader.PROGRAM).getStringCellValue();
            int semester = (int) row.getCell(instituteReqHeader.SEMESTER).getNumericCellValue();
            String category = row.getCell(instituteReqHeader.CATEGORY).getStringCellValue();
            int count = (int) row.getCell(instituteReqHeader.COUNT).getNumericCellValue();

            instituteReqDtos.add(new InstituteReqDto(program, category, semester, count));
        }

        return new Response(ResponseConstants.OK,"Requirements Saved Successfully!");
    }

    /**
     * Load the course-offering Data from the sheet.
     */
    public Response getCourseForProgram(InputStream offerData, List<CourseOfferingDto> courseOfferingDtos) {
        XSSFWorkbook offerWorkbook;
        XSSFSheet offerSheet;
        try {
            offerWorkbook = new XSSFWorkbook(offerData);
            offerSheet= offerWorkbook.getSheetAt(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Iterator<Row> courseOfferIterator = offerSheet.iterator();
        CourseOfferSheetHeader offerHeader = new CourseOfferSheetHeader(offerSheet.getRow(offerSheet.getFirstRowNum()));

        courseOfferIterator.next();
        while (courseOfferIterator.hasNext()) {
            Row row = courseOfferIterator.next();
            String courseID = row.getCell(offerHeader.COURSE_ID).getStringCellValue();
            String program = row.getCell(offerHeader.PROGRAM).getStringCellValue();
            int semester = (int) row.getCell(offerHeader.SEMESTER).getNumericCellValue();
            String category = row.getCell(offerHeader.CATEGORY).getStringCellValue();
            int seats = (int) row.getCell(offerHeader.SEATS).getNumericCellValue();

            if(!courseService.isPresent(courseID)){
                courseOfferingDtos.clear();
                return new Response(ResponseConstants.BAD_REQUEST,"Error: Some entries refer to non-existing course in course-offerings. Please verify your data.");
            }
            courseOfferingDtos.add(new CourseOfferingDto(program, courseID, category, semester, seats));
        }

        return new Response(ResponseConstants.OK,"Course Offerings Data Saved Successfully!");
    }
}
