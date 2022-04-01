package test_cases;
import emulator_core.*;
import junit.framework.TestCase;



import org.junit.*;


public class bitfunctions_test extends TestCase {
    
   
    @Test public void testbitchk()
    {
        final int desired = 0xabcd;
        final String test = "1011001111010101";
        char[]buffer = new char[16];
        for(int i = 0; i < 16;i++)
            buffer[i] = (char)(gb_bitfunctions.chk_bit(desired, i) + '0');
        String out = new String(buffer);
        assertEquals(String.format("%s test %s outout",test, out),test, out);
    }

    @Test public void testbitset()
    {
        final String test = "0101010101010101";
        int current = 0;
        for(int i = 0; i < 16; i++)
        {
            if(i%2 == 0)
                current = gb_bitfunctions.set_bit(current, i);
        }
        String out = new String(gb_bitfunctions.get_word_string(current));
        assertEquals(String.format("%s test %s outout",test, out),test, out);
}

    @Test public void testbitclr()
    {
        final String test = "1010101010101010";
        int current = 0xffff;
        for(int i = 0; i < 16; i++)
        {
            if(i%2 == 0)
                current = gb_bitfunctions.clr_bit(current, i);
        }
        String out = new String(gb_bitfunctions.get_word_string(current));
        assertEquals(String.format("%s test %s outout",test, out),test, out);
    }

    @Test public void testbitgetbyte()
    {
        final String test = "00001111";
        int value =  gb_bitfunctions.get_value_string(test,8);
        String current = gb_bitfunctions.get_byte_string(value);
        assertEquals(String.format("%s test %s outout",test, current),test, current);
    }
    @Test public void testbitgetword()
    {
        final String test = "0011110011000101";
        int value =  gb_bitfunctions.get_value_string(test,16);
        String current = gb_bitfunctions.get_word_string(value);
        assertEquals(String.format("%s test %s outout",test, current),test, current);
    }
}
