package com.example.bankcards.util;

import com.example.bankcards.dto.AdminCardDTO;
import com.example.bankcards.dto.AdminCreateCardRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;

import java.time.LocalDate;

public class CardMapper {
    public AdminCardDTO toDTO(final Card card) {
        AdminCardDTO dto = new AdminCardDTO();
        dto.setId(card.getId());
        dto.setFullNumber("**** **** **** " + card.getCardNumber().substring(12));
        dto.setOwnerName(card.getOwnerName());
        dto.setExpiryDate(card.getExpiryDate());
        dto.setBalance(card.getBalance());
        dto.setStatus(card.getStatus().getDisplayName());
        dto.setUserId(card.getUser().getId());
        return dto;
    }

    public Card getCardFromRequest(final AdminCreateCardRequest request, final User user) {
        Card card = new Card();
        card.setUser(user);
        card.setCardNumber(request.getCardNumber());
        card.setOwnerName(user.getUsername());
        card.setExpiryDate(request.getExpiryDate());
        card.setBalance(request.getInitialBalance());
        return card;
    }
}