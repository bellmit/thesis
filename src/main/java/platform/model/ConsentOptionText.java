package platform.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class ConsentOptionText {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consent_option_text_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "consent_option_id", nullable = false)
    private ConsentOption consentOption;

    @ManyToOne
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @NotNull
    private String text;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ConsentOption getConsentOption() {
        return consentOption;
    }

    public void setConsentOption(ConsentOption consentOption) {
        this.consentOption = consentOption;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
