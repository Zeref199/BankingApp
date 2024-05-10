package com.bank.front.payment;

import com.bank.common.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Integer> {
    @Query(value = "SELECT * FROM payment_history WHERE customer_id = :customer_id", nativeQuery = true)
    List<PaymentHistory> getPaymentRecordsById(@Param("customer_id")int customer_id);
}
