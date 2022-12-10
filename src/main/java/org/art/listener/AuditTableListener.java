package org.art.listener;

import org.art.entity.Audit;
import org.hibernate.event.spi.*;

import java.io.Serializable;

public class AuditTableListener implements PreDeleteEventListener, PreInsertEventListener {

    @Override
    public boolean onPreDelete(PreDeleteEvent event) {
        auditEntity(event, Audit.Operation.DELETE);
        return false; // значит нам не нужно завршать эту опреацию - все идет свои чередом
    }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        auditEntity(event, Audit.Operation.INSERT);
        return false;
    }

    private void auditEntity(AbstractPreDatabaseOperationEvent event, Audit.Operation operation) {
        if(event.getEntity().getClass() != Audit.class) {
            var audit = Audit.builder()
                    .entityId((Serializable) event.getId())
                    .entityName(event.getClass().getName())
                    .entityContent(event.getEntity().toString())
                    .operation(operation)
                    .build();
            event.getSession().save(audit);
        }
    }
}
