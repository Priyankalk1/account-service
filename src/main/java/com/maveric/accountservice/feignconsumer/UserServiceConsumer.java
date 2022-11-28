package com.maveric.accountservice.feignconsumer;

import com.maveric.accountservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="user-service")
@Service
public interface UserServiceConsumer {
    @GetMapping("api/v1/users/{userId}")
    public ResponseEntity<UserDto> getUserDetails(@PathVariable String userId);
}
