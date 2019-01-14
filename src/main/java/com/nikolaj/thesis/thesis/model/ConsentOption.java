package com.nikolaj.thesis.thesis.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class ConsentOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consent_option_id")
    private long id;

    @ManyToOne
    @JoinColumn(name="consent_form_id", nullable=false)
    private ConsentForm consentForm;

    @NotNull
    private String name;

    @NotNull
    int orderNo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ConsentForm getConsentForm() {
        return consentForm;
    }

    public void setConsentForm(ConsentForm consentForm) {
        this.consentForm = consentForm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }
}
