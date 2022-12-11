package org.art.mapper;

public interface Mapper<F, T> {

    T mapFrom(F object);
}
