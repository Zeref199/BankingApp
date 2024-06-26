package com.bank.front.payment;

import com.bank.common.entity.Customer;
import com.bank.front.ControllerHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/transact")
public class PaymentController {
    private final PaymentService paymentService;
    private final ControllerHelper controllerHelper;

    public PaymentController(PaymentService paymentService, ControllerHelper controllerHelper) {
        this.paymentService = paymentService;
        this.controllerHelper = controllerHelper;
    }


    @PostMapping("/payment")
    public String payment(@RequestParam String senderAccountNumber, @RequestParam String recipientAccountNumber,
                          @RequestParam String reference,
                          @RequestParam double amount,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttributes) {
        Customer customer = controllerHelper.getAuthenticatedCustomer(request);

        String message = paymentService.payment(customer, senderAccountNumber, recipientAccountNumber,reference, amount);
        switch (message) {
            case "Payment Amount Cannot be of 0 (Zero) value, please enter a value greater than 0 (Zero)",
                    "Could not Processed Payment due to insufficient funds!",
                    "Invalid account number."-> {
                redirectAttributes.addFlashAttribute("error", message);
                return "redirect:/app/HomePage";
            }
            case "Payment Processed Successfully!" -> {
                redirectAttributes.addFlashAttribute("success", message);
                return "redirect:/app/HomePage";
            }
        }
        return "redirect:/app/HomePage";
    }

}
