package emulator_core;

import java.security.InvalidParameterException;
import java.util.Vector;

public class vertex<T extends Object> {
    private int length;
    private Vector<T> data;

    public vertex() {
        throw new InvalidParameterException("args must be a valid array");
    }

    public vertex(int size) {
        data = new Vector<T>(size);
    }

    @SafeVarargs
    public vertex(T... args) {
        for (int i = 0; i < args.length; i++)
            data.add(args[i]);
        length = args.length;
    }

    public void set_at(T _data, int index) {
        if (index >= length || index < 0)
            throw new NullPointerException("array out of range");
        else
            data.set(index, _data);
    }

    public T get_at(int index) {

        if (index >= length || index < 0)
            return data.get(index);
        else
            return data.get(index);
    }

    @SuppressWarnings(value = "unchecked")
    public T[] get_data_list() {

        return (T[]) data.toArray();
    }

    public int get_length() {
        return data.size();
    }

    public void clear() {
        data.clear();
    }
}
