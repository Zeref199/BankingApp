package com.bank.admin.accounts;

import com.bank.admin.paging.SearchRepository;
import com.bank.common.entity.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends SearchRepository<Account, Integer> {

    @Query("SELECT a FROM Account a WHERE CONCAT(a.accountName, ' ', a.accountNumber, ' ', a.customer) LIKE %?1%")
    public Page<Account> findAll(String keyword, Pageable pageable);

    @Query("SELECT a FROM Account a JOIN FETCH a.customer WHERE a.id = :accountId")
    Account findAccountWithCustomerById(int accountId);

    public Long countById(Integer id);

    @Modifying
    @Query(value = "DELETE FROM Account a WHERE a.id = :accountId")
    @Transactional
    void deleteAccountByCustomerId(@Param("accountId") int accountId);



    @Modifying
    @Query(value = "DELETE FROM PaymentHistory p WHERE p.account.id = :accountId")
    @Transactional
    void deletePaymentHistory(@Param("accountId") int accountId);



    @Query(value = "SELECT balance FROM accounts WHERE customer_id = :customer_id AND id = :id", nativeQuery = true)
    double getAccountBalance(@Param("customer_id") int customer_id, @Param("id") int id);

    @Modifying
    @Query(value ="UPDATE accounts SET balance = :new_balance WHERE id = :id" , nativeQuery = true)
    @Transactional
    void changeAccountBalanceById(@Param("new_balance") double new_balance, @Param("id") int id);


}
