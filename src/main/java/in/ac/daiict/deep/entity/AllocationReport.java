package in.ac.daiict.deep.entity;

import in.ac.daiict.deep.constant.DBConstants;
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
@Table(name = DBConstants.ALLOCATION_REPORT_TABLE)
public class AllocationReport {
    @Id
    @Column(length = 100)
    private String name;
    @Lob
    private byte[] file;
}
