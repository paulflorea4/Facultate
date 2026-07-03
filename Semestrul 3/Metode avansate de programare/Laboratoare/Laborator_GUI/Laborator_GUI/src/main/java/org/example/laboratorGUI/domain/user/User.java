package org.example.laboratorGUI.domain.user;

import org.example.laboratorGUI.domain.Entity;

import java.util.Objects;

public abstract class User extends Entity<Long> {
    private final String username, email;
    private String password;

    public User(Long userID, String username, String email, String password) {
        super(userID);
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getUserID() {
        return super.getId();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User id = " + super.getId() + ", username = " + username + ", email = " + email + ", password = " + password;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(super.getId(), user.getId()) && Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getEmail(), user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId(), getUsername(), getEmail());
    }
}
