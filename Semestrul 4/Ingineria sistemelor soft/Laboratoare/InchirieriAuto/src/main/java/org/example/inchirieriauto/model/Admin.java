package org.example.inchirieriauto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admins")
@Getter @Setter @NoArgsConstructor
public class Admin extends User {

    @Column(nullable = false,  length = 50)
    private String name;
}
