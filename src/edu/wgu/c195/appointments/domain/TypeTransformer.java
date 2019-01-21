package edu.wgu.c195.appointments.domain;

@FunctionalInterface
public interface TypeTransformer<T,K> {

    K transform(T instance);

}
