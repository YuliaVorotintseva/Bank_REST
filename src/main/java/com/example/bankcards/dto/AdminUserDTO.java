package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Set;

@Data
@Schema(description = "Административное представление пользователя")
public class AdminUserDTO {
    @Schema(description = "ID пользователя", example = "1")
    private Long id;

    @Schema(description = "Логин", example = "ivan.ivanov")
    private String username;

    @Schema(description = "Email", example = "ivan@gmail.com")
    private String email;

    @Schema(description = "Роли", example = "[\"USER\"]")
    private Set<String> roles;

    @Schema(description = "Статус", example = "true")
    private Boolean active;
}