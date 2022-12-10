package org.art.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(exclude = "version")
@Builder
@Entity
//@OptimisticLocking(type = OptimisticLockType.ALL)
//@DynamicUpdate
public class Payment extends AuditableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Version
//    private Long version;

    @Column(nullable = false)
    private Integer amount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    // в метод не нужно передавать сущность, т.к. мы и так в ней и тут ссылка this укажет на нее
    // унесли в AuditableEntity, т.к. там ему и место
//    @PrePersist
//    public void prePersist() {
//        setCreatedAt(Instant.now());
//        // в реальных приложениях это могло бы быть setCreatedBy(SecurityContext.getUser());
//        setCreatedBy("set in PrePersist");
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        setUpdatedAt(Instant.now());
//        setUpdatedBy("set in PreUpdate");
//    }
}
