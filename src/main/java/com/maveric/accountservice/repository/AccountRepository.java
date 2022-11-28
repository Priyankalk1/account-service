package com.maveric.accountservice.repository;

import com.maveric.accountservice.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account,String> {
    Page<Account> findByCustomerId(Pageable page, String userId);

}
