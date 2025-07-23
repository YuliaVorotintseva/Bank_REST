package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;

import org.springframework.lang.NonNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    @NonNull Optional<Card> findById(@NonNull Long id);

    List<Card> findByUserId(Long userId);

    boolean existsByCardNumber(String cardNumber);

    Page<Card> findAll(Specification<Card> cardSpecification, Pageable pageable);
}