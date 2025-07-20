package com.example.bankcards.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Номер карты обязателен")
    @Column(name = "card_number", nullable = false, unique = true)
    @ColumnTransformer(
            read = "pgp_sym_decrypt(card_number, '${encryption.secret}')",
            write = "pgp_sym_encrypt(?, '${encryption.secret}')"
    )
    private String cardNumber; // Хранится зашифрованным в БД

    @NotBlank(message = "Имя владельца обязательно")
    @Size(min = 2, max = 100, message = "Имя владельца должно быть от 2 до 100 символов")
    @Column(nullable = false)
    private String ownerName;

    @Future(message = "Срок действия должен быть в будущем")
    @Column(name = "expiry_date", nullable = false)
    private String expiryDate;

    @DecimalMin(value = "0.0", message = "Баланс не может быть отрицательным")
    @Column(nullable = false)
    private Double balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // Связь с владельцем

    // Метод для получения маскированного номера
    @Transient
    public String getMaskedNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "**** **** **** ****";
        }
        String last4 = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + last4;
    }

    public enum CardStatus {
        ACTIVE("Активна"),
        BLOCKED("Заблокирована"),
        EXPIRED("Истек срок");

        private final String displayName;

        CardStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
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

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
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

    public Double getBalance() {
        return balance;
    }

    public CardStatus getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }
}