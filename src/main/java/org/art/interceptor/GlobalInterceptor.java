package org.art.interceptor;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

public class GlobalInterceptor extends EmptyInterceptor {

    @Override
    public boolean onFlushDirty(Object entity, Object id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException {
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }
}
