package in.ac.daiict.deep.util.dataloader.impl;

import in.ac.daiict.deep.constant.response.ResponseMessage;
import in.ac.daiict.deep.constant.response.ResponseStatus;
import in.ac.daiict.deep.dto.CourseDto;
import in.ac.daiict.deep.dto.CourseOfferingDto;
import in.ac.daiict.deep.dto.InstituteReqDto;
import in.ac.daiict.deep.entity.*;
import in.ac.daiict.deep.service.AllocationResultService;
import in.ac.daiict.deep.service.CourseService;
import in.ac.daiict.deep.dto.ResponseDto;
import in.ac.daiict.deep.util.allocation.model.AllocationCourse;
import in.ac.daiict.deep.util.allocation.model.AllocationStudent;
import in.ac.daiict.deep.util.allocation.model.CourseOffer;
import in.ac.daiict.deep.util.dataloader.DataLoader;
import in.ac.daiict.deep.util.dataloader.headers.*;
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
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class ExcelDataLoader implements DataLoader {
    private CourseService courseService;
    private AllocationResultService allocationResultService;

    @Autowired
    @Lazy
    public ExcelDataLoader(CourseService courseService, AllocationResultService allocationResultService) {
        this.courseService = courseService;
        this.allocationResultService = allocationResultService;
    }

    /**
     * Load the STUDENT_DATA from the sheet.
     */
    public ResponseDto getStudentData(InputStream studentData, List<Student> students) {
        XSSFWorkbook studentWorkbook = null;
        XSSFSheet studentSheet = null;
        try {
            studentWorkbook = new XSSFWorkbook(studentData);
            studentSheet = studentWorkbook.getSheetAt(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DecimalFormat formatStudentID = new DecimalFormat("#");
        Iterator<Row> studentIterator = studentSheet.rowIterator();
        StudentSheetHeader studentHeader = new StudentSheetHeader(studentSheet.getRow(studentSheet.getFirstRowNum()));

        // Extracting the Data from the sheet.
        studentIterator.next();
        while (studentIterator.hasNext()) {
            Row studentRow = studentIterator.next();
            String studentID = "";
            if(studentRow.getCell(studentHeader.STUDENT_ID).getCellType().equals(CellType.NUMERIC)) studentID=formatStudentID.format(studentRow.getCell(studentHeader.STUDENT_ID).getNumericCellValue());
            else if(studentRow.getCell(studentHeader.STUDENT_ID).getCellType().equals(CellType.STRING)) studentID=studentRow.getCell(studentHeader.STUDENT_ID).getStringCellValue();
            else{
                System.out.println(studentRow.getCell(studentHeader.STUDENT_ID).getCellType().name());
            }
            String studentName = studentRow.getCell(studentHeader.NAME).getStringCellValue();
            String program = studentRow.getCell(studentHeader.PROGRAM).getStringCellValue();
            int semester = (int) studentRow.getCell(studentHeader.SEMESTER).getNumericCellValue();
            students.add(new Student(studentID, studentName, program, semester));
        }
        try {
            studentWorkbook.close();
        } catch (IOException e) {
            return new ResponseDto(ResponseStatus.OK, "Student Data Saved Successfully!");
        }
        return new ResponseDto(ResponseStatus.OK, "Student Data Saved Successfully!");
    }

    /**
     * Load the COURSE_DATA from the sheet.
     */
    public ResponseDto getCourseData(InputStream courseData, List<Course> courses) {
        XSSFWorkbook courseWorkbook;
        XSSFSheet courseSheet;
        try {
            courseWorkbook = new XSSFWorkbook(courseData);
            courseSheet = courseWorkbook.getSheetAt(0);
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

            courses.add(new Course(courseID, courseName, credits, slot));
        }
        try {
            courseWorkbook.close();
        } catch (IOException e) {
            return new ResponseDto(ResponseStatus.OK, "Student Data Saved Successfully!");
        }
        return new ResponseDto(ResponseStatus.OK, "Course Data Saved Successfully!");
    }

    /**
     * Load the institute-requirements from the sheet.
     */
    public ResponseDto getInstituteRequirements(InputStream instReqData, List<InstituteReq> instituteReqs) {
        XSSFWorkbook instReqWorkbook;
        XSSFSheet instReqSheet;
        try {
            instReqWorkbook = new XSSFWorkbook(instReqData);
            instReqSheet = instReqWorkbook.getSheetAt(0);
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

            instituteReqs.add(new InstituteReq(program, semester, category, count));
        }
        try {
            instReqWorkbook.close();
        } catch (IOException e) {
            return new ResponseDto(ResponseStatus.OK, "Student Data Saved Successfully!");
        }
        return new ResponseDto(ResponseStatus.OK, "Requirements Saved Successfully!");
    }

    /**
     * Load the course-offering Data from the sheet.
     */
    public ResponseDto getCourseForProgram(InputStream offerData, List<CourseOffering> courseOfferings) {
        XSSFWorkbook offerWorkbook;
        XSSFSheet offerSheet;
        try {
            offerWorkbook = new XSSFWorkbook(offerData);
            offerSheet = offerWorkbook.getSheetAt(0);
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

            if (!courseService.isPresent(courseID)) {
                courseOfferings.clear();
                return new ResponseDto(ResponseStatus.BAD_REQUEST, ResponseMessage.DB_SAVE_ERROR);
            }
            courseOfferings.add(new CourseOffering(program, courseID, semester, category, seats));
        }
        try {
            offerWorkbook.close();
        } catch (IOException e) {
            return new ResponseDto(ResponseStatus.OK, "Student Data Saved Successfully!");
        }
        return new ResponseDto(ResponseStatus.OK, "Course Offerings Data Saved Successfully!");
    }

    @Override
    public ByteArrayOutputStream createStudentPrefSheet(List<CoursePref> coursePrefList, List<SlotPref> slotPrefList) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XSSFWorkbook outputWorkbook = new XSSFWorkbook();

        Font generalFont = outputWorkbook.createFont();
        generalFont.setFontHeightInPoints((short) 12);

        CellStyle generalStyle = outputWorkbook.createCellStyle();
        generalStyle.setFont(generalFont);

        // Prepare sheet for course preferences
        XSSFSheet coursePrefSheet = outputWorkbook.createSheet("CoursePreferences");
        CoursePrefSheetHeader coursePrefHeader = new CoursePrefSheetHeader(outputWorkbook, coursePrefSheet);

        int entryNum = 1;
        Row row = coursePrefSheet.getRow(coursePrefSheet.getFirstRowNum());
        for (CoursePref coursePref : coursePrefList) {
            row = coursePrefSheet.createRow(entryNum++);
            Cell cell = row.createCell(coursePrefHeader.STUDENT_ID, CellType.STRING);
            cell.setCellValue(coursePref.getSid());
            cell.setCellStyle(generalStyle);

            cell = row.createCell(coursePrefHeader.SLOT, CellType.STRING);
            cell.setCellValue(coursePref.getSlot());
            cell.setCellStyle(generalStyle);

            cell = row.createCell(coursePrefHeader.COURSE_ID, CellType.STRING);
            cell.setCellValue(coursePref.getCid());
            cell.setCellStyle(generalStyle);

            cell = row.createCell(coursePrefHeader.PREFERENCE_INDEX, CellType.NUMERIC);
            cell.setCellValue(coursePref.getPref());
            cell.setCellStyle(generalStyle);
        }
        for (int j = 0; j <= row.getLastCellNum(); j++) coursePrefSheet.autoSizeColumn(j);

        // Prepare sheet for slot preferences
        XSSFSheet slotPrefSheet = outputWorkbook.createSheet("SlotPreferences");
        SlotPrefSheetHeader slotPrefHeader = new SlotPrefSheetHeader(outputWorkbook, slotPrefSheet);

        entryNum = 1;
        row = slotPrefSheet.getRow(slotPrefSheet.getFirstRowNum());
        for (SlotPref slotPref : slotPrefList) {
            row = slotPrefSheet.createRow(entryNum++);
            Cell cell = row.createCell(slotPrefHeader.STUDENT_ID, CellType.STRING);
            cell.setCellValue(slotPref.getSid());
            cell.setCellStyle(generalStyle);

            cell = row.createCell(slotPrefHeader.SLOT_NO, CellType.STRING);
            cell.setCellValue(slotPref.getSlot());
            cell.setCellStyle(generalStyle);

            cell = row.createCell(slotPrefHeader.PREFERENCE_INDEX, CellType.NUMERIC);
            cell.setCellValue(slotPref.getPref());
            cell.setCellStyle(generalStyle);
        }
        for (int j = 0; j <= row.getLastCellNum(); j++) slotPrefSheet.autoSizeColumn(j);

        try {
            outputWorkbook.write(byteArrayOutputStream);
        } catch (IOException e) {
            return null;
        }
        try {
            outputWorkbook.close();
        } catch (IOException e) {
            return null;
        }
        return byteArrayOutputStream;
    }

    public ByteArrayOutputStream createResultSheet(Map<String, AllocationStudent> students, Map<String, AllocationCourse> courses, Map<String, Map<String, String>> courseCategories) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
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
            return null;
        }
        try {
            outputWorkbook.close();
        } catch (IOException e) {
            return null;
        }
        return byteArrayOutputStream;
    }

    public ByteArrayOutputStream createSeatSummary(List<CourseOffer> openFor, Map<String, AllocationCourse> courses, Map<String, Map<String, Integer>> availableSeats) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
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
            return null;
        }
        try {
            outputWorkbook.close();
        } catch (IOException e) {
            return null;
        }
        return byteArrayOutputStream;
    }

    public ByteArrayOutputStream createCourseWiseAllocation(Map<String, AllocationCourse> courses, Map<String, AllocationStudent> students) {
        int STUDENT_ID = 0, STUDENT_NAME = 1;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        Set<String> courseIds = courses.keySet();
        for (String cid : courseIds) {
            List<AllocationResult> allocationResultList = allocationResultService.fetchCourseWiseAllocation(cid);
            XSSFWorkbook outputWorkbook = new XSSFWorkbook();
            XSSFSheet sheet = outputWorkbook.createSheet("AllocatedStudents");

            CourseWiseSheetHeader courseWiseSheetHeader = new CourseWiseSheetHeader(outputWorkbook, sheet);

            Font generalFont = outputWorkbook.createFont();
            generalFont.setFontHeightInPoints((short) 12);

            CellStyle generalStyle = outputWorkbook.createCellStyle();
            generalStyle.setFont(generalFont);

            int entryNum = 1;
            Row row = sheet.getRow(sheet.getFirstRowNum());
            for (AllocationResult allocationResult : allocationResultList) {
                row = sheet.createRow(entryNum++);

                Cell cell = row.createCell(courseWiseSheetHeader.STUDENT_ID, CellType.STRING);
                cell.setCellValue(allocationResult.getSid());
                cell.setCellStyle(generalStyle);

                cell = row.createCell(courseWiseSheetHeader.STUDENT_NAME, CellType.STRING);
                cell.setCellValue(students.get(allocationResult.getSid()).getName());
                cell.setCellStyle(generalStyle);

                // debug
                //System.out.println("CID: "+allocationResult.getCid()+"SID: "+allocationResult.getSid());
            }

            for (int j = 0; j <= row.getLastCellNum(); j++) sheet.autoSizeColumn(j);
            addToZip(zipOutputStream, outputWorkbook, cid + "_Students.xlsx");

            try {
                outputWorkbook.close();
            } catch (IOException e) {
                return null;
            }
        }

        try {
            zipOutputStream.close();
        } catch (IOException e) {
            return null;
        }
        return byteArrayOutputStream;
    }

    private void addToZip(ZipOutputStream zipOutputStream, XSSFWorkbook workbook, String fileName) {
        try {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOutputStream.putNextEntry(zipEntry);
            workbook.write(zipOutputStream);
            zipOutputStream.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
