package com.bank.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "currencies")
public class Currency extends IdBaseEntity{
    @Column(nullable = false, length = 64)
    private String name;
    @Column(nullable = false, length = 3)
    private String symbol;
    @Column(nullable = false, length = 4)
    private String code;

}
