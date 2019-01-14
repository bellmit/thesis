package com.nikolaj.thesis.thesis.modelResults;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "dataset_file_overview")
public class DatasetFile implements Serializable {
    @Id
    @Column(name = "file_id")
    private long fileId;

    @Column(name = "experiment_id")
    private long experimentId;

    @Column(name = "experiment_def_id")
    private long experimentDefinitionId;

    @Column(name = "data_def_id")
    private long dataDefinitionId;

    @Column(name = "data_def_name")
    private String dataDefinitionName;

    private long versionCount;

    private Date dateUploaded;

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public long getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(long experimentId) {
        this.experimentId = experimentId;
    }

    public long getExperimentDefinitionId() {
        return experimentDefinitionId;
    }

    public void setExperimentDefinitionId(long experimentDefinitionId) {
        this.experimentDefinitionId = experimentDefinitionId;
    }

    public long getDataDefinitionId() {
        return dataDefinitionId;
    }

    public void setDataDefinitionId(long dataDefinitionId) {
        this.dataDefinitionId = dataDefinitionId;
    }

    public String getDataDefinitionName() {
        return dataDefinitionName;
    }

    public void setDataDefinitionName(String dataDefinitionName) {
        this.dataDefinitionName = dataDefinitionName;
    }

    public long getVersionCount() {
        return versionCount;
    }

    public void setVersionCount(long versionCount) {
        this.versionCount = versionCount;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }
}
