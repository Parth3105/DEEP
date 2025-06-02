package in.ac.daiict.deep.utility;

import in.ac.daiict.deep.dto.StudentDto;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StudentLoader {
    private XSSFWorkbook studentWorkbook;
    private XSSFSheet studentSheet;
    private DecimalFormat formatStudentID;
    public StudentLoader(InputStream studentData) {
        try {
            formatStudentID = new DecimalFormat("#");
            this.studentWorkbook = new XSSFWorkbook(studentData);
            this.studentSheet= studentWorkbook.getSheetAt(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * @return the required studentData from the sheet in the map format where student_id is key and Student Object as data.
     */
    public List<StudentDto> getStudentData() {
        List<StudentDto> studentDtos = new ArrayList<>();

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

        return studentDtos;
    }

}
