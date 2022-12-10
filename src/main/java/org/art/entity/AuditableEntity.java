package org.art.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.art.listener.AuditDatesListener;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditDatesListener.class)
public abstract class AuditableEntity<T extends Serializable> implements BaseEntity<T> {

    // instant заюзали для примера показать, что он маппится на timestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by")
    private String createdBy;


    private Instant updatedAt;

    private String updatedBy;
}
