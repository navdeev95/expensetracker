package io.github.nikoir.expensetracker.domain.entity.base;

import io.github.nikoir.expensetracker.domain.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Optional;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class ModifiedBaseEntity {
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @Setter
    private User createdBy;

    public Optional<User> getCreatedBy() {
        return Optional.ofNullable(this.createdBy);
    }

    @Getter
    @Setter
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    @Setter
    private User updatedBy;

    public Optional<User> getUpdatedBy() {
        return Optional.ofNullable(this.updatedBy);
    }

    @Getter
    @Setter
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
