package in.ac.daiict.deep.util;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Response {
    private int status;
    private String message;
    private List<String> warnings;

    public Response(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public Response(int status, List<String> warnings) {
        this.status = status;
        this.warnings = warnings;
    }

    public void addWarning(String warning){
        this.warnings.add(warning);
    }
}
