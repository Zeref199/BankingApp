package com.bank.front.payment;

import com.bank.common.entity.Payment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Modifying
    @Query(value = " INSERT INTO payments(account_id, beneficiary, beneficiary_acc_no, amount, reference_no, status, reason_code, created_at) " +
            "VALUES(:account_id,:beneficiary, :beneficiary_acc_no, :amount, :reference_no, :status, :reason_code, :created_at )", nativeQuery = true)
    @Transactional
    void makePayment(@Param("account_id") int account_id,
                     @Param("beneficiary") String beneficiary,
                     @Param("beneficiary_acc_no") String beneficiary_acc_no,
                     @Param("amount") double amount,
                     @Param("reference_no") String reference_no,
                     @Param("status") String status,
                     @Param("reason_code") String reason_code,
                     @Param("created_at") LocalDateTime created_at);

    @Modifying
    @Query(value = " INSERT INTO payment_history(customer_id, account_id, beneficiary, beneficiary_acc_no, amount, reference_no, status, reason_code, created_at) " +
            "VALUES(:customer_id, :account_id,:beneficiary, :beneficiary_acc_no, :amount, :reference_no, :status, :reason_code, :created_at )", nativeQuery = true)
    @Transactional
    void logTransaction(@Param("customer_id")int customer_id,
                     @Param("account_id") int account_id,
                     @Param("beneficiary") String beneficiary,
                     @Param("beneficiary_acc_no") String beneficiary_acc_no,
                     @Param("amount") double amount,
                     @Param("reference_no") String reference_no,
                     @Param("status") String status,
                     @Param("reason_code") String reason_code,
                     @Param("created_at") String created_at);
}
