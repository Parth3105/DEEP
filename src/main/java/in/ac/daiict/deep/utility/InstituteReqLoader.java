package in.ac.daiict.deep.utility;

import in.ac.daiict.deep.dto.InstituteReqDto;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InstituteReqLoader {

    private XSSFWorkbook instReqWorkbook;
    private XSSFSheet instReqSheet;

    public InstituteReqLoader(InputStream instReqData) {
        try {
            this.instReqWorkbook=new XSSFWorkbook(instReqData);
            this.instReqSheet=instReqWorkbook.getSheetAt(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<InstituteReqDto> getInstituteRequirements() {
        List<InstituteReqDto> instituteRequirements = new ArrayList<>();

        Iterator<Row> instituteReqIterator = instReqSheet.rowIterator();
        InstituteReqSheetHeader instituteReqHeader = new InstituteReqSheetHeader(instReqSheet.getRow(instReqSheet.getFirstRowNum()));

        instituteReqIterator.next();
        while (instituteReqIterator.hasNext()) {
            Row row = instituteReqIterator.next();
            String program = row.getCell(instituteReqHeader.PROGRAM).getStringCellValue();
            int semester = (int) row.getCell(instituteReqHeader.SEMESTER).getNumericCellValue();
            String category = row.getCell(instituteReqHeader.CATEGORY).getStringCellValue();
            int count = (int) row.getCell(instituteReqHeader.COUNT).getNumericCellValue();

            instituteRequirements.add(new InstituteReqDto(program, category, semester, count));
        }

        return instituteRequirements;
    }
}
