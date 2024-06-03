package com.bank.front.account;

import com.bank.common.entity.Account;
import com.bank.common.entity.Customer;
import com.bank.front.account.repository.AccountRepository;
import com.bank.front.account.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class AccountServiceTests {

    @MockBean
    private AccountRepository repo;

    @InjectMocks
    private AccountService service;

    @Test
    public void testListAccountByCustomer() {

        Customer customer = new Customer();
        customer.setId(9);

        List<Account> accounts = new ArrayList<>();

        Mockito.when(repo.getUserAccountsById(customer.getId())).thenReturn(accounts);

        List<Account> result = service.ListAccountByCustomer(customer);

        verify(repo).getUserAccountsById(customer.getId());

        assertNotNull(result);
        assertEquals(accounts.size(), result.size());
    }
}
