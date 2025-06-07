package in.ac.daiict.deep.entity;

import in.ac.daiict.deep.constant.database.DBConstants;
import in.ac.daiict.deep.entity.compositekeys.CourseOfferingPK;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = DBConstants.COURSE_OFFERING_TABLE)
@IdClass(CourseOfferingPK.class)
public class CourseOffering {
    @Id
    @Column(length = 10)
    private String program;
    @Id
    @Column(length = 10)
    private String cid;
    @Id
    private int semester;
    @Column(length = 10, nullable = false)
    private String category;
    @Column(nullable = false)
    private int seats;
}
