package test_cases;
import emulator_core.*;
import org.junit.*;

public class cpu_test {

    
    @Test public void cpu_inittest()
    {
        gb_handle handle = new gb_handle(gb_handle.HANDLE_TYPE.DEBUG);
        gb_bus bus = new gb_bus("cputest", handle);
        gb_cpu cpu = bus.gbcpu;
    
    }

    @Test public void cpu_speedtest()
    {

        gb_handle handle = new gb_handle(gb_handle.HANDLE_TYPE.DEBUG);
        gb_bus bus = new gb_bus("cputest", handle);
        

        
    }

    @Test public void cpu_instructiontest_NOP()
    {

    }

}
