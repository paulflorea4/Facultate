package org.example.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Entity<ID> {
    private ID id;
}
