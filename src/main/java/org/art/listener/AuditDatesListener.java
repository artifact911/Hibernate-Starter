package org.art.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.art.entity.AuditableEntity;

import java.time.Instant;

public class AuditDatesListener {

    @PrePersist
    public void prePersist(AuditableEntity<?> entity) {
        entity.setCreatedAt(Instant.now());
        // в реальных приложениях это могло бы быть setCreatedBy(SecurityContext.getUser());
        entity.setCreatedBy("set in PrePersist");
    }

    @PreUpdate
    public void preUpdate(AuditableEntity<?> entity) {
        entity.setUpdatedAt(Instant.now());
        entity.setUpdatedBy("set in PreUpdate");
    }
}
