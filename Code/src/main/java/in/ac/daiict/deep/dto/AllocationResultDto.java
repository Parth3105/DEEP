package in.ac.daiict.deep.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AllocationResultDto {
    @NotBlank
    private String cid;
    @NotBlank
    private String cname;
    @NotBlank
    private String category;
    @Positive
    private int credits;
}
