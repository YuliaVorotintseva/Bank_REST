package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Set;

@Data
@Schema(description = "Административное представление пользователя")
public class UserDTO {
    @Schema(description = "ID пользователя", example = "1")
    private Long id;

    @Schema(description = "Логин", example = "ivan.ivanov")
    private String username;

    @Schema(description = "Email", example = "ivan@gmail.com")
    private String email;

    @Schema(description = "Роли", example = "[\"USER\"]")
    private Set<Role> roles;

    @Schema(description = "Статус", example = "true")
    private Boolean active;

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}