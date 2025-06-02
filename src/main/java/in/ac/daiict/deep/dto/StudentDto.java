package in.ac.daiict.deep.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentDto {
    @NotBlank
    @Pattern(regexp = "^\\d+$", message = "Must contain only digits")
    private String sid;
    @Pattern(regexp = "^[A-Za-z]+$", message = "Must contain only letters")
    private String name;
    @NotBlank
    private String program;
    @Positive @Max(10)
    private int semester;
}
