package in.ac.daiict.deep.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InstituteReqDto {
    @NotBlank
    private String program;
    @NotBlank
    private String category;
    @Positive
    @Max(10)
    private int semester;
    @PositiveOrZero
    private int courseCnt;
}
