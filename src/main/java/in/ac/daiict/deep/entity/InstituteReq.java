package in.ac.daiict.deep.entity;

import in.ac.daiict.deep.constant.database.DBConstants;
import in.ac.daiict.deep.entity.compositekeys.InstituteReqPK;
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
@Table(name= DBConstants.INST_REQ_TABLE)
@IdClass(InstituteReqPK.class)
public class InstituteReq {
    @Id
    @Column(length = 10)
    private String program;
    @Id
    private int semester;
    @Id
    @Column(length = 10)
    private String category;
    @Column(name = "course_cnt", nullable = false)
    private int courseCnt;
}
