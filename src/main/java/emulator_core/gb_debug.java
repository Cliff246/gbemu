package emulator_core;

import java.security.InvalidParameterException;
import java.util.Vector;

import emulator_core.*;

public class gb_debug {

    static int[] sz_toks(String in, char[] look) {
        if (in == null)
            throw new InvalidParameterException("in string is not defined\n");
        else if (look == null)
            throw new InvalidParameterException("look up char array is not defined\n");
        else if (look.length == 0)
            return new int[] {};
        else {
            int[] tokens = new int[look.length];
            for (int i = 0; i < in.length(); i++) {
                if (in.charAt(i) == look[i])
                    tokens[i]++;
            }
            return tokens;
        }
    }

    public final byte[] debug_memrange(gb_memory gbmem, int start, int end) {
        return gbmem.get_byterange(start, end);
    }

    public final byte debug_memindex(gb_memory gbmem, int index) {
        return gbmem.get_byte(index);
    }

}
