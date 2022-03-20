package emulator_core;


import java.security.InvalidParameterException;
import java.util.Timer;
import java.util.Vector;

import emulator_core.*;

public class gb_cpu extends Thread {
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

    private static enum REGISTER{
        Accumulator,  Flag,
        RegisterB, RegisterC,
        RegisterD, RegisterE,
        RegisterH, RegisterL,
        RegisterAF, RegisterBC,
        RegisterDE, RegisterHL,
        StackPointer, ProgramCounter,
        NoRegister
    }

    private final int sbyte_max = 128, sbyte_min = -127;
    private final int ubyte_max = 255, ubyte_min = 0;
    private final int uword_max = 65535, uword_min = 0;
    private final int word16_hi = 0;
    private final int word16_lo = 1;  
    private int cycles = 0;
    private int length = 0;
    private int rA, rF, rB, rC, rD, rE, rH, rL;
    private int spLO, spHI, pcLO, pcHI;
    private boolean debug = true;
    private gb_applog applog;

    Vector<vertex<Integer>> instruction_debug = new Vector<vertex<Integer>>();


    public void setup(){
        if(debug == true)
            applog = new gb_applog("cpu debug");
    }


    private int chk_byte(int bite){
        return bite & 0xff;
    }

    private int chk_word(int word){
        return word & 0xffff;
    }

  
    private vertex<Integer> get_16bit(int get){
        if(get < uword_min && get >= uword_max)
            get &= 0xffff;
        int hi = get & 0x00ff, lo = get & 0xff;
        return new vertex<Integer>(hi, lo);  
    }

    private int set_16bit(vertex<Integer> set){
        Integer[] hilo = set.get_data_list();
        int hi = hilo[word16_hi], lo = hilo[word16_lo];
        if((hi < ubyte_min && hi >= ubyte_max) || (lo >= ubyte_min && lo < ubyte_max))
        {
            hi &= 0x00ff;
            lo &= 0xff;
        }
        int result = (hi << 8);
        return result + lo;
    }


    private int set_register(REGISTER reg, int value){
        if(value < uword_min)
        {
            gb_errorstate.exception(thread, "virtual 8 bit value is does not fit within range", Integer.toString(value), reg.toString());
            return -1;
        }
        else if(value > uword_max)
        {
            return -1;
        }
        else
        {   
            int bitsize = 0;
            switch(reg)
            {
                case Accumulator:{
                    bitsize = 8;
                    rA = value;
                    break;
                }
                case Flag:{
                    bitsize = 8;
                    rF = value;
                    break;
                }
                case RegisterB:{
                    bitsize = 8;
                    rB = value;
                    break;
                }
                case RegisterC:{
                    bitsize = 8;
                    rC = value;
                    break;
                }
                case RegisterD:{
                    bitsize = 8;
                    rD = value;
                    break;
                }
                case RegisterE:{
                    bitsize = 8;
                    rE = value;
                    break;
                }
                case RegisterH:{
                    bitsize = 8;
                    rH = value;
                    break;
                }
                case RegisterL:{
                    bitsize = 8;
                    rL = value;
                    break;
                }
                case StackPointer:{
                    bitsize = 16;
                    Integer[]hilo = get_16bit(value).get_data_list();
                    spHI = hilo[word16_hi];
                    spLO = hilo[word16_lo];
                    break;
                }
                case ProgramCounter:{
                    bitsize = 16;
                    Integer[]hilo = get_16bit(value).get_data_list();
                    pcHI = hilo[word16_hi];
                    pcLO = hilo[word16_lo];
                }
                case RegisterAF:{
                    bitsize = 16;
                    Integer[]hilo = get_16bit(value).get_data_list();
                    rA = hilo[word16_hi];
                    rF = hilo[word16_lo];
                    break;
                }
                case RegisterBC:{
                    bitsize = 16;
                    Integer[]hilo = get_16bit(value).get_data_list();
                    rB = hilo[word16_hi];
                    rC = hilo[word16_lo];
                    break;
                }
                case RegisterDE:{
                    bitsize = 16;
                    Integer[]hilo = get_16bit(value).get_data_list();
                    rD = hilo[word16_hi];
                    rE = hilo[word16_lo];
                    break;
                }
                case RegisterHL:{
                    bitsize = 16;
                    Integer[]hilo = get_16bit(value).get_data_list();
                    rH = hilo[word16_hi];
                    rL = hilo[word16_lo];
                    break;
                }
                default:{
                    bitsize = -1;
                    gb_errorstate.warning(thread, "8 bit load register not found", reg.toString(), Integer.toString(value) );
                    break;
                }
            }
            return bitsize;
        }
    }
    private int[][]opcode_immedate = {
        {},
        {},
    };
    private int[][]opcode_displace = {
        {},
        {}
    };

    private int[][]opcode_length = {
        {},
        {}
    };
    private final int __instruction__ = 0;
    private final int __precode__ = 1;
    private final int __postcode__ = 2;
    private final int __displacment__ = 3;
    private final int __immedate__ = 4;
    private final int __complete__ = 5;
    private final int __instruction_precode__ = 0;
    private final int __instruction_postcode__ = 1;
    private final int __instruction_displace__ = 2;
    private final int __instruction_immedate__ = 3;

