package in.ac.daiict.deep.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseDto {
    private int status;
    private String message;
    private List<String> warnings;

    public ResponseDto(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseDto(int status, List<String> warnings) {
        this.status = status;
        this.warnings = warnings;
    }

    public void addWarning(String warning){
        this.warnings.add(warning);
    }
}
