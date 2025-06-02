package in.ac.daiict.deep.utility;

import in.ac.daiict.deep.dto.CourseDto;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CourseLoader {
    private XSSFWorkbook courseWorkbook;
    private XSSFSheet courseSheet;
    public CourseLoader(InputStream courseData) {
        try {
            this.courseWorkbook = new XSSFWorkbook(courseData);
            this.courseSheet= courseWorkbook.getSheetAt(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public List<CourseDto> getCourseData() {
        List<CourseDto> courses = new ArrayList<>();

        Iterator<Row> courseIterator = courseSheet.iterator();
        CourseSheetHeader courseHeader = new CourseSheetHeader(courseSheet.getRow(courseSheet.getFirstRowNum()));

        courseIterator.next();
        while (courseIterator.hasNext()) {
            Row row = courseIterator.next();
            String courseID = row.getCell(courseHeader.COURSE_ID).getStringCellValue();
            String courseName = row.getCell(courseHeader.COURSE_NAME).getStringCellValue();
            int credits = (int) row.getCell(courseHeader.CREDITS).getNumericCellValue();
            String slot = String.valueOf((int) row.getCell(courseHeader.SLOT).getNumericCellValue());

            courses.add(new CourseDto(courseID, courseName, credits, slot));
        }

        return courses;
    }
}
