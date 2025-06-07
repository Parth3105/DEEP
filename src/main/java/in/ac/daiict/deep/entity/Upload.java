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
@Table(name = DBConstants.UPLOAD_TABLE)
public class Upload {
    @Id
    @Column(length = 100)
    private String name;
    @Lob
    @Column(name = "data", columnDefinition="bytea")
    private byte[] file;
}
