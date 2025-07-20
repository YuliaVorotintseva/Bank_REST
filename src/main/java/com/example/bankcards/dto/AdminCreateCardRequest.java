package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание карты администратором")
public class AdminCreateCardRequest {
    @Schema(description = "ID пользователя", example = "5")
    @NotNull
    private Long userId;

    @Schema(description = "Полный номер карты", example = "1234567890123456")
    @Pattern(regexp = "^[0-9]{16}$", message = "Номер карты должен содержать 16 цифр")
    private String cardNumber;

    @Schema(description = "Имя владельца", example = "IVAN IVANOV")
    @NotBlank
    private String ownerName;

    @Schema(description = "Срок действия (MM/yy)", example = "12/25")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/[0-9]{2}$", message = "Неверный формат срока действия")
    private String expiryDate;

    @Schema(description = "Начальный баланс", example = "0.00")
    @DecimalMin("0.00")
    private Double initialBalance;

    public Long getUserId() {
        return userId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public Double getInitialBalance() {
        return initialBalance;
    }
}