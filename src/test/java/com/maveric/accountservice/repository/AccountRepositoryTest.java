package com.maveric.accountservice.repository;

import com.maveric.accountservice.model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
import static com.maveric.accountservice.AccountServiceApplicationTests.getAccount;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@RunWith(SpringRunner.class)
public class AccountRepositoryTest {

    @Autowired
    AccountRepository repository;


    @Test
    public void testSave() {
        Account account = repository.save(getAccount());
        assertEquals("1234",account.getCustomerId());
    }

    @Test
    public void testFindAll() {
        List<Account> account = repository.findAll();
        assertNotNull(account);
        assert(account.size()>0);
    }

}
