package in.ac.daiict.deep.entity;

import in.ac.daiict.deep.entity.compositekeys.CourseOfferingPK;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "course_offerings")
@IdClass(CourseOfferingPK.class)
public class CourseOffering {
    @Id
    private String program;
    @Id
    private String cid;
    @Column
    private String category;
    @Column
    private int semester;
    @Column
    private int seats;
}
