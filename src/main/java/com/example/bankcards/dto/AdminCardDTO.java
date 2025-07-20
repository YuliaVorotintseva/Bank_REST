package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Административное представление карты")
public class AdminCardDTO {
    @Schema(description = "ID карты", example = "1")
    private Long id;

    @Schema(description = "Полный номер карты", example = "1234567890123456")
    private String fullNumber;

    @Schema(description = "Имя владельца", example = "IVAN IVANOV", required = true)
    private String ownerName;

    @Schema(description = "Срок действия", example = "2025-12-31")
    private String expiryDate;

    @Schema(description = "Баланс", example = "1000.00")
    private Double balance;

    @Schema(description = "Статус", example = "ACTIVE", allowableValues = {"ACTIVE", "BLOCKED", "EXPIRED"})
    private String status;

    @Schema(description = "ID владельца", example = "5")
    private Long userId;

    public void setId(Long id) {
        this.id = id;
    }

    public void setFullNumber(String fullNumber) {
        this.fullNumber = fullNumber;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}