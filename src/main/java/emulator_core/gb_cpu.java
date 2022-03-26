package emulator_core;


import java.util.Stack;
import java.util.Vector;


public class gb_cpu extends Thread {
    
    public static enum REGISTER{
        Accumulator,  Flag,
        RegisterB, RegisterC,
        RegisterD, RegisterE,
        RegisterH, RegisterL,
        RegisterAF, RegisterBC,
        RegisterDE, RegisterHL,
        StackPointer, ProgramCounter,
        StackPointerHI, StackPointerLO,
        ProgramCounterHI, ProgramCounterLO,
        NoRegister
    }
    public class registers{
        private int rA, rF, rB, rC, rD, rE, rH, rL;
        private int spLO, spHI, pcLO, pcHI;


        public registers(
            int _rA, int _rF,
            int _rB, int _rC,
            int _rD, int _rE,
            int _rH, int _rL,
            int _spLO, int _spHI,
            int _pcLO, int _pcHI){
                rA = _rA;
                rF = _rF;
                rB = _rB;
                rC = _rC;
                rD = _rD;
                rE = _rE;
                rH = _rH;
                rL = _rL;
                spLO = _spLO;
                spHI = _spHI;
                pcLO = _pcLO;
                pcHI = _pcHI;
        }
        
        public registers(registers copy){
            
            
            

        }

        public void clr_registers(){
            rA = 0;
            rF = 0;
            rB = 0;
            rC = 0;
            rD = 0;
            rE = 0;
            rH = 0;
            rL = 0;
            spLO = 0;
            spHI = 0;
            pcLO = 0;
            pcHI = 0;
        }
        public int get_register(REGISTER op)
        {
            switch(op)
            {
                case Accumulator:
                    return rA;
                case Flag:
                    return rF;
                case RegisterB:
                    return rB;
                case RegisterC:
                    return rC;
                case RegisterD:
                    return rD;
                case RegisterE:
                    return rE;
                case RegisterH:
                    return rH;
                case RegisterL:
                    return rL;
                case StackPointer:
                    return gb_bitfunctions.cat_word(spLO, spHI);
                case ProgramCounter:
                    return gb_bitfunctions.cat_word(pcLO, pcHI);
                case RegisterAF:
                    return gb_bitfunctions.cat_word(rF, rA);
                case RegisterBC:
                    return gb_bitfunctions.cat_word(rC, rB);
                case RegisterDE:
                    return gb_bitfunctions.cat_word(rE, rD);
                case RegisterHL:
                    return gb_bitfunctions.cat_word(rL, rH);
                case StackPointerHI:
                    return spHI;
                case StackPointerLO:
                    return spLO;
                case ProgramCounterHI:
                    return pcHI;
                case ProgramCounterLO:
                    return pcLO;
                default:
                    return Integer.MIN_VALUE;
            }
        }

        public void set_register(REGISTER op, int value){
            if(value < __uword_min__ || value > __uword_max__)
                return;
            
               
            switch(op)
            {
                case Accumulator:{
                    rA = value;
                    break;
                }
                case Flag:{
                    rF = value;
                    break;
                }
                case RegisterB:{
                    rB = value;
                    break;
                }
                case RegisterC:{
                    rC = value;
                    break;
                }
                case RegisterD:{
                    rD = value;
                    break;
                }
                case RegisterE:{
                    rE = value;
                    break;
                }
                case RegisterH:{
                    rH = value;
                    break;
                }
                case RegisterL:{
                    rL = value;
                    break;
                }
                case StackPointer:{
                    Integer[]hilo = get_16bit(value).get_data_list();
                    spHI = hilo[__word16_hi__];
                    spLO = hilo[__word16_lo__];
                    break;
                }
                case ProgramCounter:{
                    Integer[]hilo = get_16bit(value).get_data_list();
                    pcHI = hilo[__word16_hi__];
                    pcLO = hilo[__word16_lo__];
                    break;
                }
                case RegisterAF:{
                    Integer[]hilo = get_16bit(value).get_data_list();
                    rA = hilo[__word16_hi__];
                    rF = hilo[__word16_lo__];
                    break;
                }
                case RegisterBC:{
                    Integer[]hilo = get_16bit(value).get_data_list();
                    rB = hilo[__word16_hi__];
                    rC = hilo[__word16_lo__];
                    break;
                }
                case RegisterDE:{
                    Integer[]hilo = get_16bit(value).get_data_list();
                    rD = hilo[__word16_hi__];
                    rE = hilo[__word16_lo__];
                    break;
                }
                case RegisterHL:{
                    Integer[]hilo = get_16bit(value).get_data_list();
                    rH = hilo[__word16_hi__];
                    rL = hilo[__word16_lo__];
                    break;
                }
                default:
                    break;
            
                
            }
        }

        public void set_register_ary(int[]values)
        {
            if(values.length == 12)
            {
                rA = values[0];
                rF = values[1];
                rB = values[2];
                rC = values[3];
                rD = values[4];
                rE = values[5];
                rH = values[6];
                rL = values[7];
                spLO = values[8];
                spHI = values[9];
                pcLO = values[10];
                pcHI = values[11];            
            }
            else
                clr_registers();
        }

        public int[] get_register_ary()
        {
            int[]out = {
                rA, rF, rB,  rC,  rD,  rE,  rH,  rL,  spLO,  spHI,  pcLO,  pcHI\
            };
            return out;
        }

    }

    public interface execute{
        public void opexe();
    }
    public class instruction{
        String idisassembly;
        int ilength;
        execute iexe;
        
        instruction(String _idisassembly, int _ilength, execute _iexe)
        {
            idisassembly = _idisassembly;
            ilength = _ilength;
            iexe = _iexe;
        }
        
        void executeinstruction(){
            iexe.opexe();
        }
        
    }

    private gb_bus bus;
    private gb_handle handle;
    private gb_handle.GAMEBOY_TYPE type;
    private Thread thread;
 
   

    public gb_cpu(Thread _thread, gb_handle _handle, gb_bus _gbbus) {
        
        thread = _thread;
        handle = _handle;
        bus = _gbbus;
        type = handle.gbtype;

    }

   


    private final int __sbyte_max__ = 128, __sbyte_min__ = -127;
    private final int __ubyte_max__ = 255, __ubyte_min__ = 0;
    private final int __uword_max__ = 65535, __uword_min__ = 0;
    private final int __word16_hi__ = 0;
    private final int __word16_lo__ = 1;  
    private final int __ppu_address_max__ = 0xfff, __ppu_address_min__ = 0;


    private boolean debug = true;
    private gb_applog applog;

    Vector<vertex<Integer>> instruction_debug = new Vector<vertex<Integer>>();




    public void setup(){
        if(debug == true)
            applog = new gb_applog("cpu debug");
    }




  
    private vertex<Integer> get_16bit(int get){
        if(get < __uword_min__ && get >= __uword_max__)
            get &= 0xffff;
        int hi = get & 0x00ff, lo = get & 0xff;
        return new vertex<Integer>(hi, lo);  
    }

    private int set_16bit(vertex<Integer> set){
        Integer[] hilo = set.get_data_list();
        int hi = hilo[__word16_hi__], lo = hilo[__word16_lo__];
        if((hi < __ubyte_min__ && hi >= __ubyte_max__) || (lo >= __ubyte_min__ && lo < __ubyte_max__))
        {
            hi &= 0x00ff;
            lo &= 0xff;
        }
        int result = (hi << 8);
        return result + lo;
    }


   

    





  
    public void gbcpu_cycle()
    {
        

    }


    public void snoopbus()
    {
        
    }

    
  
}
