package com.example.bankcards.util;

import com.example.bankcards.entity.Card;
import org.springframework.data.jpa.domain.Specification;

public class CardSpecifications {
    public static Specification<Card> withFilters(String status, Long userId) {
        return Specification.where(withStatus(status))
                .and(withUserId(userId));
    }

    private static Specification<Card> withStatus(String status) {
        if (status == null) return null;
        return (root, query, cb) -> cb.equal(root.get("status"), Card.CardStatus.valueOf(status));
    }

    private static Specification<Card> withUserId(Long userId) {
        if (userId == null) return null;
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }
}