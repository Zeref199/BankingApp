package com.bank.admin.customer.repository;

import com.bank.admin.paging.SearchRepository;
import com.bank.common.entity.Account;
import com.bank.common.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends SearchRepository<Customer, Integer> {

    @Query("SELECT c FROM Customer c WHERE CONCAT(c.email, ' ', c.firstName, ' ', c.lastName) LIKE %?1%")
    public Page<Customer> findAll(String keyword, Pageable pageable);

    @Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);

    @Query("SELECT c FROM Customer c WHERE c.email = ?1")
    public Customer findByEmail(String email);

    @Query("SELECT a FROM Account a WHERE a.customer.id = ?1")
    List<Account> getUserAccountsById(int customer_id);

    public Long countById(Integer id);

    @Query(value = "SELECT balance FROM accounts WHERE customer_id = :customer_id AND id = :id", nativeQuery = true)
    double getAccountBalance(@Param("customer_id") int customer_id, @Param("id") int id);



}
