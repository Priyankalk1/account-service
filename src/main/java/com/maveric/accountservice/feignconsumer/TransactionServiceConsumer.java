package com.maveric.accountservice.feignconsumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="transaction-service")
@Service
public interface TransactionServiceConsumer {

    @DeleteMapping("api/v1/accounts/{accountId}/transactions")
    public ResponseEntity<String> deleteTransactionByAccountId(@PathVariable String accountId);
}
