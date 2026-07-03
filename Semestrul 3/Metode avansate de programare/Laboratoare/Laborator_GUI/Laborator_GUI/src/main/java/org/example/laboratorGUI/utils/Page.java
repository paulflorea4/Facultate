package org.example.laboratorGUI.utils;

public class Page<TElem> {
    private final Iterable<TElem> content;
    private final int totalPages;

    public Page(Iterable<TElem> content, int totalPages) {
        this.content = content;
        this.totalPages = totalPages;
    }

    public Iterable<TElem> getContent() {
        return content;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
