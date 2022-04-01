package emulator_core;


import java.security.InvalidParameterException;

import org.w3c.dom.ranges.RangeException;


public class gb_memory extends Thread
{
    private gb_bus gbbus;
    public final int memlength = 2000;
    private byte [] m_memory;

    public gb_memory(Thread _thread, gb_handle _handler, gb_bus _gbbus) 
    {
        gbbus = _gbbus;
    }

    public void snoopbus(int[]data, int address)
    {

    }

    public byte get_byte(int index)
    {  
        //why is there no unsigned values
        if(index < 0)
            throw new InvalidParameterException("index cannot be negative\n");
        else if(index >= memlength)
            throw new InvalidParameterException("arguments cannot be greater then WORD MAX\n");
        return m_memory[index];
    }

    public void set_byte(int index, byte set)
    {
        if(index < 0)
            throw new InvalidParameterException("index cannot be negative\n");
        else if(index >= memlength)
            throw new InvalidParameterException("arguments cannot be greater then WORD MAX\n");
        m_memory[index] = set;
    }

    public byte[] get_byterange(int start, int end)
    {
        if(start < 0 || end < 0)
            throw new InvalidParameterException("arguments cannot be negative\n");
        if(start >= memlength | end >= memlength)
            throw new InvalidParameterException("arguments cannot be greater then WORD MAX\n");
        int range = start - end;
        if(range > 0)
        {
            byte []temp = new byte[range];
            System.arraycopy(m_memory, start, temp, 0, range);
            return temp;

        }
        else
            throw new RangeException(RangeException.BAD_BOUNDARYPOINTS_ERR, "invalid range selection");
    }

  

   



}
