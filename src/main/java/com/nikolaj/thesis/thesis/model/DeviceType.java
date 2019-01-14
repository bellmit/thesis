package com.nikolaj.thesis.thesis.model;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class DeviceType  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_type_id")
    private long id;

    @NotNull
    private String producer;

    @NotNull
    private String model;

    @NotNull
    private String type;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "devicetype_datatype",
            joinColumns = { @JoinColumn(name = "devicetype_id") },
            inverseJoinColumns = { @JoinColumn(name = "datatype_id") })
    private List<DataType> datatype;


    public DeviceType(@NotNull String producer, @NotNull String model, @NotNull String type) {
        this.producer = producer;
        this.model = model;
        this.type = type;
    }

    public DeviceType() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "DeviceType{" +
                "id=" + id +
                ", producer='" + producer + '\'' + ", model='" + model+ '\'' +
                '}';
    }
}
