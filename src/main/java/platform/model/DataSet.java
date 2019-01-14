package platform.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class DataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "data_set_id")
    private long id;

//    @ManyToOne
//    @JoinColumn(name="experiment_id", nullable=false)
    @Column(name="experiment_id", nullable=false)
    private long experimentId;

//    @ManyToOne
//    @JoinColumn(name="dpuser_id", nullable=false)
    @Column(name="dpuser_id", nullable=false)
    private long dpUserId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(long experimentId) {
        this.experimentId = experimentId;
    }

    public long getDpUserId() {
        return dpUserId;
    }

    public void setDpUserId(long dpUserId) {
        this.dpUserId = dpUserId;
    }
}
