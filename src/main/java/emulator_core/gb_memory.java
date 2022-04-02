package emulator_core;

import java.security.InvalidParameterException;

import org.w3c.dom.ranges.RangeException;

public class gb_memory extends Thread {

    public final int __mem_range_start__ = 0;
    public final int __mem_range_end__ = 1;
    private gb_bus gbbus;
    private gb_handle handle;
    private int dmemlength;
    private Thread thread;
    private vertex<Integer> memrange;
    private byte[] memory;

    private int get_memstart() {
        return memrange.get_at(__mem_range_start__);
    }

    private int get_memend() {
        return memrange.get_at(__mem_range_end__);
    }

    public gb_memory(Thread _thread, gb_handle _handle, gb_bus _gbbus, vertex<Integer> _memrange) {
        thread = _thread;
        handle = _handle;
        gbbus = _gbbus;
        memrange = _memrange;

        int start = memrange.get_at(__mem_range_start__);
        int end = memrange.get_at(__mem_range_end__);
        dmemlength = start - end;
        memory = new byte[dmemlength];

    }

    public void snoopbus(int[] data, int address) {
        int start = get_memstart();
        int end = get_memend();

    }

    public byte __get_byte(int index) {
        // why is there no unsigned values
        if (index < 0)
            throw new InvalidParameterException("index cannot be negative\n");
        else if (index >= dmemlength)
            throw new InvalidParameterException("arguments cannot be greater then WORD MAX\n");
        return memory[index];
    }

    public void __set_byte(int index, byte set) {
        if (index < 0)
            throw new InvalidParameterException("index cannot be negative\n");
        else if (index >= dmemlength)
            throw new InvalidParameterException("arguments cannot be greater then WORD MAX\n");
        memory[index] = set;
    }

    public byte[] __get_byterange(int start, int end) {
        if (start < 0 || end < 0)
            throw new InvalidParameterException("arguments cannot be negative\n");
        if (start >= dmemlength | end >= dmemlength)
            throw new InvalidParameterException("arguments cannot be greater then WORD MAX\n");
        int range = start - end;
        if (range > 0) {
            byte[] temp = new byte[range];
            System.arraycopy(memory, start, temp, 0, range);
            return temp;

        } else
            throw new RangeException(RangeException.BAD_BOUNDARYPOINTS_ERR, "invalid range selection");
    }

}
