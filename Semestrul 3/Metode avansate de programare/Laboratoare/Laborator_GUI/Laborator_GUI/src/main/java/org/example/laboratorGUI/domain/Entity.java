package org.example.laboratorGUI.domain;

public class Entity<ID> {
    private ID id;

    public Entity(ID id) {
        this.id = id;
    }

    protected ID getId() {
        return id;
    }

    public void setId(ID newID) {
        id = newID;
    }
}
