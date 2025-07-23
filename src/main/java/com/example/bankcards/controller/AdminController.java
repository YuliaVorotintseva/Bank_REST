package com.example.bankcards.controller;

import com.example.bankcards.dto.AdminCreateCardRequest;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.exception.customExceptions.cardExceptions.CardAlreadyBlockedException;
import com.example.bankcards.exception.customExceptions.cardExceptions.CardAlreadyExistsException;
import com.example.bankcards.exception.customExceptions.cardExceptions.CardNotFoundException;
import com.example.bankcards.exception.customExceptions.userExceptions.UserAlreadyBlockedException;
import com.example.bankcards.exception.customExceptions.userExceptions.UserNotFoundException;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final CardService cardService;
    private final UserService userService;

    public AdminController(CardService cardService, UserService userService) {
        this.cardService = cardService;
        this.userService = userService;
    }

    @GetMapping("/cards")
    public ResponseEntity<Page<CardDTO>> getAllCards(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long userId,
            Pageable pageable) {
        return ResponseEntity.ok(cardService.getAllCards(userId, status, pageable));
    }

    @PostMapping("/cards")
    public ResponseEntity<CardDTO> createCardForUser(
            @RequestBody @Valid AdminCreateCardRequest request
    ) throws CardAlreadyExistsException {
        return ResponseEntity.ok(cardService.createCard(request));
    }

    @PatchMapping("/cards/{id}/block")
    public ResponseEntity<Void> adminBlockCard(@PathVariable Long id)
            throws CardAlreadyBlockedException, CardNotFoundException {
        cardService.blockCard(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/cards/{id}/activate")
    public ResponseEntity<Void> adminActivateCard(@PathVariable Long id) throws CardNotFoundException {
        cardService.activateCard(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cards/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) throws CardNotFoundException {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @PatchMapping("/users/{id}/block")
    public ResponseEntity<Void> blockUser(@PathVariable Long id)
            throws UserNotFoundException, UserAlreadyBlockedException {
        userService.blockUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{id}/roles")
    public ResponseEntity<Void> updateUserRoles(
            @PathVariable Long id,
            @RequestBody Set<String> roles
    ) throws UserNotFoundException {
        userService.updateUserRoles(id, roles);
        return ResponseEntity.noContent().build();
    }
}