package org.example.repos.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Page<E> {
    private Iterable<E> pageElements;
    private int elementCount;
}
