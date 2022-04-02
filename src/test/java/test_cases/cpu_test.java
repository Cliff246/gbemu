package test_cases;
import emulator_core.*;

import java.util.Vector;

import org.javatuples.Pair;
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

    @Test public void cpu_instructiontest()
    {
        boolean[]flags = new boolean[]{true,true};
        gb_handle handle = new gb_handle(gb_handle.HANDLE_TYPE.DEBUG);
        gb_bus bus = new gb_bus("cputest", handle, flags);
        gb_cpu cpu = bus.gbcpu;
        
        Vector<Pair<Integer, Integer>>opcodelist = new Vector<Pair<Integer,Integer>>();
        for(int i = 0; i < opcodelist.size();i++)
        {
            Pair<Integer, Integer> temp = new Pair<Integer,Integer>(0, i);
            opcodelist.addElement(temp);
        }
        cpu.opcodelist = opcodelist;
        

    }

}
