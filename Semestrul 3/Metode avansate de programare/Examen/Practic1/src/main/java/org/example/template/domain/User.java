package org.example.template.domain;

public class User extends Entity<Long>{
    private String username;
    private String password;
    private Rol rol;

    public User(Long aLong, String username, String password,Rol rol) {
        super(aLong);
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
