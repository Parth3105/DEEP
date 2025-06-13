package in.ac.daiict.deep.entity;

import in.ac.daiict.deep.constant.database.DBConstants;
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

    @Column(name = "has_enrolled")
    private boolean hasEnrolled;

    public Student(String sid, String name, String program, int semester) {
        this.sid = sid;
        this.name = name;
        this.program = program;
        this.semester = semester;
    }
}
