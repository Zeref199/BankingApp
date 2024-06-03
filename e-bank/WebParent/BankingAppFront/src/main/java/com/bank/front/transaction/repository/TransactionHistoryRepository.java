package com.bank.front.transaction.repository;

import com.bank.common.entity.TransactionHistory;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Integer> {

    @Query("SELECT t FROM TransactionHistory t WHERE t.id = ?1 AND t.createdAt LIKE %?2%")
    Page<TransactionHistory> getTransactionRecordsById(int customer_id, String keyword, Pageable pageable);
    @Query(value = "SELECT * FROM transaction_history WHERE customer_id = :customer_id", nativeQuery = true)
    Page<TransactionHistory> getTransactionRecordsById(@Param("customer_id")int customer_id, Pageable pageable);
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
