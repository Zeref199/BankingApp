package com.bank.front;

import com.bank.common.entity.Account;
import com.bank.common.entity.Customer;
import com.bank.common.entity.PaymentHistory;
import com.bank.common.entity.TransactionHistory;
import com.bank.front.account.service.AccountService;
import com.bank.front.payment.PaymentService;
import com.bank.front.transaction.service.TransactService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/app")
public class AppController {

    private final PaymentService paymentService;
    private final TransactService transactService;
    private final ControllerHelper controllerHelper;
    private final AccountService accountService;


    public AppController(PaymentService paymentService, TransactService transactService, ControllerHelper controllerHelper, AccountService accountService) {
        this.paymentService = paymentService;
        this.transactService = transactService;
        this.controllerHelper = controllerHelper;
        this.accountService = accountService;
    }

    @GetMapping("/HomePage")
    public String getHomePage(HttpServletRequest request, Model model){
        // Get the details of the logged i user:
        Customer customer = controllerHelper.getAuthenticatedCustomer(request);

        List<Account> getCustomerAccounts = accountService.ListAccountByCustomer(customer);

        // Get Balance:
        BigDecimal totalAccountsBalance = accountService.totalBalance(customer);
        String hello = "hello";

        // Set Objects:
        model.addAttribute("userAccounts", getCustomerAccounts);
        model.addAttribute("totalBalance", totalAccountsBalance);
        model.addAttribute("hello", hello);

        return "HomePage";
    }



    @GetMapping("/payment_history")
    public String getPaymentHistory(HttpServletRequest request, Model model){

        // Get Logged In User:\
        Customer customer = controllerHelper.getAuthenticatedCustomer(request);

        // Get Payment History / Records:
        List<PaymentHistory> customerPaymentHistory = paymentService.getPaymentHistory(customer);

        model.addAttribute("payment_history", customerPaymentHistory);

        return "paymentHistory";
    }
    @GetMapping("/transact_history")
    public String listTransactFirstPage(HttpServletRequest request, Model model, String keyword){
        return getTransactHistory(request, model, 1, keyword);
    }


    @GetMapping("/transact_history/page/{pageNum}")
    public String getTransactHistory(HttpServletRequest request, Model model, @PathVariable(name = "pageNum") int pageNum,
                                    @Param("keyword") String keyword){

        // Get Logged In User:\
        Customer customer = controllerHelper.getAuthenticatedCustomer(request);

        // Get Payment History / Records:
        Page<TransactionHistory> page = transactService.listTransactionByPage(customer, pageNum, keyword);

        long startCount = (long) (pageNum - 1) * TransactService.TRANSACTIONS_PER_PAGE + 1;
        long endCount = startCount + TransactService.TRANSACTIONS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("transact_history", page);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("keyword", keyword);

        return "transactionHistory";

    }
}
