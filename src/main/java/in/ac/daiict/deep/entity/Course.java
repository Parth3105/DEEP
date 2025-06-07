package in.ac.daiict.deep.entity;

import in.ac.daiict.deep.constant.database.DBConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = DBConstants.COURSE_TABLE)
public class Course {
    @Id
    @Column(length = 10)
    private String cid;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private int credits;

    @Column(length = 4, nullable = false)
    private String slot;
}
