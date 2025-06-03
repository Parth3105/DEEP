package in.ac.daiict.deep.entity;

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
@Table(name="institute_reqs")
@IdClass(InstituteReqPK.class)
public class InstituteReq {
    @Id
    private String program;
    @Id
    private String category;
    @Column
    private int semester;
    @Column
    private int course_cnt;
}
