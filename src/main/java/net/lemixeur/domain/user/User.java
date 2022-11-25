package net.lemixeur.domain.user;

import net.lemixeur.domain.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "USERS")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", nullable = false, updatable = false)
    private Long id;

    @NotNull(message = "L'adresse courriel ne peut être vide.")
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
            message = "L'adresse courriel est invalide.")
    @UniqueEmail(message = "L'adresse courriel est déjà dans notre système.")
    @Column(name = "EMAIL", nullable = false, unique = true, updatable = false, length = 64)
    private String email;

    @NotNull(message = "Le mot de passe ne peut être vide.")
    @Size(min = 8, max = 32, message = "Le mot de passe doit contenir entre 8 et 32 caractères.")
    @Column(name = "PASSWORD", nullable = false, length = 32)
    private String password;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}