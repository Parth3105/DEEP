package in.ac.daiict.deep.utility;

import in.ac.daiict.deep.dto.CourseOfferingDto;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CourseOfferLoader {

    private XSSFWorkbook offerWorkbook;
    private XSSFSheet offerSheet;
    public CourseOfferLoader(InputStream offerData) {
        try {
            this.offerWorkbook = new XSSFWorkbook(offerData);
            this.offerSheet= offerWorkbook.getSheetAt(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CourseOfferingDto> getCourseForProgram() {
        List<CourseOfferingDto> courseOfferingDtos = new ArrayList<>();

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

            courseOfferingDtos.add(new CourseOfferingDto(program, courseID, category, semester, seats));
        }

        return courseOfferingDtos;
    }

}
