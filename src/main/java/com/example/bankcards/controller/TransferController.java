package com.example.bankcards.controller;

import com.example.bankcards.service.TransferService;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.dto.TransferDTO;
import com.example.bankcards.service.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transfers")
@Tag(name = "Переводы", description = "Операции перевода между картами")
@SecurityRequirement(name = "JWT")
public class TransferController {

    private final TransferService transferService;
    private final CardRepository cardRepository;
    private final CurrentUserService currentUserService;
    private final TransferRepository transferRepository;

    @Autowired
    public TransferController(TransferService transferService, CardRepository cardRepository, CurrentUserService currentUserService, TransferRepository transferRepository) {
        this.transferService = transferService;
        this.cardRepository = cardRepository;
        this.currentUserService = currentUserService;
        this.transferRepository = transferRepository;
    }

    @Operation(summary = "Перевод между своими картами")
    @PostMapping
    public ResponseEntity<Void> transfer(
            @Valid @RequestBody TransferDTO transferRequest) throws Exception {
        Long userId = currentUserService.getCurrentUserId();
        transferService.makeTransferBetweenUserCards(
                userId,
                transferRequest.getSourceCardId(),
                transferRequest.getTargetCardId(),
                transferRequest.getAmount(),
                transferRequest.getComment()
        );
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить историю переводов")
    @GetMapping
    public ResponseEntity<Page<TransferDTO>> getTransferHistory(Pageable pageable) {
        Long userId = currentUserService.getCurrentUserId();
        List<Card> userCards = cardRepository.findByUserId(userId);
        List<Transfer> allTransfers = userCards.stream()
                .flatMap(card -> {
                    List<Transfer> sent = transferRepository.findBySourceCard(card);
                    List<Transfer> received = transferRepository.findByTargetCard(card);
                    sent.addAll(received);
                    return sent.stream();
                })
                .distinct()
                .collect(Collectors.toList());
        List<TransferDTO> dtos = allTransfers.stream().map(tr -> {
            TransferDTO dto = new TransferDTO();
            dto.setSourceCardId(tr.getSourceCard().getId());
            dto.setTargetCardId(tr.getTargetCard().getId());
            dto.setAmount(tr.getAmount());
            dto.setComment(tr.getComment());
            return dto;
        }).collect(Collectors.toList());
        PageImpl<TransferDTO> page = new PageImpl<>(dtos, pageable, dtos.size());
        return ResponseEntity.ok(page);
    }
}