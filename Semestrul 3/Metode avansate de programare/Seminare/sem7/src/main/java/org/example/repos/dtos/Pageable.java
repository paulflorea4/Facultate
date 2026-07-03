package org.example.repos.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Pageable {
    private int pageNumber;
    private int pageSize;
}
