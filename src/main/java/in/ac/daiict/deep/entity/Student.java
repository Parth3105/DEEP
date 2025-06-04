package in.ac.daiict.deep.entity;

import in.ac.daiict.deep.constant.DBConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = DBConstants.STUDENT_TABLE)
public class Student {
    @Id
    @Column(length = 12)
    private String sid;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 10, nullable = false)
    private String program;

    @Column(nullable = false)
    private int semester;
}
