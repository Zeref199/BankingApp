package com.bank.front.transaction;

import com.bank.common.entity.Transact;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transact, Integer> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO transaction_history(customer_id, account_id, transaction_type, amount, source, status, reason_code, created_at)" +
            "VALUES(:customer_id, :account_id, :transact_type, :amount, :source, :status, :reason_code, :created_at)", nativeQuery = true)
    void logTransaction(@Param("customer_id")int customer_id,
                        @Param("account_id")int account_id,
                        @Param("transact_type")String transact_type,
                        @Param("amount")double amount,
                        @Param("source")String source,
                        @Param("status")String status,
                        @Param("reason_code")String reason_code,
                        @Param("created_at") String created_at);
}
