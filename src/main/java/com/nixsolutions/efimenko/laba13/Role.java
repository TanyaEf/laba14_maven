package com.nixsolutions.efimenko.laba13;

public class Role {

    private Long id;
    private String name;

    public Role() {
        this(0, "");
    }

    public Role(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(long newId) {
        this.id = newId;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }
}
