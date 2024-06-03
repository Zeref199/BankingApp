package com.bank.admin.account;

import com.bank.admin.accounts.repository.TransactionHistoryRepository;
import com.bank.common.entity.TransactionHistory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class AccountRepositoryTests {
    @Autowired
    private TransactionHistoryRepository repo;

    @Test
    public void testListTransactionHistory() {
        int account_id = 3;
        List<TransactionHistory> result = repo.getTransactionRecordsById(account_id);

        for (TransactionHistory tr: result) {
            System.out.println(tr);
        }

        assertThat(result).isNotNull();
    }
}
