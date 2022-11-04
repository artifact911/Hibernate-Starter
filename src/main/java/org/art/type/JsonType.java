package org.art.type;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Если мы создаем какой-то свой кастомный тип данных на который нет конвертера, то нам нужно реализовать интерфейс UserType
 * ЭТОТ КЛАСС  НАПИСАН ДЛЯ ПРИМЕРА И В КОДЕ ПОКА НЕ ИСПОЛЬЗУЕТСЯ НИГДЕ!!!!
 */

public class JsonType implements UserType {

    @Override
    public int getSqlType() {
        return 0;
    }

    @Override
    public Class returnedClass() {
        return null;
    }

    @Override
    public boolean equals(Object x, Object y) {
        return false;
    }

    @Override
    public int hashCode(Object x) {
        return 0;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {

    }

    @Override
    public Object deepCopy(Object value) {
        return null;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) {
        return null;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) {
        return null;
    }

    @Override
    public Object replace(Object detached, Object managed, Object owner) {
        return null;
    }
}
