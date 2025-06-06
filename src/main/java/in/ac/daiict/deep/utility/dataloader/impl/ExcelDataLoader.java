package in.ac.daiict.deep.utility.dataloader.impl;

import in.ac.daiict.deep.constant.response.ResponseStatus;
import in.ac.daiict.deep.dto.CourseDto;
import in.ac.daiict.deep.dto.CourseOfferingDto;
import in.ac.daiict.deep.dto.InstituteReqDto;
import in.ac.daiict.deep.dto.StudentDto;
import in.ac.daiict.deep.service.CourseService;
import in.ac.daiict.deep.utility.Response;
import in.ac.daiict.deep.utility.allocation.model.AllocationCourse;
import in.ac.daiict.deep.utility.allocation.model.AllocationStudent;
import in.ac.daiict.deep.utility.allocation.model.CourseOffer;
import in.ac.daiict.deep.utility.dataloader.DataLoader;
import in.ac.daiict.deep.utility.dataloader.headers.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

        return new Response(ResponseStatus.OK,"Student Data Saved Successfully!");
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

        return new Response(ResponseStatus.OK,"Course Data Saved Successfully!");
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

        return new Response(ResponseStatus.OK,"Requirements Saved Successfully!");
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
                return new Response(ResponseStatus.BAD_REQUEST,"Error: Some entries refer to non-existing course in course-offerings. Please verify your data.");
            }
            courseOfferingDtos.add(new CourseOfferingDto(program, courseID, category, semester, seats));
        }

        return new Response(ResponseStatus.OK,"Course Offerings Data Saved Successfully!");
    }

    public ByteArrayOutputStream createResultSheet(Map<String, AllocationStudent> students, Map<String, AllocationCourse> courses, Map<String, Map<String, String>> courseCategories) {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        XSSFWorkbook outputWorkbook = new XSSFWorkbook();
        XSSFSheet resultSheet = outputWorkbook.createSheet("AllocationResults");
        ResultSheetHeader resultHeader = new ResultSheetHeader(outputWorkbook, resultSheet);

        Font generalFont = outputWorkbook.createFont();
        generalFont.setFontHeightInPoints((short) 12);

        CellStyle generalStyle = outputWorkbook.createCellStyle();
        generalStyle.setFont(generalFont);

        int entryNum = 1;
        Row row = resultSheet.getRow(resultSheet.getFirstRowNum());
        for (AllocationStudent student : students.values()) {
            for (String courseID : student.getAllocatedCourses()) {
                AllocationCourse course = courses.get(courseID);
                String category = courseCategories.get(courseID).get(student.getProgram());

                row = resultSheet.createRow(entryNum++);
                Cell cell = row.createCell(resultHeader.STUDENT_ID, CellType.STRING);
                cell.setCellValue(student.getSid());
                cell.setCellStyle(generalStyle);

                cell = row.createCell(resultHeader.PROGRAM, CellType.STRING);
                cell.setCellValue(student.getProgram());
                cell.setCellStyle(generalStyle);

                cell = row.createCell(resultHeader.SEMESTER, CellType.NUMERIC);
                cell.setCellValue(student.getSemester());
                cell.setCellStyle(generalStyle);

                cell = row.createCell(resultHeader.COURSE_ID, CellType.STRING);
                cell.setCellValue(courseID);
                cell.setCellStyle(generalStyle);

                cell = row.createCell(resultHeader.COURSE_NAME, CellType.STRING);
                cell.setCellValue(course.getName());
                cell.setCellStyle(generalStyle);

                cell = row.createCell(resultHeader.CATEGORY, CellType.STRING);
                cell.setCellValue(category);
                cell.setCellStyle(generalStyle);

                cell = row.createCell(resultHeader.SLOT, CellType.STRING);
                cell.setCellValue(course.getSlot());
                cell.setCellStyle(generalStyle);

                cell = row.createCell(resultHeader.PRIORITY, CellType.NUMERIC);
                cell.setCellValue(student.getPriority());
                cell.setCellStyle(generalStyle);

                cell = row.createCell(resultHeader.CUMULATIVE_PRIORITY, CellType.NUMERIC);
                cell.setCellValue(student.getCumulativePriority());
                cell.setCellStyle(generalStyle);
            }
        }

        for (int j = 0; j <= row.getLastCellNum(); j++) resultSheet.autoSizeColumn(j);
        try {
            outputWorkbook.write(byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream;
    }

    public ByteArrayOutputStream createSeatSummary(List<CourseOffer> openFor, Map<String, AllocationCourse> courses, Map<String, Map<String, Integer>> availableSeats) {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        XSSFWorkbook outputWorkbook = new XSSFWorkbook();
        XSSFSheet seatSummarySheet = outputWorkbook.createSheet("AvailableSeat Summary");
        SeatSummarySheetHeader seatHeader = new SeatSummarySheetHeader(outputWorkbook, seatSummarySheet);

        Font generalFont = outputWorkbook.createFont();
        generalFont.setFontHeightInPoints((short) 12);

        CellStyle generalStyle = outputWorkbook.createCellStyle();
        generalStyle.setFont(generalFont);

        int entryNum = 1;
        Row row = seatSummarySheet.getRow(seatSummarySheet.getFirstRowNum());
        for (CourseOffer of : openFor) {
            AllocationCourse course = courses.get(of.getCid());
            row = seatSummarySheet.createRow(entryNum++);

            Cell cell = row.createCell(seatHeader.COURSE_ID, CellType.STRING);
            cell.setCellValue(of.getCid());
            cell.setCellStyle(generalStyle);

            cell = row.createCell(seatHeader.COURSE_NAME, CellType.STRING);
            cell.setCellValue(course.getName());
            cell.setCellStyle(generalStyle);

            cell = row.createCell(seatHeader.PROGRAM, CellType.STRING);
            cell.setCellValue(of.getProgram());
            cell.setCellStyle(generalStyle);

            cell = row.createCell(seatHeader.SEMESTER, CellType.NUMERIC);
            cell.setCellValue(of.getSemester());
            cell.setCellStyle(generalStyle);


            cell = row.createCell(seatHeader.CATEGORY, CellType.STRING);
            cell.setCellValue(of.getCategory());
            cell.setCellStyle(generalStyle);

            cell = row.createCell(seatHeader.AVAILABLE_SEATS, CellType.NUMERIC);
            cell.setCellValue(availableSeats.get(of.getProgram()).get(of.getCid()));
            cell.setCellStyle(generalStyle);
        }

        for (int j = 0; j <= row.getLastCellNum(); j++) seatSummarySheet.autoSizeColumn(j);

        try {
            outputWorkbook.write(byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream;
    }
}
