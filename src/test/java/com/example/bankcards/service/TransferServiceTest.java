package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TransferServiceTest {
    @Mock
    private CardRepository cardRepository;
    @Mock
    private TransferRepository transferRepository;
    @InjectMocks
    private TransferService transferService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    @DisplayName("makeTransferBetweenUserCards completes transfer")
    void makeTransferBetweenUserCards() throws Exception {
        Card source = new Card();
        source.setId(1L);
        source.setBalance(200.0);
        Card target = new Card();
        target.setId(2L);
        target.setBalance(100.0);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(source));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(target));
        when(transferRepository.save(any())).thenReturn(new Transfer());
        assertDoesNotThrow(() -> transferService.makeTransferBetweenUserCards(1L, 1L, 2L, 50.0, "test"));
    }
} 