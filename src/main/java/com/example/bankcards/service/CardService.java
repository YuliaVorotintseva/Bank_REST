package com.example.bankcards.service;

import com.example.bankcards.dto.AdminCreateCardRequest;
import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.customExceptions.cardExceptions.CardAlreadyBlockedException;
import com.example.bankcards.exception.customExceptions.cardExceptions.CardAlreadyExistsException;
import com.example.bankcards.exception.customExceptions.cardExceptions.CardNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardMapper;
import com.example.bankcards.util.CardSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;

    public CardService(
            final CardRepository cardRepository,
            final UserRepository userRepository,
            final CardMapper cardMapper
    ) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.cardMapper = cardMapper;
    }

    @Transactional(readOnly = true)
    public Page<CardDTO> getAllCards(final Long userId, final String status, final Pageable pageable) {
        return cardRepository.findAll(
                CardSpecifications.withFilters(status, userId), pageable
        ).map(cardMapper::toCardDTO);
    }

    @Transactional
    public CardDTO createCard(final AdminCreateCardRequest request)
            throws CardAlreadyExistsException {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException(request.getUserId().toString()));

        if (cardRepository.existsByCardNumber(request.getCardNumber())) {
            throw new CardAlreadyExistsException(request.getCardNumber());
        }

        Card card = cardMapper.getCardFromRequest(request, user);
        card.setUser(user);
        card.setStatus(Card.CardStatus.ACTIVE);
        return cardMapper.toCardDTO(cardRepository.save(card));
    }

    @Transactional
    public void blockCard(Long cardId) throws CardNotFoundException, CardAlreadyBlockedException {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));

        if (card.getStatus() == Card.CardStatus.BLOCKED) {
            throw new CardAlreadyBlockedException(cardId);
        }

        card.setStatus(Card.CardStatus.BLOCKED);
        cardRepository.save(card);
    }

    @Transactional
    public void activateCard(Long cardId) throws CardNotFoundException {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));

        card.setStatus(Card.CardStatus.ACTIVE);
        cardRepository.save(card);
    }

    @Transactional
    public void deleteCard(Long cardId) throws CardNotFoundException {
        if (!cardRepository.existsById(cardId)) {
            throw new CardNotFoundException(cardId);
        }
        cardRepository.deleteById(cardId);
    }

    @Transactional(readOnly = true)
    public Optional<Card> getUserCard(Long cardId, Long userId) {
        return cardRepository.findById(cardId)
                .filter(card -> card.getUser() != null && card.getUser().getId().equals(userId));
    }

    @Transactional
    public void requestCardBlock(Long userId, Long cardId, String reason) {
        Card card = cardRepository.findById(cardId)
                .filter(c -> c.getUser() != null && c.getUser().getId().equals(userId))
                .orElseThrow(() -> new UsernameNotFoundException("Card not found or does not belong to user"));
        card.setStatus(Card.CardStatus.BLOCKED);
        cardRepository.save(card);
    }

    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }
}