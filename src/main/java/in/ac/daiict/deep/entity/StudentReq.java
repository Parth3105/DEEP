package in.ac.daiict.deep.entity;

import in.ac.daiict.deep.constant.database.DBConstants;
import in.ac.daiict.deep.entity.compositekeys.StudentReqPK;
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
@Table(name = DBConstants.STUDENT_REQUIREMENTS_TABLE)
@IdClass(StudentReqPK.class)
public class StudentReq {
    @Id
    @Column(length = 12)
    private String sid;
    @Id
    @Column(length = 10)
    private String category;
    @Column(nullable = false)
    private int course_cnt;

}
