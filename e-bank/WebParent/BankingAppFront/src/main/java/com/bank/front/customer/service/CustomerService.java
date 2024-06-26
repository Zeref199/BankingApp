package com.bank.front.customer.service;

import com.bank.common.entity.AuthenticationType;
import com.bank.common.entity.Customer;
import com.bank.common.exceptions.CustomerNotFoundException;
import com.bank.front.customer.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean isEmailUnique(String email) {
        Customer customer = customerRepo.findByEmail(email);
        return customer == null;
    }

    public void updateAuthenticationType(Customer customer, AuthenticationType type){
        if(!customer.getAuthenticationType().equals(type)){
            customerRepo.updateAuthenticationType(customer.getId(), type);
        }
    }

    public void registerCustomer(Customer customer) {
        encodePassword(customer);
        customer.setEnabled(false);
        customer.setCreatedAt(new Date());
        customer.setAuthenticationType(AuthenticationType.DATABASE);

        String randomCode = RandomString.make(64);
        customer.setVerificationCode(randomCode);

        customerRepo.save(customer);

    }

    public Customer getCustomerByEmail(String email) {
        return customerRepo.findByEmail(email);
    }

    private void encodePassword(Customer customer) {
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
    }

    public boolean verify(String verificationCode) {
        Customer customer = customerRepo.findByVerificationCode(verificationCode);

        if (customer == null || customer.isEnabled()) {
            return false;
        } else {
            customerRepo.enable(customer.getId());
            return true;
        }
    }

    public void addNewCustomerUponOAuthLogin(String name, String email) {
        Customer customer = new Customer();
        customer.setEmail(email);
        setName(name, customer);

        customer.setEnabled(true);
        customer.setCreatedAt(new Date());
        customer.setAuthenticationType(AuthenticationType.GOOGLE);
        customer.setPassword("");

        customerRepo.save(customer);
    }

    private void setName(String name, Customer customer) {
        String[] nameArray = name.split(" ");
        if (nameArray.length < 2) {
            customer.setFirstName(name);
            customer.setLastName("");
        } else {
            String firstName = nameArray[0];
            customer.setFirstName(firstName);

            String lastName = name.replaceFirst(firstName, "");
            customer.setLastName(lastName);
        }
    }

    public void update(Customer customerInForm) {
        Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();

        if (customerInDB.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
            if (!customerInForm.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
                customerInForm.setPassword(encodedPassword);
            } else {
                customerInForm.setPassword(customerInDB.getPassword());
            }
        } else {
            customerInForm.setPassword(customerInDB.getPassword());
        }

        customerInForm.setEnabled(customerInDB.isEnabled());
        customerInForm.setCreatedAt(customerInDB.getCreatedAt());
        customerInForm.setVerificationCode(customerInDB.getVerificationCode());
        customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
        customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());

        customerRepo.save(customerInForm);
    }

    public String updateResetPasswordToken(String email) throws CustomerNotFoundException {
        Customer customer = customerRepo.findByEmail(email);
        if (customer != null) {
            String token = RandomString.make(30);
            customer.setResetPasswordToken(token);
            customerRepo.save(customer);

            return token;
        } else {
            throw new CustomerNotFoundException("Could not find any customer with the email " + email);
        }
    }

    public Customer getByResetPasswordToken(String token) {
        return customerRepo.findByResetPasswordToken(token);
    }

    public void updatePassword(String token, String newPassword) throws CustomerNotFoundException {
        Customer customer = customerRepo.findByResetPasswordToken(token);
        if (customer == null) {
            throw new CustomerNotFoundException("No customer found: invalid token");
        }

        customer.setPassword(newPassword);
        customer.setResetPasswordToken(null);
        encodePassword(customer);

        customerRepo.save(customer);
    }
}
