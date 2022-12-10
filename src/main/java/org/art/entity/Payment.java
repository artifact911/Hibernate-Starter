package org.art.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(exclude = "version")
@Builder
@Entity
//@OptimisticLocking(type = OptimisticLockType.ALL)
//@DynamicUpdate
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Payment extends AuditableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Version
//    private Long version;

    @Column(nullable = false)
    private Integer amount;

//    @NotAudited
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;
}
