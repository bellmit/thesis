package platform.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class FixedAnswerText {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fixed_answer_text_id")
    private long id;

    @ManyToOne
    @JoinColumn(name="fixed_answer_id", nullable=false)
    private FixedAnswer fixedAnswer;

    @ManyToOne
    @JoinColumn(name="language_id", nullable=false)
    private Language language;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FixedAnswer getFixedAnswer() {
        return fixedAnswer;
    }

    public void setFixedAnswer(FixedAnswer fixedAnswer) {
        this.fixedAnswer = fixedAnswer;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
