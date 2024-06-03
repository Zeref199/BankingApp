package com.bank.admin.accounts.service;

import com.bank.admin.accounts.repository.AccountRepository;
import com.bank.admin.accounts.repository.PaymentHistoryRepository;
import com.bank.admin.accounts.repository.TransactionHistoryRepository;
import com.bank.admin.paging.PagingAndSortingHelper;
import com.bank.common.Dto.PaymentHistoryDto;
import com.bank.common.entity.Account;
import com.bank.common.entity.Customer;
import com.bank.common.entity.PaymentHistory;
import com.bank.common.entity.TransactionHistory;
import com.bank.common.exceptions.AccountNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
public class AccountService {

    private final AccountRepository accountRepository;
    public static final int ACCOUNTS_PER_PAGE = 10;
    private final TransactionHistoryRepository transactionRepository;
    private final PaymentHistoryRepository paymentRepository;
    private final ModelMapper modelMapper;

    LocalDateTime currentDateTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    String formattedDateTime = currentDateTime.format(formatter);
    @Autowired
    public AccountService(AccountRepository accountRepository, TransactionHistoryRepository transactionRepository, PaymentHistoryRepository paymentRepository, ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.paymentRepository = paymentRepository;
        this.modelMapper = modelMapper;
    }
    public void listByPage(int pageNum, PagingAndSortingHelper helper){
        helper.listEntities(pageNum, ACCOUNTS_PER_PAGE, accountRepository);
    }

        public void deleteAccount(Integer id) throws AccountNotFoundException {
        Long count = accountRepository.countById(id);
        if (count == null || count == 0) {
            throw new AccountNotFoundException("Could not find any customers with ID " + id);
        }
        transactionRepository.deleteTransactionHistory(id);
        accountRepository.deletePaymentHistory(id);
        accountRepository.deleteAccountByCustomerId(id);
        }

        public Account get(Integer id) throws AccountNotFoundException {
            try {
                return accountRepository.findById(id).get();
            } catch (NoSuchElementException ex) {
                throw new AccountNotFoundException("Could not find any account with ID " + id);
            }
        }

        public String deposit(String depositAmount, String accountID) {
        //GET CURRENT ACCOUNT BALANCE:
        int acc_id = Integer.parseInt(accountID);
        double depositAmountValue = Double.parseDouble(depositAmount);
        if (depositAmountValue <= 0) {
            return "Deposit Amount Cannot Be less or equals 0, please enter a value greater than 0";
        }
        Account account = accountRepository.findAccountWithCustomerById(acc_id);
        if (account == null) {
            return "Account not found";
        }
        Customer customer = account.getCustomer();
        if (customer == null) {
            return "Customer associated with this account not found";
        }
        double currentBalance = accountRepository.getAccountBalance(customer.getId(), acc_id);
        double newBalance = currentBalance + depositAmountValue;

        accountRepository.changeAccountBalanceById(newBalance, acc_id);

        transactionRepository.logTransaction(customer.getId(), acc_id, "deposit", depositAmountValue, "online", "success", "Deposit Transaction Successful", formattedDateTime);

        return "Amount Deposited Successfully!";
        }

    public List<TransactionHistory> listTransactions(Account account) {
        return transactionRepository.getTransactionRecordsById(account.getId());
    }

    public List<PaymentHistoryDto> listPayments(Account account) {
        List<PaymentHistory> paymentHistories = paymentRepository.getPaymentRecordsById(account.getId());
        return paymentHistories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PaymentHistoryDto convertToDto(PaymentHistory paymentHistory) {
        return modelMapper.map(paymentHistory, PaymentHistoryDto.class);
    }
}
