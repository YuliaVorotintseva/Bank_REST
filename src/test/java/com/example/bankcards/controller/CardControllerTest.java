package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CardMapper;
import com.example.bankcards.service.CurrentUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(CardController.class)
class CardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CardService cardService;
    @MockBean
    private CardMapper cardMapper;
    @MockBean
    private CurrentUserService currentUserService;

    @Test
    @DisplayName("GET /api/cards returns cards page")
    void getAllCards() throws Exception {
        CardDTO card = new CardDTO();
        card.setId(1L);
        card.setOwnerName("Test Owner");
        Page<CardDTO> page = new PageImpl<>(List.of(card));
        Mockito.when(cardService.getAllCards(any(), any(), any())).thenReturn(page);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cards"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("GET /api/cards/{id} returns card")
    void getCardById() throws Exception {
        CardDTO card = new CardDTO();
        card.setId(1L);
        Mockito.when(cardService.getUserCard(eq(1L), any())).thenReturn(java.util.Optional.of(new com.example.bankcards.entity.Card()));
        Mockito.when(cardMapper.toCardDTO(any())).thenReturn(card);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cards/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("PATCH /api/cards/{id}/block blocks card")
    void blockCard() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/cards/1/block"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
} 