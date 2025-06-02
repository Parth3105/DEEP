package in.ac.daiict.deep.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "uploads")
public class Upload {
    public Upload(String name, byte[] file) {
        this.name = name;
        this.file = file;
    }

    @Id
    String name;
    @Column
    byte[] file;
}
