package edu.wgu.c195.appointments.domain;

import java.math.BigDecimal;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static edu.wgu.c195.appointments.domain.Pair.cons;
import static edu.wgu.c195.appointments.domain.TransformerService.convert;
import static java.util.Optional.ofNullable;

public class DefaultTuple extends AbstractList<Pair<String, ? extends Object>> implements Tuple {

    //note the optional is untyped, this honestly makes working with tuple as a whole easier than using wildcards
    Pair<String, ?>[] list;


    public DefaultTuple(Pair<String, ?>... list) {
        this.list = list;
    }

    public DefaultTuple(Collection<Pair<String, ?>> values) {
        this.list = new Pair[values.size()];
        int cnt = 0;
        for (Pair<String, ?> value : values) {
            this.list[cnt++] = value;
        }
    }



    public static Tuple tuple(Pair<String, ?>... list) {
        return new DefaultTuple(list);
    }


    @Override
    public <T> Optional<T> val(String name) {
        return (Optional<T>) val(name, Object.class);
    }

    @Override
    public <T> Optional<T> val(int index) {
        assert index > -1 && index < list.length;
        return (Optional<T>) val(index, Object.class);
    }

    @Override
    public <T> Optional<T> val(int index, Class<T> type) {
        assert index > -1 && index < list.length;
        return (Optional<T>) ofNullable(list[index].getCdr());
    }


    @Override
    public long asLong(String name) {
        return val(name).map((val) -> convert(val, Long.class)).orElse(0L);
    }

    @Override
    public float asFloat(String name) {
        return val(name).map((val) -> convert(val, Float.class)).orElse(0.0f);
    }

    @Override
    public double asDouble(String name) {
        return val(name).map((val) -> convert(val, Double.class)).orElse(0.0);
    }

    @Override
    public BigDecimal asBigDecimal(String name) {
        return val(name).map((val) -> convert(val, BigDecimal.class)).orElse(null);
    }

    @Override
    public short asShort(String name) {
        return val(name).map((val) -> convert(val, Short.class)).orElse((short) 0);
    }

    @Override
    public byte asByte(String name) {
        return val(name).map((val) -> convert(val, Byte.class)).orElse((byte) 0);
    }

    @Override
    public boolean asBoolean(String name) {
        return val(name).map((val) -> convert(val, Boolean.class)).orElse(false);
    }

    @Override
    public char asChar(String name) {
        return val(name).map((val) -> convert(val, Character.class)).orElse((char) 0);
    }


    @Override
    public int asInt(String name) {
        return val(name).map((val) -> convert(val, Integer.class)).orElse(0);
    }

    @Override
    public int asInt(int index) {
        return val(index).map((val) -> convert(val, Integer.class)).orElse(0);
    }

    @Override
    public float asFloat(int index) {
        return val(index).map((val) -> convert(val, Float.class)).orElse(0.0f);
    }

    @Override
    public double asDouble(int index) {
        return val(index).map((val) -> convert(val, Double.class)).orElse(0.0);
    }

    @Override
    public BigDecimal asBigDecimal(int index) {
        return val(index).map((val) -> convert(val, BigDecimal.class)).orElse(null);
    }

    @Override
    public long asLong(int index) {
        return (long) val(index).map((val) -> convert(val, Long.class)).orElse(0L);
    }

    @Override
    public short asShort(int index) {
        return (short) val(index).map((val) -> convert(val, Short.class)).orElse((short) 0);
    }

    @Override
    public byte asByte(int index) {
        return (byte) val(index).map((val) -> convert(val, Byte.class)).orElse((byte) 0);
    }

    @Override
    public boolean asBoolean(int index) {
        return (boolean) val(index).map((val) -> convert(val, Boolean.class)).orElse(false);
    }

    @Override
    public char asChar(int index) {
        return (char) val(index).map((val) -> convert(val, Character.class)).orElse((char) 0);
    }

    @Override
    public <T> Optional<T> val(String name, Class<T> type) {
        for (Pair<String, ?> stringPair : list) {
            if (name.equalsIgnoreCase(stringPair.getCar())) {
                return ofNullable(convert(stringPair.getCdr(), type));
            }
        }
        return Optional.empty();
    }

    @Override
    public Tuple createFrom(Collection<Pair<String, ? extends Object>> values) {
        return new DefaultTuple(values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple)) return false;
        if (!super.equals(o)) return false;

        DefaultTuple pairs = (DefaultTuple) o;

        if (!Arrays.equals(list, pairs.list)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (list != null ? Arrays.hashCode(list) : 0);
        return result;
    }

    @Override
    public Pair<String,Object> get(int index) {
        return (Pair<String, Object>) list[index];
    }

    @Override
    public int size() {
        return list.length;
    }

    public static Pair<String, ?> tCons(String name, Object value) {
        return cons(name, value);
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("(");
        forEach((Pair<String, ?> pair) -> {
            builder.append(pair.toString()).append(',');
        });
        return builder.deleteCharAt(builder.length() - 1).append(')').toString();

    }

}
