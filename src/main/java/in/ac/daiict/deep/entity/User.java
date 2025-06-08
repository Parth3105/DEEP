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
@Table(name = "users")
public class User {
    @Id
    @Column(length = 12)
    private String username;
    @Column(length = 500)
    private String password;
    @Column(length = 100)
    private String email;
    @Column(length = 50)
    private String role;
}
