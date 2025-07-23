package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.exception.customExceptions.cardExceptions.CardNotFoundException;
import com.example.bankcards.exception.customExceptions.transferExceptions.NotEnoughFundsException;
import com.example.bankcards.exception.customExceptions.transferExceptions.SameCardTransferException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransferService {
    private final CardRepository cardRepository;
    private final TransferRepository transferRepository;

    public TransferService(CardRepository cardRepository, TransferRepository transferRepository) {
        this.cardRepository = cardRepository;
        this.transferRepository = transferRepository;
    }

    @Transactional
    public void makeTransferBetweenUserCards(
            Long userId,
            Long sourceCardId,
            Long targetCardId,
            Double amount,
            String comment
    ) throws CardNotFoundException, SameCardTransferException, NotEnoughFundsException {
        if (sourceCardId.equals(targetCardId)) {
            throw new SameCardTransferException();
        }

        Card sourceCard = cardRepository.findById(sourceCardId)
                .orElseThrow(() -> new CardNotFoundException(sourceCardId));

        Card targetCard = cardRepository.findById(targetCardId)
                .orElseThrow(() -> new CardNotFoundException(targetCardId));

        if (sourceCard.getBalance().compareTo(amount) < 0) {
            throw new NotEnoughFundsException(sourceCardId, sourceCard.getBalance());
        }

        sourceCard.setBalance(sourceCard.getBalance() - amount);
        targetCard.setBalance(targetCard.getBalance() + amount);

        Transfer transfer = new Transfer();
        transfer.setSourceCard(sourceCard);
        transfer.setTargetCard(targetCard);
        transfer.setAmount(amount);
        transfer.setComment(comment);
        transfer.setStatus(Transfer.TransferStatus.COMPLETED);

        transferRepository.save(transfer);
        cardRepository.saveAll(List.of(sourceCard, targetCard));
    }
}