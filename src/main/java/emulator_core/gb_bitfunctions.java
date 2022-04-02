package emulator_core;

import java.security.InvalidParameterException;

public class gb_bitfunctions {
    // since java has no unsigned data types (WTF)
    // I have do some increadbly unoptimised work
    // relating to bit manipulation so this is to convert a
    // signed int into a short to hopefully simulate overflowing and underflowing
    // since that is kind of important when implementing hardware either way
    // this could easly be optimised out by the compiler and I cannot figure out a
    // better alternative so this is staying

    public static final int __s_byte_max__ = 127, __s_byte_min__ = -128;
    public static final int __u_byte_max__ = 255, __u_byte_min__ = 0;
    public static final int __u_word_max__ = 65535, __u_word_min__ = 0;
    public static final int __s_word_max__ = 32767, __s_word_min__ = -32768;

    public static String get_byte_string(int in) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 8; i++)
            buffer.insert(i, (char) (chk_bit(in, i) + '0'));
        return buffer.reverse().toString();
    }

    public static String get_word_string(int in) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 16; i++)
            buffer.insert(i, (char) (chk_bit(in, i) + '0'));
        return buffer.reverse().toString();
    }

    public static int get_value_string(String in, int length) {
        int value = 0;
        for (int i = 0, opp = in.length() - 1; i < length; i++, opp--) {
            if (i < in.length()) {
                if (in.charAt(i) == '1')
                    value = set_bit(value, opp);
            }
        }
        return value;
    }

    public static int cat_word(int lo, int hi) {

        int output = hi;
        output <<= 8;
        return output + lo;
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

    public static int chk_byte(int bite) {
        return bite & 0xff;
    }

    public static int chk_word(int word) {
        return word & 0xffff;
    }
}
