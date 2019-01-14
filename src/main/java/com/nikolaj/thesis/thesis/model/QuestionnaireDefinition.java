package com.nikolaj.thesis.thesis.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "questionnaire_def")
public class QuestionnaireDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "questionnaire_def_id")
    private long id;

//    @ManyToOne
//    @JoinColumn(name="experiment_def_id", nullable=false)
    @Column(name="experiment_def_id", nullable=false)
    private long experimentDefinitionId;

    @NotNull
    private String title;

    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getExperimentDefinitionId() {
        return experimentDefinitionId;
    }

    public void setExperimentDefinitionId(long experimentDefinitionId) {
        this.experimentDefinitionId = experimentDefinitionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
