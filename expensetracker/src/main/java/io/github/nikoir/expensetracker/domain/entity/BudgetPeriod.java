package io.github.nikoir.expensetracker.domain.entity;

import io.github.nikoir.expensetracker.domain.entity.base.ModifiedBaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "budget_periods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetPeriod extends ModifiedBaseEntity {
    @Id
    @Column(name = "code", length = 20)
    private String code;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "description")
    private String description;

    @Version
    @Column(name = "version")
    private Long version;
}
