package platform.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class ConsentForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consent_form_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "experiment_def_id", nullable = false)
    private ExperimentDefinition experimentDefinition;

    @NotNull
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ExperimentDefinition getExperimentDefinition() {
        return experimentDefinition;
    }

    public void setExperimentDefinition(ExperimentDefinition experimentDefinition) {
        this.experimentDefinition = experimentDefinition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
