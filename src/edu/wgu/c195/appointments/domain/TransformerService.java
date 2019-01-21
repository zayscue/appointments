package edu.wgu.c195.appointments.domain;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static edu.wgu.c195.appointments.domain.Pair.cons;


public class TransformerService {

    static Map<Pair<Class<?>, Class<?>>, TypeTransformer<?, ?>> CONVERSION_REGISTRY;

    static {
        registerConversion(String.class, Long.class, Long::valueOf);
        registerConversion(String.class, Long.TYPE, Long::valueOf);
        registerConversion(String.class, Integer.class, Integer::valueOf);
        registerConversion(String.class, Integer.TYPE, Integer::valueOf);
        registerConversion(String.class, Byte.class, Byte::valueOf);
        registerConversion(String.class, Byte.TYPE, Byte::valueOf);
        registerConversion(String.class, Short.class, Short::valueOf);
        registerConversion(String.class, Short.TYPE, Short::valueOf);
        registerConversion(String.class, Boolean.class, Transformations::toBoolean);
        registerConversion(String.class, Boolean.TYPE, Transformations::toBoolean);
        registerConversion(String.class, Float.class, Float::valueOf);
        registerConversion(String.class, Float.TYPE, Float::valueOf);
        registerConversion(String.class, Double.class, Double::valueOf);
        registerConversion(String.class, Double.TYPE, Double::valueOf);
        registerConversion(String.class, Character.class, (instance)-> (instance!= null) ? instance.charAt(0) : (char)(byte)0);
        registerConversion(String.class, Character.TYPE, (instance)-> (instance!= null) ? instance.charAt(0) : (char)(byte)0);
        registerConversion(String.class, Double.TYPE, Double::valueOf);
        registerConversion(String.class, BigDecimal.class, BigDecimal::new);
        registerConversion(Number.class, Long.class, (instance)-> Transformations.toNumber(instance, Long.class));
        registerConversion(Number.class, Long.TYPE, (instance)-> Transformations.toNumber(instance, Long.class));
        registerConversion(Number.class, Integer.class, (instance)-> Transformations.toNumber(instance, Integer.class));
        registerConversion(Number.class, Integer.TYPE, (instance)-> Transformations.toNumber(instance, Integer.class));
        registerConversion(Number.class, Byte.class, (instance)-> Transformations.toNumber(instance, Byte.class));
        registerConversion(Number.class, Byte.TYPE, (instance)-> Transformations.toNumber(instance, Byte.class));
        registerConversion(Number.class, Short.class, (instance)-> Transformations.toNumber(instance, Short.class));
        registerConversion(Number.class, Short.TYPE, (instance)-> Transformations.toNumber(instance, Short.class));
        registerConversion(Number.class, Boolean.class, Transformations::toBoolean);
        registerConversion(Number.class, Boolean.TYPE, Transformations::toBoolean);
        registerConversion(Number.class, Float.class, (instance)-> Transformations.toNumber(instance, Float.class));
        registerConversion(Number.class, Float.TYPE, (instance)-> Transformations.toNumber(instance, Float.class));
        registerConversion(Number.class, Double.class, (instance)-> Transformations.toNumber(instance, Double.class));
        registerConversion(Number.class, Double.TYPE, (instance)-> Transformations.toNumber(instance, Double.class));
        registerConversion(Number.class, BigDecimal.class, Transformations::toBigDecimal);
        registerConversion(Object.class, String.class, Object::toString);
        registerConversion(Character.class, Boolean.class, Transformations::toBoolean);
        registerConversion(Number.class, Boolean.class, Transformations::toBoolean);
        registerConversion(Timestamp.class, Date.class, (instance -> new Date(instance.getTime())));
    }


    static Map<Pair<Class<?>, Class<?>>, TypeTransformer<?, ?>> getConversionRegistry() {
        if (CONVERSION_REGISTRY == null) {
            CONVERSION_REGISTRY = new HashMap<>();
        }
        return CONVERSION_REGISTRY;
    }

    public static <T, K> void registerConversion(Class<T> source, Class<K> target, TypeTransformer<T, K> transformer) {
        getConversionRegistry().put(cons((Class<?>) source, (Class<?>) target), transformer);
    }

    static TypeTransformer resolveTransformer(boolean isNull, Class<?> source, Class<?> target) {


        TypeTransformer<?, ?> typeTransformer = findWideningTransformer(source, target);
        if (typeTransformer == null) {
            return new TypeTransformer() {
                @Override
                public Object transform(Object instance) {
                    if (target.isPrimitive()) {
                        try {
                            return target.getField("TYPE").get(null);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else if (isNull) {
                        return null;
                    } else {
                        throw new IllegalArgumentException("No Type Transformer registered for " + source.getName() + "->" + target.getName());
                    }
                }
            };
        }
        return typeTransformer;
    }

    private static TypeTransformer<?, ?> findWideningTransformer(Class<?> source, Class<?> target) {
        TypeTransformer<?, ?> typeTransformer = getConversionRegistry().get(cons(source, target));
        if (typeTransformer != null) {
            return typeTransformer;
        }

        //do a widening search
        Pair<Class<?>, Class<?>> transformerKey = getConversionRegistry()
                .keySet()
                .stream()
                .filter(key -> {
                    boolean sourceAssignable = key.getCar().isAssignableFrom(source);
                    boolean targetAssignable = key.getCdr().isAssignableFrom(target);
                    boolean fits = sourceAssignable && targetAssignable;
                    return fits;
                })
                .findFirst().orElse(null);
        return getConversionRegistry().get(transformerKey);
    }


    public static <T> T convert(Object instance, Class<T> target) {
        boolean isNull = instance == null;
        if (!isNull && (target.equals(instance.getClass()) || target.isAssignableFrom(instance.getClass()))) {
            return (T) instance;
        }

        return (T) resolveTransformer(isNull, (isNull) ? null : instance.getClass(), target).transform(instance);
    }

}
