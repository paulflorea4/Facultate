package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class User extends Entity<Integer> {
    private String username;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private int credits;
}
