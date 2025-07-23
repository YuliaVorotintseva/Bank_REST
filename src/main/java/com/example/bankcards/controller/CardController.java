package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CardMapper;
import com.example.bankcards.service.CurrentUserService;
import com.example.bankcards.entity.Card;
import com.example.bankcards.dto.AdminCreateCardRequest;
import com.example.bankcards.exception.customExceptions.cardExceptions.CardNotFoundException;
import com.example.bankcards.exception.customExceptions.cardExceptions.CardAlreadyBlockedException;
import com.example.bankcards.exception.customExceptions.cardExceptions.CardAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/cards")
@Tag(name = "Cards", description = "Управление банковскими картами")
@SecurityRequirement(name = "JWT")
public class CardController {
    private final CardService cardService;
    private final CardMapper cardMapper;
    private final CurrentUserService currentUserService;

    @Autowired
    public CardController(CardService cardService, CardMapper cardMapper, CurrentUserService currentUserService) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
        this.currentUserService = currentUserService;
    }

    @Operation(summary = "Получить все карты пользователя")
    @GetMapping
    public ResponseEntity<Page<CardDTO>> getAllCards(
            @Parameter(description = "Статус карты") @RequestParam(required = false) String status,
            @Parameter(description = "Минимальный баланс") @RequestParam(required = false) Double minBalance,
            Pageable pageable) {
        Page<CardDTO> cards = cardService.getAllCards(null, status, pageable);
        if (minBalance != null) {
            var filtered = cards.getContent().stream()
                .filter(card -> card.getBalance() != null && card.getBalance() >= minBalance)
                .collect(Collectors.toList());
            return ResponseEntity.ok(new PageImpl<>(filtered, pageable, filtered.size()));
        }
        return ResponseEntity.ok(cards);
    }

    @Operation(summary = "Получить карту по ID")
    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> getCardById(
            @Parameter(description = "ID карты", required = true) @PathVariable Long id) throws CardNotFoundException {
        Card card = cardService.getUserCard(id, null)
                .orElseThrow(() -> new CardNotFoundException(id));
        return ResponseEntity.ok(cardMapper.toCardDTO(card));
    }

    @Operation(summary = "Создать новую карту")
    @PostMapping
    public ResponseEntity<CardDTO> createCard(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные карты", required = true)
            @Valid @RequestBody CardDTO cardDto) throws CardAlreadyExistsException {
        AdminCreateCardRequest req = new AdminCreateCardRequest();
        req.setUserId(currentUserService.getCurrentUserId());
        req.setCardNumber(cardDto.getMaskedNumber()); // You may need to adjust this logic
        req.setOwnerName(cardDto.getOwnerName());
        req.setExpiryDate(cardDto.getExpiryDate());
        req.setInitialBalance(cardDto.getBalance());
        CardDTO created = cardService.createCard(req);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Обновить карту")
    @PutMapping("/{id}")
    public ResponseEntity<CardDTO> updateCard(
            @Parameter(description = "ID карты", required = true) @PathVariable Long id,
            @Valid @RequestBody CardDTO cardDto) throws CardNotFoundException {
        Card card = cardService.getUserCard(id, null)
                .orElseThrow(() -> new CardNotFoundException(id));
        card.setOwnerName(cardDto.getOwnerName());
        card.setExpiryDate(cardDto.getExpiryDate());
        Card updated = cardService.saveCard(card);
        return ResponseEntity.ok(cardMapper.toCardDTO(updated));
    }

    @Operation(summary = "Заблокировать карту")
    @PatchMapping("/{id}/block")
    public ResponseEntity<Void> blockCard(
            @Parameter(description = "ID карты", required = true) @PathVariable Long id) throws CardNotFoundException, CardAlreadyBlockedException {
        cardService.blockCard(id);
        return ResponseEntity.noContent().build();
    }
}