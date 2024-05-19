package com.bank.front.account;

import com.bank.common.entity.Account;
import com.bank.common.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class AccountRepositoryTests {
    @Autowired
    private AccountRepository repo;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateAccount() {
        Integer customerId = 9;
        Customer customer = entityManager.find(Customer.class, customerId);

        Account account = new Account();
        account.setAccountName("test2");
        account.setAccountNumber("49877894");
        account.setAccountType("checking");
        double balance = 20000;
        account.setBalance(balance);
        account.setCustomer(customer);

        Account savedAccount = (Account) repo.save(account);

        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetUserAccountsById() {
        int customerId = 9;

        List<Account> customerAccounts = repo.getUserAccountsById(customerId);

        for (Account account : customerAccounts
             ) {
            System.out.println(account);
        }

        assertThat(customerAccounts).isNotNull();
        assertFalse(customerAccounts.isEmpty());
    }

    @Test
    public void testGetTotalBalance() {
        int customerId = 9;
        BigDecimal totalBalance = repo.getTotalBalance(customerId);

        System.out.println("The Total balance is : " + totalBalance);
        assertThat(totalBalance).isNotNull();
    }

    @Test
    public void testGetAccountBalance(){
        int customerId = 9;
        int AccountId = 2;

        double AccountBalance = repo.getAccountBalance(customerId, AccountId);

        System.out.println("the account Balance of the customer with the id : " + customerId + " is " + AccountBalance);

        assertThat(AccountBalance).isGreaterThan(0);
    }

    @Test
    public void testChangeAccountBalanceById(){
        double newBalance = 3000;
        int customerId = 9;
        int AccountId = 2;

        repo.changeAccountBalanceById(newBalance, AccountId);

        double AccountBalance = repo.getAccountBalance(customerId, AccountId);

        assertThat(AccountBalance).isEqualTo(newBalance);

    }

}

