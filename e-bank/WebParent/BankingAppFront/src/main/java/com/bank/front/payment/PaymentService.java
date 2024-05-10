package com.bank.front.payment;

import com.bank.common.entity.Customer;
import com.bank.common.entity.PaymentHistory;
import com.bank.front.account.AccountRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;

    LocalDateTime currentDateTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    String formattedDateTime = currentDateTime.format(formatter);

    public PaymentService(PaymentRepository paymentRepository, AccountRepository accountRepository, PaymentHistoryRepository paymentHistoryRepository) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
        this.paymentHistoryRepository = paymentHistoryRepository;
    }

    public String payment(Customer customer, String beneficiary, String account_number, String account_id,
                          String reference, String payment_amount) {

        int accountID = Integer.parseInt(account_id);
        double paymentAmount = Double.parseDouble(payment_amount);
        if(paymentAmount == 0){
            return "Payment Amount Cannot be of 0 (Zero) value, please enter a value greater than 0 (Zero)";
        }

        double currentBalance = accountRepository.getAccountBalance(customer.getId(), accountID);

        if(currentBalance < paymentAmount){
            String reasonCode = "Could not Processed Payment due to insufficient funds!";
            paymentRepository.makePayment(accountID, beneficiary, account_number, paymentAmount, reference, "failed", reasonCode, currentDateTime);
            // Log Failed Transaction:
            paymentRepository.logTransaction(customer.getId(), accountID, beneficiary, account_number, paymentAmount, reference, "failed", "Insufficient Funds", formattedDateTime);
            return reasonCode;
        }

        double newBalance = currentBalance - paymentAmount;

        // MAKE PAYMENT:
        String reasonCode = "Payment Processed Successfully!";
        paymentRepository.makePayment(accountID, beneficiary, account_number, paymentAmount, reference, "success", reasonCode, currentDateTime);

        //UPDATE ACCOUNT PAYING FROM:
        accountRepository.changeAccountBalanceById(newBalance, accountID);

        paymentRepository.logTransaction(customer.getId(), accountID, beneficiary, account_number, paymentAmount, reference, "success", "Payment Transaction Successful", formattedDateTime);
        return reasonCode;

    }

    public List<PaymentHistory> getPaymentHistory(Customer customer){
        return paymentHistoryRepository.getPaymentRecordsById(customer.getId());
    }
}
