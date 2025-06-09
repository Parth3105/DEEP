package in.ac.daiict.deep.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentReqDto {
    @NotBlank
    @Pattern(regexp = "^\\d+$", message = "Must contain only digits")
    private String sid;
    @NotBlank
    private String category;
    @PositiveOrZero
    private int courseCnt;

    public StudentReqDto(String sid, String category) {
        this.sid = sid;
        this.category=category;
    }
}
