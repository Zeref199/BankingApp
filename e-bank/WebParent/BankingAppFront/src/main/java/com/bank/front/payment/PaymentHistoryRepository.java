package com.bank.front.payment;

import com.bank.common.entity.PaymentHistory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Integer> {
    @Query(value = "SELECT * FROM payment_history WHERE customer_id = :customer_id", nativeQuery = true)
    List<PaymentHistory> getPaymentRecordsById(@Param("customer_id")int customer_id);

    @Modifying
    @Query(value = " INSERT INTO payment_history(account_id, beneficiary, beneficiary_acc_no, amount, reference_no, status, reason_code, created_at) " +
            "VALUES(:account_id,:beneficiary, :beneficiary_acc_no, :amount, :reference_no, :status, :reason_code, :created_at )", nativeQuery = true)
    @Transactional
    void logTransaction(@Param("account_id") int account_id,
                        @Param("beneficiary") String beneficiary,
                        @Param("beneficiary_acc_no") String beneficiary_acc_no,
                        @Param("amount") double amount,
                        @Param("reference_no") String reference_no,
                        @Param("status") String status,
                        @Param("reason_code") String reason_code,
                        @Param("created_at") String created_at);
}
