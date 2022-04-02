package emulator_core;

import java.security.InvalidParameterException;

import org.javatuples.Pair;
import org.w3c.dom.ranges.RangeException;

public class gb_memory extends gb_components {

    private gb_bus gbbus;
    private int dmemlen;

    private int memstart, memend;
    private byte[] memory;

    public gb_memory() {
        memstart = 0;
        memend = 0;
        dmemlen = 0;
        memory = null;
    }

    public gb_memory(int _memstart, int _memend) {
        setup(_memstart, _memend);
    }

    public void setup(int _memstart, int _memend) {

        memstart = _memstart;
        memend = _memend;
        if (memstart >= memend) {
            dmemlen = 0;
            memory = null;
        } else {
            dmemlen = memend - memstart;
            memory = new byte[dmemlen];
        }
    }

    public void snoopbus(int[] data, int address) {

    }

    public byte __get_byte(int index) {
        // why is there no unsigned values
        if (index < 0)
            throw new InvalidParameterException("index cannot be negative\n");
        else if (index >= dmemlen)
            throw new InvalidParameterException("arguments cannot be greater then WORD MAX\n");
        return memory[index];
    }

    public void __set_byte(int index, byte set) {
        if (index < 0)
            throw new InvalidParameterException("index cannot be negative\n");
        else if (index >= dmemlen)
            throw new InvalidParameterException("arguments cannot be greater then WORD MAX\n");
        memory[index] = set;
    }

    public byte[] __get_byterange(int start, int end) {
        if (start < 0 || end < 0)
            throw new InvalidParameterException("arguments cannot be negative\n");
        if (start >= dmemlen | end >= dmemlen)
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
