package org.example.ducksocialnetworkui.dto;

public class UserDto implements Dto {
    private String type;
    private String duckType;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDuckType() {
        return duckType;
    }

    public void setDuckType(String duckType) {
        this.duckType = duckType;
    }
}
