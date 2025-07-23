package com.example.bankcards.util;

import com.example.bankcards.dto.AdminCreateCardRequest;
import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;

public class CardMapper {
    public CardDTO toCardDTO(final Card card) {
        CardDTO dto = new CardDTO();
        dto.setId(card.getId());
        dto.setOwnerName(card.getOwnerName());
        dto.setMaskedNumber("**** **** **** " + card.getCardNumber().substring(12));
        dto.setStatus(card.getStatus().getDisplayName());
        dto.setBalance(card.getBalance());
        dto.setExpiryDate(card.getExpiryDate());
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