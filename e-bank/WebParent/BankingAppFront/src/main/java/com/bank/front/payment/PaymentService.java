package com.bank.front.payment;

import com.bank.common.entity.Account;
import com.bank.common.entity.Customer;
import com.bank.common.entity.PaymentHistory;
import com.bank.front.account.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PaymentService {
    private final AccountRepository accountRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;

    LocalDateTime currentDateTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    String formattedDateTime = currentDateTime.format(formatter);

    public PaymentService(AccountRepository accountRepository, PaymentHistoryRepository paymentHistoryRepository) {
        this.accountRepository = accountRepository;
        this.paymentHistoryRepository = paymentHistoryRepository;
    }

    public String payment(Customer customer, String senderAccountNumber, String recipientAccountNumber,
                          String reference, double amount) {

        Account sender = accountRepository.findByAccountNumber(senderAccountNumber);
        Account recipient = accountRepository.findByAccountNumber(recipientAccountNumber);

        if (sender == null || recipient == null) {
            return "Invalid account number.";
        }

        if(amount == 0){
            return "Payment Amount Cannot be of 0 (Zero) value, please enter a value greater than 0 (Zero)";
        }


        if(sender.getBalance() < amount){
            String reasonCode = "Could not Processed Payment due to insufficient funds!";
            // Log Failed Transaction:
            paymentHistoryRepository.logTransaction(customer.getId(), sender.getId(), recipient.getAccountName(), recipient.getAccountNumber(), amount, reference, "failed", reasonCode, formattedDateTime);
            return reasonCode;
        }
        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);

        accountRepository.save(sender);
        accountRepository.save(recipient);

        // MAKE PAYMENT:
        String reasonCode = "Payment Processed Successfully!";

        paymentHistoryRepository.logTransaction(customer.getId(), sender.getId(), recipient.getAccountName(), recipient.getAccountNumber(), amount, reference, "success", reasonCode, formattedDateTime);
        return reasonCode;
    }

    public List<PaymentHistory> getPaymentHistory(Customer customer){
        return paymentHistoryRepository.getPaymentRecordsById(customer.getId());
    }
}
