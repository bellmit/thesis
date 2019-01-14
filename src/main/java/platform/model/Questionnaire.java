package platform.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Questionnaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "questionnaire_id")
    private long id;

    @Column(name="data_def_id", nullable=false)
    private long dataDefId;

    @Column(name="experiment_id", nullable=false)
    private long experimentId;

    @Column(name="questionnaire_def_id", nullable=false)
    private long questionnaireDefinitionId;

    @Column(name="timestamp", nullable=false)
    private Date timestamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDataDefId() {
        return dataDefId;
    }

    public void setDataDefId(long dataDefId) {
        this.dataDefId = dataDefId;
    }

    public long getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(long experimentId) {
        this.experimentId = experimentId;
    }

    public long getQuestionnaireDefinitionId() {
        return questionnaireDefinitionId;
    }

    public void setQuestionnaireDefinitionId(long questionnaireDefinition) {
        this.questionnaireDefinitionId = questionnaireDefinition;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
