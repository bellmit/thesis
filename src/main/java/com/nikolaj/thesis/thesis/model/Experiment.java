package com.nikolaj.thesis.thesis.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Experiment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experiment_id")
    private long id;

    private String name;

    @Column(name="experiment_def_id", nullable=false)
    private long experimentDefinitionId;

    @Column(name="dpuser_id", nullable=false)
    private long dataSubjectId;

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

    public long getExperimentDefinitionId() {
        return experimentDefinitionId;
    }

    public void setExperimentDefinitionId(long experimentDefinitionId) {
        this.experimentDefinitionId = experimentDefinitionId;
    }

    public long getDataSubjectId() {
        return dataSubjectId;
    }

    public void setDataSubjectId(long dataSubjectId) {
        this.dataSubjectId = dataSubjectId;
    }
}
