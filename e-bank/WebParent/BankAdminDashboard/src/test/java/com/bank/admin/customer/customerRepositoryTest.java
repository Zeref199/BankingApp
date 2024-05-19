package com.bank.admin.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class customerRepositoryTest {
    @Autowired
    private CustomerRepository repo;

//    @Test
//    public void testDeleteAccount() {
//        Integer account_id = 6;
//        repo.deleteAccountByCustomerId(account_id);
//    }
//
//    @Test
//    public void testDeleteTransactionHistory() {
//        int account_id = 6;
//        repo.deleteTransactionHistory(account_id);
//    }
//
//    @Test
//    public void testDeletePaymentHistory() {
//        int account_id = 6;
//        repo.deletePaymentHistory(account_id);
//    }
}
