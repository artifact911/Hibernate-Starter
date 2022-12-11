package org.art.listener;

import org.art.entity.Revision;
import org.hibernate.envers.RevisionListener;

public class ArtRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        ((Revision) revisionEntity).setUserName("User set in " + this.getClass().getName());
    }
}
