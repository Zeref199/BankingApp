package com.bank.admin.accounts;

import com.bank.common.entity.TransactionHistory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Integer> {
    @Modifying
    @Query(value = "DELETE FROM TransactionHistory t WHERE t.account.id = :accountId")
    @Transactional
    void deleteTransactionHistory(@Param("accountId") int accountId);

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

    @Query(value = "SELECT * FROM transaction_history WHERE account_id = :account_id", nativeQuery = true)
    List<TransactionHistory> getTransactionRecordsById(@Param("account_id")int account_id);
}
