package com.bank.admin.accounts.repository;

import com.bank.common.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Integer> {
    @Query(value = "SELECT * FROM payment_history WHERE account_id = :account_id", nativeQuery = true)
    List<PaymentHistory> getPaymentRecordsById(@Param("account_id")int account_id);
}
