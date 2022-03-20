package emulator_core;


import java.security.InvalidParameterException;

import src.app.emulator_core.*;

public class vertex <T> 
{
    private int length;
    private T[] data;

    public vertex ()
    {
        throw new InvalidParameterException("args must be a valid array");
    }

    @SafeVarargs vertex (T ... args)
    {
        data = args;
        length = args.length;                   
    }

    public void set_at(T _data, int index)
    {
        if(index >= length || index < 0)
            throw new NullPointerException("array out of range");
        else
            data[index] = _data;
    }

    public T get_at(int index)
    {
        if(index >= length || index < 0)
            return data[0];
        else
            return data[index];
    } 

    public T[] get_data_list()
    {
        return data;
    }

    public int get_length()
    {
        return data.length;
    }
    public void clear(){
        for(int i = 0; i < data.length; i++)
            data[i] = null;
    }
}
