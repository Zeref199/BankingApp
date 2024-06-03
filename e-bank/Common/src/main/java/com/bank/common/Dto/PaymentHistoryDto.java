package com.bank.common.Dto;

import lombok.Data;

@Data
public class PaymentHistoryDto {
    private String beneficiary;
    private String beneficiary_acc_no;
    private double amount;
    private String status;
    private String reference_no;
    private String reason_code;
}
