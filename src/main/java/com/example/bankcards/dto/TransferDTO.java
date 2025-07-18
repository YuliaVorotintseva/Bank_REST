package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TransferDTO {
    @Schema(description = "ID карты отправителя", example = "1")
    @NotNull(message = "Card from ID cannot be null")
    private Long sourceCardId;

    @Schema(description = "ID карты получателя", example = "2")
    @NotNull(message = "Card to ID cannot be null")
    private Long targetCardId;

    @Schema(description = "Сумма перевода", example = "500.00")
    @Positive(message = "Amount must be positive")
    @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
    private Double amount;

    @Schema(description = "Комментарий", example = "За услуги")
    @Size(max = 255, message = "Comment must be less than 255 characters")
    private String comment;
}