package com.bank.front.transaction;

import com.bank.common.entity.Customer;
import com.bank.front.ControllerHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
@RequestMapping("/transact")
public class TransactController {
    private final TransactService service;
    private final ControllerHelper controllerHelper;

    public TransactController(TransactService service, ControllerHelper controllerHelper) {
        this.service = service;
        this.controllerHelper = controllerHelper;
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam("deposit_amount") String depositAmount,
                          @RequestParam("account_id") String accountID,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttributes) {
        Customer customer = controllerHelper.getAuthenticatedCustomer(request);

        String message = service.deposit(customer, depositAmount, accountID);
        if(Objects.equals(message, "Deposit Amount Cannot Be less or equals 0, please enter a value greater than 0")){
            redirectAttributes.addFlashAttribute("error", message);
        }else{
            redirectAttributes.addFlashAttribute("success", message);
        }
        return "redirect:/app/HomePage";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam("transfer_from") String transferFrom,
                           @RequestParam("transfer_to") String transferTo,
                           @RequestParam("transfer_amount") String transferAmount,
                           HttpServletRequest request,
                           RedirectAttributes redirectAttributes) {
        Customer customer = controllerHelper.getAuthenticatedCustomer(request);
        String message = service.transfer(customer, transferFrom, transferTo, transferAmount);
        switch (message) {
            case "Cannot Transfer Into The same Account, Please select the appropriate account to perform transfer",
                    "Transfer Amount Cannot Be less or equals 0, please enter a value greater than 0",
                    "You Have insufficient Funds to perform this Transfer!" -> {
                redirectAttributes.addFlashAttribute("error", message);
                return "redirect:/app/HomePage";
            }
            case "Amount Transferred Successfully!" -> {
                redirectAttributes.addFlashAttribute("success", message);
                return "redirect:/app/HomePage";
            }
        }
        return "redirect:/app/HomePage";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("withdrawal_amount")String withdrawalAmount,
                           @RequestParam("account_id")String accountID,
                           HttpServletRequest request,
                           RedirectAttributes redirectAttributes){
        Customer customer = controllerHelper.getAuthenticatedCustomer(request);

        String message = service.withdraw(customer, withdrawalAmount, accountID);

        if(Objects.equals(message, "Withdraw Amount Cannot Be less or equals 0, please enter a value greater than 0") ||
                Objects.equals(message, "You Have insufficient Funds to perform this Withdrawal!")){
            redirectAttributes.addFlashAttribute("error", message);
        }else{
            redirectAttributes.addFlashAttribute("success", message);
        }
        return "redirect:/app/HomePage";
    }


}
