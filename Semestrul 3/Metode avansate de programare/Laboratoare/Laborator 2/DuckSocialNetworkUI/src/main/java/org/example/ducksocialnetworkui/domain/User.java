package org.example.ducksocialnetworkui.domain;

import org.example.ducksocialnetworkui.event.Event;

public abstract class User extends Entity<Long> {
    protected String username;
    protected String email;
    protected String password;

    public User(Long id, String username, String email, String password) {
        super(id);
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }


    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", username='" + username + '\'' +
                ", tip=" + getClass().getSimpleName() +
                '}';
    }

    public void receiveNotification(Event e,String message) {
        System.out.println(username + " a primit notificare pentru evenimentul " + e.getName()+": " + message);
    }
}
