package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.dto.TransferDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.customExceptions.cardExceptions.CardNotFoundException;
import com.example.bankcards.exception.customExceptions.transferExceptions.NotEnoughFundsException;
import com.example.bankcards.exception.customExceptions.transferExceptions.SameCardTransferException;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.TransferService;
import com.example.bankcards.util.CardMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/cards")
@Tag(name = "Карты пользователя", description = "Управление картами пользователя")
@SecurityRequirement(name = "JWT")
public class UserController {
    private final CardService cardService;
    private final TransferService transferService;
    private final CardMapper cardMapper;

    public UserController(CardService cardService, TransferService transferService, CardMapper cardMapper) {
        this.cardService = cardService;
        this.transferService = transferService;
        this.cardMapper = cardMapper;
    }

    @Operation(summary = "Получить все карты пользователя")
    @GetMapping
    public ResponseEntity<Page<CardDTO>> getUserCards(
            @Parameter(description = "Статус карты (опционально)") @RequestParam(required = false) String status,
            @Parameter(description = "Минимальный баланс (опционально)") @RequestParam(required = false) Double minBalance,
            Pageable pageable) {

        Long userId = getCurrentUserId();
        return ResponseEntity.ok(cardService.getAllCards(userId, status, pageable));
    }

    @Operation(summary = "Получить детали карты")
    @GetMapping("/{cardId}")
    public ResponseEntity<CardDTO> getCardDetails(
        @Parameter(description = "ID карты", required = true) @PathVariable Long cardId
        )throws CardNotFoundException{
        Long userId = getCurrentUserId();
        Card card = cardService.getUserCard(cardId, userId)
                .orElseThrow(() -> new CardNotFoundException(cardId));
        return ResponseEntity.ok(cardMapper.toCardDTO(card));
    }

    @Operation(summary = "Запросить блокировку карты")
    @PostMapping("/{cardId}/block-request")
    public ResponseEntity<Void> requestBlockCard(
            @Parameter(description = "ID карты", required = true) @PathVariable Long cardId,
            @Parameter(description = "Причина блокировки") @RequestParam(required = false) String reason) {
        Long userId = getCurrentUserId();
        cardService.requestCardBlock(userId, cardId, reason);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Перевод между своими картами")
    @PostMapping("/transfer")
    public ResponseEntity<Void> transferBetweenCards(
            @Valid @RequestBody TransferDTO transferRequest) throws CardNotFoundException, SameCardTransferException, NotEnoughFundsException {

        Long userId = getCurrentUserId();
        transferService.makeTransferBetweenUserCards(
                userId,
                transferRequest.getSourceCardId(),
                transferRequest.getTargetCardId(),
                transferRequest.getAmount(),
                transferRequest.getComment()
        );
        return ResponseEntity.noContent().build();
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).getId();
        }
        throw new IllegalStateException("Principal is not of type User");
    }
}