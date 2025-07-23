package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.TransferService;
import com.example.bankcards.util.CardMapper;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CardService cardService;
    @MockBean
    private TransferService transferService;
    @MockBean
    private CardMapper cardMapper;
    @MockBean
    private CurrentUserService currentUserService;

    @Test
    @DisplayName("GET /api/user/cards returns user cards")
    void getUserCards() throws Exception {
        Page<CardDTO> page = new PageImpl<>(List.of(new CardDTO()));
        Mockito.when(cardService.getAllCards(any(), any(), any())).thenReturn(page);
        Mockito.when(currentUserService.getCurrentUserId()).thenReturn(1L);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/cards"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("GET /api/user/cards/{cardId} returns card details")
    void getCardDetails() throws Exception {
        Mockito.when(cardService.getUserCard(eq(1L), any())).thenReturn(java.util.Optional.of(new com.example.bankcards.entity.Card()));
        Mockito.when(cardMapper.toCardDTO(any())).thenReturn(new CardDTO());
        Mockito.when(currentUserService.getCurrentUserId()).thenReturn(1L);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/cards/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("POST /api/user/cards/{cardId}/block-request blocks card")
    void requestBlockCard() throws Exception {
        Mockito.when(currentUserService.getCurrentUserId()).thenReturn(1L);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/cards/1/block-request"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/user/cards/transfer performs transfer")
    void transferBetweenCards() throws Exception {
        Mockito.when(currentUserService.getCurrentUserId()).thenReturn(1L);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/cards/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sourceCardId\":1,\"targetCardId\":2,\"amount\":100,\"comment\":\"test\"}"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
} 