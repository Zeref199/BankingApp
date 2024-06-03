package com.bank.admin.accounts.controller;

import com.bank.admin.accounts.service.AccountService;
import com.bank.admin.paging.PagingAndSortingHelper;
import com.bank.admin.paging.PagingAndSortingParam;
import com.bank.common.Dto.PaymentHistoryDto;
import com.bank.common.entity.Account;
import com.bank.common.entity.TransactionHistory;
import com.bank.common.exceptions.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

@Controller
public class AccountController {
    private final AccountService service;
    @Autowired
    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping("/accounts")
    public String listFirstPage() {
        return "redirect:/accounts/page/1?sortField=accountName&sortDir=asc";
    }

    @GetMapping("/accounts/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "listAccounts", moduleURL = "/accounts") PagingAndSortingHelper helper,
                             @PathVariable(name = "pageNum") int pageNum){

        service.listByPage(pageNum, helper);

        return "accounts/accounts";
    }

    @GetMapping("/accounts/delete/{id}")
    public String deleteAccount(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            service.deleteAccount(id);
            ra.addFlashAttribute("message", "The account ID " + id + " has been deleted successfully.");
        } catch (AccountNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/accounts";
    }

    @GetMapping("/accounts/deposit/{id}")
    public String deposit(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            Account account = service.get(id);

            model.addAttribute("account", account);
            model.addAttribute("pageTitle", String.format("Deposit in Account with (ID: %d)", id));

            return "accounts/deposit_modal";

        } catch (AccountNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return "redirect:/accounts";
        }
    }

    @PostMapping("/accounts/deposit")
    public String deposit(@RequestParam("deposit_amount") String depositAmount,
                          @RequestParam("account_id") String accountID,
                          RedirectAttributes redirectAttributes, Model model) {

        String message = service.deposit(depositAmount, accountID);
        if(Objects.equals(message, "Deposit Amount Cannot Be less or equals 0, please enter a value greater than 0")){
            model.addAttribute("error", message);
        }else{
            model.addAttribute("success", message);
        }
        return "redirect:/accounts";
    }

    @GetMapping("/accounts/detail/{id}")
    public String listAllTransactions(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            Account account = service.get(id);
            List<TransactionHistory> transactions = service.listTransactions(account);
            List<PaymentHistoryDto> payments = service.listPayments(account);

            model.addAttribute("transactions", transactions);
            model.addAttribute("payments", payments);

            return "accounts/transactions_modal";
        } catch (AccountNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return "redirect:/accounts";
        }
    }


}
