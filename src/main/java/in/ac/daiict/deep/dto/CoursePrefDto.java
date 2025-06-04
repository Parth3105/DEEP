package in.ac.daiict.deep.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CoursePrefDto {
    @NotBlank
    @Pattern(regexp = "^\\d+$", message = "Must contain only digits")
    private String sid;
    @NotBlank
    private String slot;
    @Positive
    private int pref;
    @NotBlank
    private String cid;
}
