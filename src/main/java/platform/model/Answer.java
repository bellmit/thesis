package platform.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private long id;

    @Column(name = "questionnaire_id", nullable = false)
    private long questionnaireId;

    @Column(name = "question_id", nullable = false)
    private long questionId;

    @NotNull
    private String text;

//    @ManyToOne
//    @JoinColumn(name = "fixed_answer_id", nullable = false)
//    private FixedAnswer fixedAnswer;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(long questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    //    public FixedAnswer getFixedAnswer() {
//        return fixedAnswer;
//    }
//
//    public void setFixedAnswer(FixedAnswer fixedAnswer) {
//        this.fixedAnswer = fixedAnswer;
//    }

}
