package emulator_core;


import java.security.InvalidParameterException;
import java.util.Vector;

import emulator_core.*;


public class gb_bitfunctions {
    // since java has no unsigned data types (WTF)
    // I have do some increadbly unoptimised work
    // relating to bit manipulation so this is to convert a
    // signed int into a short to hopefully simulate overflowing and underflowing
    // since that is kind of important when implementing hardware either way
    // this could easly be optimised out by the compiler and I cannot figure out a
    // better alternative so this is staying
 
    public static String get_byte_string(int in) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 8; i++)
            buffer.insert(i,(char) (chk_bit(in, i) + '0'));
        return buffer.reverse().toString();
    }
    public static String get_word_string(int in) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 16; i++)
            buffer.insert(i,(char) (chk_bit(in, i) + '0'));
        return buffer.reverse().toString();
    }
    public static int get_value_string(String in, int length){
        int value = 0;
        for(int i = 0, opp = in.length() - 1; i < length;i++, opp--)
        {
            if(i < in.length())
            {
                if(in.charAt(i) == '1')
                    value = set_bit(value,opp);
            }
        }
        return value;
    }

    public static int set_bit(int input, int index) {
        if (index < 0 || index >= 32)
            throw new InvalidParameterException("index must be between 0 and 32");
        return (input | (0x01 << index));
    }


    public static int clr_bit(int input, int index) {
        if (index < 0 || index >= 32)
            throw new InvalidParameterException("index must be between 0 and 32");
        return (input & ~(1 << index));
    }

    public static int chk_bit(int input, int index) {
        if (index < 0 || index >= 32)
            throw new InvalidParameterException("index must be between 0 and 32");
        return ((input >> index) & 1);
    }
}
