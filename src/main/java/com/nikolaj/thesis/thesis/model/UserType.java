package com.nikolaj.thesis.thesis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.squareup.moshi.Json;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usertype")
public class UserType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usertype_id")
    private long id;

    @NotNull
    private String type;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private Set<DPUser> dpusers;



    public UserType() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Set<DPUser> getDpusers() {
        return dpusers;
    }

    public void setDpusers(Set<DPUser> dpusers) {
        this.dpusers = dpusers;
    }
}
