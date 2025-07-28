package io.github.nikoir.expensetracker.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "currencies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Currency {

    @Id
    @Column(name = "code", length = 3)
    private String code;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "symbol")
    private String symbol;
}