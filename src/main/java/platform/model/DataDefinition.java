package platform.model;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "data_def")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class DataDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "data_def_id")
    private long id;

//    @ManyToOne
//    @JoinColumn(name="experiment_def_id", nullable=false)
    @NotNull
    @Column(name="experiment_def_id", nullable=false)
    private long experimentDefinitionId;

    @NotNull
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type( type = "pgsql_enum" )
    private DataSensitivity sensitivity;

    @NotNull
    private long dataTypeId;

    private long linesToSkip;

    private String columnDelimiter;

    private String cellDelimiter;

    private long timestampColumn;

    private String timestampFormat;

    private String timestampUnit;

//    @NotNull
//    @Enumerated(EnumType.STRING)
//    @Type( type = "pgsql_enum" )
//    private DataSensitivity dataType;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataSensitivity getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(DataSensitivity sensitivity) {
        this.sensitivity = sensitivity;
    }

    public long getDataTypeId() {
        return dataTypeId;
    }

    public void setDataTypeId(long dataTypeId) {
        this.dataTypeId = dataTypeId;
    }

    public long getLinesToSkip() {
        return linesToSkip;
    }

    public void setLinesToSkip(long linesToSkip) {
        this.linesToSkip = linesToSkip;
    }

    public String getColumnDelimiter() {
        return columnDelimiter;
    }

    public void setColumnDelimiter(String columnDelimiter) {
        this.columnDelimiter = columnDelimiter;
    }

    public String getCellDelimiter() {
        return cellDelimiter;
    }

    public void setCellDelimiter(String cellDelimiter) {
        this.cellDelimiter = cellDelimiter;
    }

    public long getTimestampColumn() {
        return timestampColumn;
    }

    public void setTimestampColumn(long timestampColumn) {
        this.timestampColumn = timestampColumn;
    }

    public String getTimestampFormat() {
        return timestampFormat;
    }

    public void setTimestampFormat(String timestampFormat) {
        this.timestampFormat = timestampFormat;
    }

    public String getTimestampUnit() {
        return timestampUnit;
    }

    public void setTimestampUnit(String timestampUnit) {
        this.timestampUnit = timestampUnit;
    }
}
