package in.ac.daiict.deep.entity;

import in.ac.daiict.deep.entity.compositekeys.CoursePrefPK;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "course_prefs")
@IdClass(CoursePrefPK.class)
public class CoursePref {
    @Id
    @Column(length = 12)
    private String sid;
    @Id
    @Column(length = 4)
    private String slot;
    @Id
    private int pref;
    @Column(length = 10)
    private String cid;
}
