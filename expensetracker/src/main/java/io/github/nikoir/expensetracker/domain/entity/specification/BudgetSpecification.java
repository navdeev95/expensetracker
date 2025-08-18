package io.github.nikoir.expensetracker.domain.entity.specification;

import io.github.nikoir.expensetracker.domain.entity.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.*;

public class BudgetSpecification {
    public static Specification<Budget> isDeleted(Boolean isDeleted) {
        return (root, query, cb) ->
                isDeleted == null ? null : cb.equal(root.get("isDeleted"), isDeleted);
    }
    public static Specification<Budget> hasCategoryId(Long categoryId) {
        return (root, query, cb) -> {
            fetchCategory(root);

            if (categoryId == null) {
                return null;
            }

            return cb.equal(root.get("category").get("id"), categoryId);
        };
    }
    public static Specification<Budget> hasCurrencyCode(String currencyCode) {
        return (root, query, cb) -> {
            root.fetch("currency", JoinType.INNER);

            if (currencyCode == null) {
                return null;
            }

            return cb.equal(root.get("currency").get("code"), currencyCode);
        };
    }
    public static Specification<Budget> hasPeriodCode(String periodCode) {
        return (root, query, cb) -> {
            root.fetch("budgetPeriod", JoinType.INNER);

            if (periodCode == null) {
                return null;
            }
            return cb.equal(root.get("budgetPeriod").get("code"), periodCode);
        };
    }
    public static Specification<Budget> hasAmountBetween(BigDecimal minAmount, BigDecimal maxAmount) {
        return (root, query, cb) -> {
            if (minAmount == null && maxAmount == null) return null;

            Path<BigDecimal> amount = root.get("amount");
            if (minAmount != null && maxAmount != null) {
                return cb.between(amount, minAmount, maxAmount);
            } else if (minAmount != null) {
                return cb.greaterThanOrEqualTo(amount, minAmount);
            } else {
                return cb.lessThanOrEqualTo(amount, maxAmount);
            }
        };
    }
    public static Specification<Budget> hasDateBetween(LocalDate periodFrom, LocalDate periodTo, ZoneId zoneId) {
        return (root, query, cb) -> {
            if (periodFrom == null && periodTo == null) return null;

            Instant instantFrom = null;
            Instant instantTo = null;

            if (periodFrom != null) {
                instantFrom = ZonedDateTime
                        .of(periodFrom, LocalTime.MIDNIGHT, zoneId)
                        .toInstant();
            }

            if (periodTo != null) {
                instantTo = ZonedDateTime
                        .of(periodTo, LocalTime.MAX, zoneId)
                        .toInstant();
            }

            Path<Instant> startDate = root.get("startDate");
            Path<Instant> endDate = root.get("endDate");

            if (instantFrom != null && instantTo != null) {
                //оба зпполнены - интервалы пересекаются
                return cb.and(
                        cb.lessThanOrEqualTo(startDate, instantFrom),
                        cb.greaterThanOrEqualTo(endDate, instantTo)
                );
            } else if (instantFrom != null) {
                //заполнена только дата начала - бюджет который активен на текущий момент
                return cb.or(
                        cb.isNull(endDate), //бессрочный период
                        cb.greaterThanOrEqualTo(endDate, instantFrom)
                );
            } else {
                //заполнена только дата окончания - бюджеты которые начались до даты окончания
                return cb.lessThanOrEqualTo(startDate, instantTo);
            }
        };
    }
    public static Specification<Budget> hasUserId(String userId) {
        return (root, query, cb) -> {
            Fetch<Budget, Category> categoryFetch = fetchCategory(root);
            categoryFetch.fetch("user", JoinType.INNER);

            return cb.equal(root.get("category").get("user").get("id"), userId);
        };
    }

    //чтобы fetch не переиспользовался
    private static Fetch<Budget, Category> fetchCategory(Root<Budget> root) {
        return root.getFetches().stream()
                .filter(fetch -> "category".equals(fetch.getAttribute().getName()))
                .map(fetch -> (Fetch<Budget, Category>) fetch)
                .findFirst()
                .orElseGet(() -> root.fetch("category", JoinType.INNER));
    }
}
