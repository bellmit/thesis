package platform.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
public class File /*extends Data*/ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private long id;

//    @ManyToOne
//    @JoinColumn(name="data_def_id", nullable=false)
    @Column(name="data_def_id", nullable=false)
    private long dataDefinitionId;

    @Column(name="experiment_id", nullable=false)
    private long experimentId;

    @NotNull
    private Date uploadDateTime;

    @NotNull
    private int version;

    @NotNull
    private String filePath;

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

    public Date getUploadDateTime() {
        return uploadDateTime;
    }

    public void setUploadDateTime(Date uploadDateTime) {
        this.uploadDateTime = uploadDateTime;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
