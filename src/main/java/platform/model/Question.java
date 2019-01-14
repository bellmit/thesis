package platform.model;

import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class Question implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private long id;

    @Column(name="questionnaire_def_id", nullable=false)
    private long questionnaireDefinitionId;

    @NotNull
    private String text;

    @NotNull
    private int orderNo;

//    @NotNull
//    @Enumerated(EnumType.STRING)
//    @Type( type = "pgsql_enum" )
//    private QuestionType questionType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuestionnaireDefinitionId() {
        return questionnaireDefinitionId;
    }

    public void setQuestionnaireDefinitionId(long questionnaireDefinitionId) {
        this.questionnaireDefinitionId = questionnaireDefinitionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

//    public QuestionType getQuestionType() {
//        return questionType;
//    }
//
//    public void setQuestionType(QuestionType questionType) {
//        this.questionType = questionType;
//    }
}
