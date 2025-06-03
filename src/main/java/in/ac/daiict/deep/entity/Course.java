package in.ac.daiict.deep.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "courses")
public class Course {
    @Id
    private String cid;

    @Column
    private String name;

    @Column
    private Integer credits;

    @Column
    private String slot;
}
