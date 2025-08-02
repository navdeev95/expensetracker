package io.github.nikoir.expensetracker.domain.entity;

import io.github.nikoir.expensetracker.domain.entity.base.ModifiedBaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends ModifiedBaseEntity {
    @Id
    @Column(name = "id", length = 36)
    private String id;
}
