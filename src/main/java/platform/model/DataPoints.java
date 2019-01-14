package platform.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
public class DataPoints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "data_points_id")
    private long id;

    @Column(name="data_def_id", nullable=false)
    private long dataDefinitionId;

    @Column(name="experiment_id", nullable=false)
    private long experimentId;

    @NotNull
    private String measurementName;

    @NotNull
    private Date lastUpdated;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDataDefinitionId() {
        return dataDefinitionId;
    }

    public void setDataDefinitionId(long dataDefinitionId) {
        this.dataDefinitionId = dataDefinitionId;
    }

    public long getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(long experimentId) {
        this.experimentId = experimentId;
    }

    public String getMeasurementName() {
        return measurementName;
    }

    public void setMeasurementName(String measurementName) {
        this.measurementName = measurementName;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
