package com.bank.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "payments")
@Data
public class Payment extends IdBaseEntity{

    @Column(name = "beneficiary")
    private String beneficiary;

    @Column(name = "beneficiary_acc_no")
    private String beneficiaryAccNo;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "status")
    private String status;

    @Column(name = "reference_no")
    private String referenceNo;

    @Column(name = "reason_code")
    private String reasonCode;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

}
