package com.example.bankcards.controller;

import com.example.bankcards.service.TransferService;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import com.example.bankcards.service.CurrentUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(TransferController.class)
class TransferControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransferService transferService;
    @MockBean
    private CardRepository cardRepository;
    @MockBean
    private TransferRepository transferRepository;
    @MockBean
    private CurrentUserService currentUserService;

    @Test
    @DisplayName("POST /api/transfers performs transfer")
    void transfer() throws Exception {
        Mockito.when(currentUserService.getCurrentUserId()).thenReturn(1L);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sourceCardId\":1,\"targetCardId\":2,\"amount\":100,\"comment\":\"test\"}"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/transfers returns transfer history")
    void getTransferHistory() throws Exception {
        Mockito.when(currentUserService.getCurrentUserId()).thenReturn(1L);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/transfers"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
} 