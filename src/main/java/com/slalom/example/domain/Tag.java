package com.slalom.example.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Tag {

    private Long id;

    private String name;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @Size(min=2, max=100) @Pattern(regexp = "^[a-zA-Z0-9_ ]*$", message="Invalid Tag Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
