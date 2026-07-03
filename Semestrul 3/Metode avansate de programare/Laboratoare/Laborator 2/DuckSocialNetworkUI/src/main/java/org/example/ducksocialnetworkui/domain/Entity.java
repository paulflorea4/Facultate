package org.example.ducksocialnetworkui.domain;

import java.util.Objects;

public abstract class Entity<ID>{
    protected ID id;

    public Entity() {}

    public Entity(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }
    public void setId(ID id) { this.id=id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity<?> entity)) return false;
        return getId().equals(entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
