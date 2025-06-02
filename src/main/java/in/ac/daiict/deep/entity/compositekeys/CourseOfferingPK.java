package in.ac.daiict.deep.entity.compositekeys;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CourseOfferingPK {
    private String cid;
    private String program;
}
