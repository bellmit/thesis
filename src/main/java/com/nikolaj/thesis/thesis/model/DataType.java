package com.nikolaj.thesis.thesis.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class DataType {
    @Id
    @GeneratedValue
    @Column(name = "data_type_id")
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
//    @NotNull
//    private ArrayList<String> Format;



    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "datatype")
    private List<DeviceType> devicetype;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public ArrayList<String> getFormat() {
//        return Format;
//    }
//
//    public void setFormat(ArrayList<String> format) {
//        Format = format;
//    }


//    @Override
//    public String toString() {
//
//        StringBuilder sb = new StringBuilder();
//        for (String s: Format
//             ) {
//            sb.append(s);
//        }
//
//        return "Datatype{" +
//                "id=" + id +
//                ", description='" + description + '\'' + ", format='" + sb + '\'' +
//                '}';
//    }
}
