package emulator_core;

import java.security.InvalidParameterException;

import org.javatuples.Pair;
import org.w3c.dom.ranges.RangeException;

public class gb_memory extends gb_components {

    private gb_bus gbbus;
    private int dmemlength;

    private Pair<Integer, Integer> memrange;
    private byte[] memory;

    private int get_memstart() {
        return memrange.getValue0();
    }

    private int get_memend() {
        return memrange.getValue1();
    }

    public gb_memory(Pair<Integer, Integer> _memrange) {

        memrange = _memrange;

        int start = memrange.getValue0();
        int end = memrange.getValue1();
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
