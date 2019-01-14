package platform.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "device")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    private long id;


    private String name;

    @NotNull
    private String muid;

    private String Batterylevel;

    @Column(name = "lastsynctime")
    private String lastSyncTime;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id")
    private DeviceType deviceType;



    @ManyToOne
    @JoinColumn(name="data_subject_id", nullable=false)
    private DPUser dataSubjectdevice;


    public Device() {}

    public Device(String name, @NotNull String muid, String batterylevel, String lastSyncTime, DeviceType deviceType) {
        this.name = name;
        this.muid = muid;
        this.Batterylevel = batterylevel;
        this.lastSyncTime = lastSyncTime;
        this.deviceType = deviceType;
    }
    /*
    public Device( @NotNull String muid, String batterylevel, String lastSyncTime, DeviceType deviceType) {
        this.Muid = muid;
        this.Batterylevel = batterylevel;
        this.lastSyncTime = lastSyncTime;
        this.deviceType = deviceType;
    }
*/

    public DPUser getDataSubject() {
        return dataSubjectdevice;
    }

    public void setDataSubject(DPUser dataSubject) {
        this.dataSubjectdevice = dataSubject;
    }

    public String getMuid() {
        return muid;
    }

    public void setMuid(String muid) {
        this.muid = muid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getBatterylevel() {
        return Batterylevel;
    }

    public void setBatterylevel(String batterylevel) {
        Batterylevel = batterylevel;
    }

    public String getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(String lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

}
