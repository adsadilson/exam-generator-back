package br.com.apssystem.exam_generator.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "application_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUser extends AbstracEntity {

    @NotEmpty(message = "The field username cannot be empty")
    @Column(unique = true, length = 25)
    private String username;
    @NotEmpty(message = "The field password cannot be empty")
    private String password;
    @OneToOne
    private Professor professor;
    private String roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ApplicationUser user = (ApplicationUser) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(professor, user.professor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, password, professor);
    }
}
