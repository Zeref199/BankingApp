package com.bank.front.account;

import com.bank.common.entity.Customer;
import com.bank.front.ControllerHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account")
public class AccountController {
    private final AccountService service;
    private final ControllerHelper controllerHelper;

    public AccountController(AccountService service, ControllerHelper controllerHelper) {
        this.service = service;
        this.controllerHelper = controllerHelper;
    }

    @PostMapping("/create_account")
    public String createAccount(@RequestParam("account_name")String accountName,
                                @RequestParam("account_type")String accountType,
                                RedirectAttributes redirectAttributes,
                                HttpServletRequest request){

        //GET LOGGED IN USER:
        Customer customer = controllerHelper.getAuthenticatedCustomer(request);

        //CREATE ACCOUNT:
        service.createAccount(customer, accountName, accountType );

        // Set Success message:
        redirectAttributes.addFlashAttribute("success", "Account Created Successfully!");
        return "redirect:/app/HomePage";

    }
}
