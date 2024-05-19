package com.bank.front.transaction;

import com.bank.common.entity.Customer;
import com.bank.common.entity.TransactionHistory;
import com.bank.front.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TransactService {
    public static final int TRANSACTIONS_PER_PAGE = 10;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    double currentBalance;
    double newBalance;
    LocalDateTime currentDateTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    String formattedDateTime = currentDateTime.format(formatter);
    @Autowired
    public TransactService(AccountRepository accountRepository, TransactionRepository transactionRepository, TransactionHistoryRepository transactionHistoryRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    public String deposit(Customer customer, String depositAmount, String accountID) {
        //GET CURRENT ACCOUNT BALANCE:
        int acc_id = Integer.parseInt(accountID);
        double depositAmountValue = Double.parseDouble(depositAmount);
        if (depositAmountValue <= 0) {
            return "Deposit Amount Cannot Be less or equals 0, please enter a value greater than 0";
        }
        double currentBalance = accountRepository.getAccountBalance(customer.getId(), acc_id);
        double newBalance = currentBalance + depositAmountValue;

        accountRepository.changeAccountBalanceById(newBalance, acc_id);

        transactionRepository.logTransaction(customer.getId(), acc_id, "deposit", depositAmountValue, "online", "success", "Deposit Transaction Successful", formattedDateTime);

        return "Amount Deposited Successfully!";
    }

    public String transfer(Customer customer, String transfer_from, String transfer_to, String transfer_amount) {
        int transferFromId = Integer.parseInt(transfer_from);
        int transferToId = Integer.parseInt(transfer_to);
        double transferAmount = Double.parseDouble(transfer_amount);

        if(transferFromId == transferToId){
            return "Cannot Transfer Into The same Account, Please select the appropriate account to perform transfer";
        }
        if(transferAmount <= 0){
            return "Transfer Amount Cannot Be less or equals 0, please enter a value greater than 0";
        }

        // GET CURRENT BALANCE:
        double currentBalanceOfAccountTransferringFrom  = accountRepository.getAccountBalance(customer.getId(), transferFromId);
        if(currentBalanceOfAccountTransferringFrom < transferAmount){
            // Log Failed Transaction:
            transactionRepository.logTransaction(customer.getId(), transferFromId, "Transfer", transferAmount, "online", "failed", "Insufficient Funds", formattedDateTime);
            return "You Have insufficient Funds to perform this Transfer!";
        }

        double  currentBalanceOfAccountTransferringTo = accountRepository.getAccountBalance(customer.getId(), transferToId);

        // SET NEW BALANCE:
        double newBalanceOfAccountTransferringFrom = currentBalanceOfAccountTransferringFrom - transferAmount;

        double newBalanceOfAccountTransferringTo = currentBalanceOfAccountTransferringTo + transferAmount;

        // Changed The Balance Of the Account Transferring From:
        accountRepository.changeAccountBalanceById( newBalanceOfAccountTransferringFrom, transferFromId);

        // Changed The Balance Of the Account Transferring To:
        accountRepository.changeAccountBalanceById(newBalanceOfAccountTransferringTo, transferToId);

        // Log Successful Transaction:
        transactionRepository.logTransaction(customer.getId(), transferFromId, "Transfer", transferAmount, "online", "success", "Transfer Transaction Successful",formattedDateTime);

        return "Amount Transferred Successfully!";
    }

    public String withdraw(Customer customer, String withdrawalAmount, String accountID) {
        double withdrawal_amount = Double.parseDouble(withdrawalAmount);
        int account_id = Integer.parseInt(accountID);

        if (withdrawal_amount <= 0){
            return "Withdraw Amount Cannot Be less or equals 0, please enter a value greater than 0";
        }
        currentBalance = accountRepository.getAccountBalance(customer.getId(), account_id);
        if(currentBalance < withdrawal_amount){
            // Log Failed Transaction:
            transactionRepository.logTransaction(customer.getId(), account_id, "Withdrawal", withdrawal_amount, "online", "failed", "Insufficient Funds", formattedDateTime);
            return "You Have insufficient Funds to perform this Withdrawal!";
        }
        newBalance = currentBalance - withdrawal_amount;

        accountRepository.changeAccountBalanceById(newBalance, account_id);

        // Log Successful Transaction:
        transactionRepository.logTransaction(customer.getId(), account_id, "Withdrawal", withdrawal_amount, "online", "success", "Withdrawal Transaction Successful",formattedDateTime);

        return "Withdrawal Successful!";
    }

    public Page<TransactionHistory> listTransactionByPage(Customer customer, int pageNum, String keyword) {

        Pageable pageable = PageRequest.of(pageNum - 1, TRANSACTIONS_PER_PAGE);

        if (keyword != null) {
            return transactionHistoryRepository.getTransactionRecordsById(customer.getId(), keyword, pageable);
        }

        return transactionHistoryRepository.getTransactionRecordsById(customer.getId(), pageable);
    }
}
