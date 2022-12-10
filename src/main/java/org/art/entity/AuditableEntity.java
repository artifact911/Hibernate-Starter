package org.art.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditableEntity<T extends Serializable> implements BaseEntity<T> {

    // instant заюзали для примера показать, что он маппится на timestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by")
    private String createdBy;


    private Instant updatedAt;

    private String updatedBy;

    // в метод не нужно передавать сущность, т.к. мы и так в ней и тут ссылка this укажет на нее
    @PrePersist
    public void prePersist() {
        setCreatedAt(Instant.now());
        // в реальных приложениях это могло бы быть setCreatedBy(SecurityContext.getUser());
        setCreatedBy("set in PrePersist");
    }

    @PreUpdate
    public void preUpdate() {
        setUpdatedAt(Instant.now());
        setUpdatedBy("set in PreUpdate");
    }
}
