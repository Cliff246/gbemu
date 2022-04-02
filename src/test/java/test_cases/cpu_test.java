package test_cases;
import emulator_core.*;

import java.util.Vector;

import org.junit.*;

public class cpu_test {

    
    @Test public void cpu_inittest()
    {
        boolean[]flags = new boolean[]{true,true};
        gb_handle handle = new gb_handle(gb_handle.HANDLE_TYPE.DEBUG);
        gb_bus bus = new gb_bus("cputest", handle,flags);
        gb_cpu cpu = bus.gbcpu;
    
    }

    @Test public void cpu_speedtest()
    {
        boolean[]flags = new boolean[]{true,true};
        gb_handle handle = new gb_handle(gb_handle.HANDLE_TYPE.DEBUG);
        gb_bus bus = new gb_bus("cputest", handle,flags);
        

        
    }

    @SuppressWarnings(value = "unchecked") @Test public void cpu_instructiontest()
    {
        boolean[]flags = new boolean[]{true,true};
        gb_handle handle = new gb_handle(gb_handle.HANDLE_TYPE.DEBUG);
        gb_bus bus = new gb_bus("cputest", handle, flags);
        gb_cpu cpu = bus.gbcpu;
        
        Vector<vertex<Integer>> vertexs = new Vector<>(10);
        for(int i = 0; i < vertexs.size();i++)
        {
            vertex<Integer> temp = new vertex<Integer>((int)0,i);
            vertexs.addElement(temp);
        }
        cpu.opcodelist = vertexs;


    }

}
