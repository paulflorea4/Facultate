package org.example.template.domain;

public class Placeholder extends Entity<Integer>{
    private String placeholder;

    public Placeholder(Integer integer, String placeholder) {
        super(integer);
        this.placeholder = placeholder;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }
}
