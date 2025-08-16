package io.github.nikoir.expensetracker.domain.entity.specification;

import io.github.nikoir.expensetracker.domain.entity.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    public static Specification<Budget> hasDateBetween(LocalDate periodFrom, LocalDate periodTo) {
        return (root, query, cb) -> {
            if (periodFrom == null && periodTo == null) return null;

            Path<LocalDate> startDate = root.get("startDate");
            Path<LocalDate> endDate = root.get("endDate");

            if (periodFrom != null && periodTo != null) {
                //оба зпполнены - интервалы пересекаются
                return cb.and(
                        cb.lessThanOrEqualTo(startDate, periodTo),
                        cb.greaterThanOrEqualTo(endDate, periodFrom)
                );
            } else if (periodFrom != null) {
                //заполнена только дата начала - бюджет который активен на текущий момент
                return cb.or(
                        cb.isNull(endDate), //бессрочный период
                        cb.greaterThanOrEqualTo(endDate, periodFrom)
                );
            } else {
                //заполнена только дата окончания - бюджеты которые начались до даты окончания
                return cb.lessThanOrEqualTo(startDate, periodTo);
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
