package com.bank.front.account;

import com.bank.common.entity.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Query("SELECT a FROM Account a WHERE a.customer.id = ?1")
    List<Account> getUserAccountsById(int customer_id);

    @Query(value = "SELECT sum(balance) FROM accounts WHERE customer_id = :customer_id", nativeQuery = true)
    BigDecimal getTotalBalance(@Param("customer_id")int customer_id);

    @Query(value = "SELECT balance FROM accounts WHERE customer_id = :customer_id AND id = :id", nativeQuery = true)
    double getAccountBalance(@Param("customer_id") int customer_id, @Param("id") int id);

    @Modifying
    @Query(value ="UPDATE accounts SET balance = :new_balance WHERE id = :id" , nativeQuery = true)
    @Transactional
    void changeAccountBalanceById(@Param("new_balance") double new_balance, @Param("id") int id);

    @Modifying
    @Query(value = "INSERT INTO accounts(customer_id, account_number, account_name, account_type, created_at) VALUES" +
            "(:customer_id, :account_number, :account_name,:account_type, :created_at)", nativeQuery = true)
    @Transactional
    void createBankAccount(@Param("customer_id") int customer_id,
                           @Param("account_number") String account_number,
                           @Param("account_name") String account_name,
                           @Param("account_type")String account_type,
                            @Param("created_at") String created_at);


}
