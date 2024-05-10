package com.bank.front.account;

import com.bank.common.entity.Account;
import com.bank.common.entity.Customer;
import com.bank.front.GenAccountNumber;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class AccountService {
    private final AccountRepository repo;

    LocalDateTime currentDateTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    String formattedDateTime = currentDateTime.format(formatter);
    public AccountService(AccountRepository repo) {
        this.repo = repo;
    }

    public void createAccount(Customer customer, String accountName, String accountType) {

        // GENERATE ACCOUNT NUMBER:
        int setAccountNumber = GenAccountNumber.generateAccountNumber();
        String bankAccountNumber = Integer.toString(setAccountNumber);

        // CREATE ACCOUNT:
        repo.createBankAccount(customer.getId(), bankAccountNumber, accountName, accountType, formattedDateTime);
    }

    public List<Account> ListAccountByCustomer(Customer customer){
        return repo.getUserAccountsById(customer.getId());
    }

    public BigDecimal totalBalance(Customer customer){
        return repo.getTotalBalance(customer.getId());
    }
}
