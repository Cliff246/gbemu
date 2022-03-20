package src.app.emulator_core;

import java.security.InvalidParameterException;
import java.util.Vector;

import src.app.emulator_core.*;

public class gb_bitfunctions {
    // since java has no unsigned data types (WTF)
    // I have do some increadbly unoptimised work
    // relating to bit manipulation so this is to convert a
    // signed int into a short to hopefully simulate overflowing and underflowing
    // since that is kind of important when implementing hardware either way
    // this could easly be optimised out by the compiler and I cannot figure out a
    // better alternative so this is staying
 
    public static String get_byte_string(int in) {
        char[] szc_temp = new char[32];
        for (int i = 31; i >= 0; i--)
            szc_temp[31 - i] = (char) (chk_bit(in, i) + '0');
        return new String(szc_temp);
    }

    public static String get_word_string(int in) {
        char[] szc_temp = new char[16];
        for (int i = 15; i >= 0; i--)
            szc_temp[15 - i] = (char) (chk_bit(in, i) + '0');
        return new String(szc_temp);
    }

    public static int set_bit(int input, int index) {
        if (index < 0 || index >= 32)
            throw new InvalidParameterException("index must be between 0 and 32");
        return (int) (input |= 1 << index);
    }

    public static int clr_bit(int input, int index) {
        if (index < 0 || index >= 32)
            throw new InvalidParameterException("index must be between 0 and 32");
        return (int) (input & ~(1 << index));
    }

    public static int chk_bit(int input, int index) {
        if (index < 0 || index >= 32)
            throw new InvalidParameterException("index must be between 0 and 32");
        return (int) ((input >> index) & 1);
    }
}
