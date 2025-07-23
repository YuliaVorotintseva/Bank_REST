package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
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

@WebMvcTest(AdminController.class)
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CardService cardService;
    @MockBean
    private UserService userService;

    @Test
    @DisplayName("GET /api/admin/cards returns cards page")
    void getAllCards() throws Exception {
        Page<CardDTO> page = new PageImpl<>(List.of(new CardDTO()));
        Mockito.when(cardService.getAllCards(any(), any(), any())).thenReturn(page);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/cards"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("POST /api/admin/cards creates card")
    void createCardForUser() throws Exception {
        Mockito.when(cardService.createCard(any())).thenReturn(new CardDTO());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("PATCH /api/admin/cards/{id}/block blocks card")
    void adminBlockCard() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/cards/1/block"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PATCH /api/admin/cards/{id}/activate activates card")
    void adminActivateCard() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/cards/1/activate"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/admin/cards/{id} deletes card")
    void deleteCard() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/cards/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/admin/users returns users page")
    void getAllUsers() throws Exception {
        Page<UserDTO> page = new PageImpl<>(List.of(new UserDTO()));
        Mockito.when(userService.getAllUsers(any())).thenReturn(page);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/users"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("PATCH /api/admin/users/{id}/block blocks user")
    void blockUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/users/1/block"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT /api/admin/users/{id}/roles updates user roles")
    void updateUserRoles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/admin/users/1/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[\"USER\"]"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
} 