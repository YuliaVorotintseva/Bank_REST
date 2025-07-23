package com.example.bankcards.service;

import com.example.bankcards.dto.AdminCreateCardRequest;
import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CardServiceTest {
    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardMapper cardMapper;
    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    @DisplayName("getAllCards returns page")
    void getAllCards() {
        when(cardRepository.findAll((Specification<Card>) any(), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(new Card())));
        when(cardMapper.toCardDTO(any())).thenReturn(new CardDTO());
        Page<CardDTO> result = cardService.getAllCards(1L, null, Pageable.unpaged());
        assertNotNull(result);
    }

    @Test
    @DisplayName("createCard creates card")
    void createCard() throws Exception {
        AdminCreateCardRequest req = new AdminCreateCardRequest();
        req.setUserId(1L);
        req.setCardNumber("1234567890123456");
        req.setOwnerName("Test Owner");
        req.setExpiryDate("12/25");
        req.setInitialBalance(100.0);
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardRepository.existsByCardNumber(any())).thenReturn(false);
        when(cardMapper.getCardFromRequest(any(), any())).thenReturn(new Card());
        when(cardRepository.save(any())).thenReturn(new Card());
        when(cardMapper.toCardDTO(any())).thenReturn(new CardDTO());
        CardDTO dto = cardService.createCard(req);
        assertNotNull(dto);
    }

    @Test
    @DisplayName("blockCard blocks card")
    void blockCard() {
        Card card = new Card();
        card.setStatus(Card.CardStatus.ACTIVE);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(any())).thenReturn(card);
        assertDoesNotThrow(() -> cardService.blockCard(1L));
    }

    @Test
    @DisplayName("getUserCard returns card")
    void getUserCard() {
        Card card = new Card();
        User user = new User();
        user.setId(1L);
        card.setUser(user);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        Optional<Card> result = cardService.getUserCard(1L, 1L);
        assertTrue(result.isPresent());
    }
}