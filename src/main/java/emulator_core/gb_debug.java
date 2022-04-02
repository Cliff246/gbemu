package emulator_core;

import java.security.InvalidParameterException;
import java.util.Vector;

public class gb_debug {

    public static int[] sz_toks(String in, char[] look) {
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

    public static byte[] debug_memrange(gb_memory gbmem, int start, int end) {
        return gbmem.__get_byterange(start, end);
    }

    public static byte debug_memindex(gb_memory gbmem, int index) {
        return gbmem.__get_byte(index);
    }

    public static Vector<int[]> opdatasend = new Vector<int[]>();
    public static Vector<int[]> opdatarecieve = new Vector<int[]>();
    public static int isend = 0, irecieve = 0;

    public static void debug_setsend(int[][] set) {
        if (set != null && set.length > 0) {
            for (int i = 0; i < set.length; i++) {
                int[] temp = set[i];
                opdatasend.add(temp);
            }
        }
    }

    public static void debug_setrecieve(int[][] set) {
        if (set != null && set.length > 0) {
            for (int i = 0; i < set.length; i++) {
                int[] temp = set[i];
                opdatarecieve.add(temp);
            } 
        }
    }

    public static void debug_odatassend(int address, int[] opperands) {
        int[] concat = new int[opperands.length + 1];
        concat[0] = address;
        for (int i = 1, n = 0; i < concat.length; i++, n++)
            concat[i] = opperands[n];
        opdatasend.add(concat);
        isend++;
    }

    public static int[] debug_opdatasrecieve() {
        int[] data = opdatarecieve.get(irecieve);
        irecieve++;
        return data;
    }
}
