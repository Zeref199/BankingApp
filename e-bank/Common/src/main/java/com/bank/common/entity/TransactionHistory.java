package com.bank.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "transaction_history")
@Data
public class TransactionHistory extends IdBaseEntity{
    @Column(name = "transaction_type", nullable = false)
    private String transactionType;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "source")
    private String source;

    @Column(name = "status")
    private String status;

    @Column(name = "reason_code")
    private String reasonCode;

    @Column(name = "created_at")
    private String createdAt;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