    private int stage = 0;
    private int ndisplace = 0;
    private int nimmedate = 0;
    private int preop = 0, postop = 0;

    private vertex<Integer> instruction = new vertex<Integer>(0,0,0,0);
    public void gbcpu_cycle()
    {
        final int __nprefix__ = 0xcb;
        int data = bus.pull_off_bus();

        if(stage == __instruction__){
            preop = 0;
            postop = 0;
            cycles = 0;
            if(data == __nprefix__){
                length = 2;
                stage = __precode__;
            }
            else{
                length = 1;
                stage = __postcode__;
            }
        }
        else if(stage == __precode__){
            instruction.set_at(data, __instruction_precode__);
            stage = __postcode__;
        }
        else if(stage == __postcode__){
            int postcode = data,
                precode = instruction.get_at(__instruction_precode__);
            instruction.set_at(postcode, __instruction_postcode__);
            length = opcode_length[precode][postcode] - length;
            
            ndisplace = opcode_immedate[precode][postcode];
            nimmedate = opcode_displace[precode][postcode];

            instruction.set_at(ndisplace, __instruction_displace__);
            instruction.set_at(nimmedate, __instruction_immedate__);
            if(ndisplace > 0){
                stage = __displacment__;
            }
            else if(nimmedate > 0){
                stage = __immedate__;
            }
            else if(length <= 0){
                stage = __complete__;
            }
            cycles++;
        }
        else if(stage == __displacment__){
            length--;
            int current = instruction.get_at(__displacment__);
            current = (current << 8) + current;
            instruction.set_at(current, __displacment__);
            if(ndisplace-- == 0 && nimmedate > 0)
                stage = __immedate__;
            else if(ndisplace == 0)
                stage = __complete__;
            cycles++;
        }
        else if(stage == __immedate__){
            length--;
            int current = instruction.get_at(__immedate__);
            current = (current << 8) + current;
            instruction.set_at(current, __immedate__);
            if(nimmedate-- == 0)
                stage = __complete__;
            cycles++;
            
        }
        else if(stage == __complete__ || length == 0){
        
            if(debug == true){
                vertex<Integer> copy = instruction;
                instruction_debug.add(copy);
            }

            instruction.clear();
            nimmedate = 0;
            ndisplace = 0;
            length = 1;
            stage = __instruction__;
            return;
        }
        
        int postcode = instruction.get_at(__instruction_postcode__);
        int precode = instruction.get_at(__instruction_precode__);
        int outbus = 0;
        if(stage >= 2){
            preop = precode;
            postop = postcode;
            if(precode == 0)
            {
                outbus = hydra_post_navigator(data);
            }
            else
            {
                outbus = hydra_prepost_navigator(data);
            }

            if(outbus != funnynumber)
            {
                bus.push_onto_bus(outbus);
            }
        
        }   
        else{

        }


        
        
    }

    private final int funnynumber = -42069;
    
    //watch displaced gammers
    private int hydra_post_navigator(int data){
        int ret = funnynumber;
        if(stage == __postcode__)
            cycles = opcode_length[preop][postop] - 1;        
        switch(postop){
            
            case 0x00:{
                //NOOP    
                break;
            }
            case 0x01:{
                //
                break;

            } 
            case 0x02:{
                //
                break;  
            }
            case 0x03:{
                //
                break;
            }
            case 0x04:{
                //
                break;
            }
            case 0x05:{
                //
                break;
            }
            case 0x06:{
                //
                break;
            }
            case 0x07:{
                //
                break;
            }
            case 0x08:{
                //
                break;
            }
            case 0x09:{
                //
                break;
            }
            case 0x0a:{
                //
                break;
            }
            case 0x0b:{
                //
                break;
            }
            case 0x0c:{
                //
                break;
            }
            case 0x0e:{
                //
                break;
            }
            case 0x0f:{
                //
                break;
            }
            case 0x10:{
                //
                break;
            }
            case 0x11:{
                //
                break;
            }
            case 0x12:{
                break;
            }
            default:{

                break;
            }
        }

        if(cycles == 0){
            if(ndisplace > 0)
                gb_errorstate.warning(thread, "no more cycles lest bus displacement has more elements", Integer.toString(ndisplace));
            else if(nimmedate > 0)
                gb_errorstate.warning(thread, "no more cycles lest bus displacement has more elements", Integer.toString(ndisplace));
            else if(debug == true)
                applog.push_log(Integer.toString(cycles) + "-> ending cycles");
        }
        return ret;
    }

    //watch displaced gammers
    private int hydra_prepost_navigator(int data){
        int ret = funnynumber;
        
        
        switch(postop)
        {

        }
        return ret;
    }

    private void gbcpu_op_misc(int input)
    {

    }

    private void load_8bit(int input, int opcode){
        

        if(stage == __postcode__){
            
        }
        if(stage == __displacment__){

        }
        if(stage == __immedate__){

        }
    }
}
