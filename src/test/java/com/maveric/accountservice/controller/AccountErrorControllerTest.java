package com.maveric.accountservice.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes=AccountErrorController.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebMvcTest(AccountErrorController.class)
class AccountErrorControllerTest {

    @Autowired
    private MockMvc mock;

    @Test
    void errorHandler() throws Exception {
        mock.perform(get("/error"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}