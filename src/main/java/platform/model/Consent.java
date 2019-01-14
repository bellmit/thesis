package platform.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Consent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consent_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "consent_option_id", nullable = false)
    private ConsentOption consentOption;

    @ManyToOne
    @JoinColumn(name = "dpuser_id", nullable = false)
    private DPUser dpUser;

    @NotNull
    private boolean givenConsent;

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

    public DPUser getDpUser() {
        return dpUser;
    }

    public void setDpUser(DPUser dpUser) {
        this.dpUser = dpUser;
    }

    public boolean isGivenConsent() {
        return givenConsent;
    }

    public void setGivenConsent(boolean givenConsent) {
        this.givenConsent = givenConsent;
    }
}
