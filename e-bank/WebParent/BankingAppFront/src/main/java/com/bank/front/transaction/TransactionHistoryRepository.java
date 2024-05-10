package com.bank.front.transaction;

import com.bank.common.entity.TransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Integer> {

    @Query("SELECT t FROM TransactionHistory t WHERE t.id = ?1 AND t.createdAt LIKE %?2%")
    Page<TransactionHistory> getTransactionRecordsById(int customer_id, String keyword, Pageable pageable);
    @Query(value = "SELECT * FROM transaction_history WHERE customer_id = :customer_id", nativeQuery = true)
    Page<TransactionHistory> getTransactionRecordsById(@Param("customer_id")int customer_id, Pageable pageable);
}
