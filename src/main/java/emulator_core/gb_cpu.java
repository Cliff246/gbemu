package emulator_core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.TimerTask;

public class gb_cpu extends Thread
{
    
    public static enum REGISTER
    {
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
    public class registers
    {
        private int rA, rF, rB, rC, rD, rE, rH, rL;
        private int spLO, spHI, pcLO, pcHI;


        public registers(int _rA, int _rF, int _rB, int _rC, int _rD, int _rE, int _rH, int _rL, int _spLO, int _spHI, int _pcLO, int _pcHI)
        {
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
        
        public registers(registers copy)
        {    
            int[]clone =  copy.get_register_ary().clone();
            set_register_ary(clone);
        }

        public registers(gb_handle.GAMEBOY_TYPE type)
        {
            init_registers(type);
        }

        public registers()
        {
            clr_registers();
        }

        public void init_registers(gb_handle.GAMEBOY_TYPE type)
        {
            int af = 0;
            switch (type)
            {
                case GB:{
                    af = 0x01;
                    break;
                }
                case GBC:{
                    af = 0x11;
                    break;
                }
                case GBP:{
                    af = 0xff;
                    break;
                }
                case GBS:{
                    af = 0x01;
                    break;
                }
                default:{
                    clr_registers();
                    return;
                }
            }
            set_register(REGISTER.RegisterAF, af);
            set_register(REGISTER.Flag, 0xbd);
            set_register(REGISTER.RegisterBC, 0x0013);
            set_register(REGISTER.RegisterDE, 0x00d8);
            set_register(REGISTER.RegisterHL, 0x014d);
            set_register(REGISTER.StackPointer, 0xfffe);
            set_register(REGISTER.ProgramCounter, 0x0100);
        }
        
        public void clr_registers()
        {
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
            if(value < gb_bitfunctions.__u_word_min__ || value > gb_bitfunctions.__u_word_max__)
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
                rA, rF, rB,  rC,  rD,  rE,  rH,  rL,  spLO,  spHI,  pcLO,  pcHI
            };
            return out;
        }

    }

    public class instruction
    {
        public String idisassembly;
        public int ilength;
        public op_functions ifunction;
        public Method imethod;
        
        instruction(String _idisassembly, String _fnname, int _ilength, op_functions _ifunction)
        {
            idisassembly = _idisassembly;
            ilength = _ilength;
            ifunction = _ifunction;
            try
            {
                imethod = ifunction.getClass().getMethod(_fnname, registers.class, int[].class);
            }
            catch (NoSuchMethodError | NoSuchMethodException | SecurityException el)
            {
                gb_execeptions.gb_exception("no such method error %s ", el.getMessage());
            }       
        } 
        void executeinstruction(registers reg, int[]opperands)
        {
            try 
            {
                imethod.invoke(ifunction, reg, opperands);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException el) 
            {
                gb_execeptions.gb_putlog("instruction method not found %s | disassembly: %s", imethod.getName(), idisassembly);
                gb_execeptions.gb_exception("no such method error: %s ", el.getMessage());
            }          
        }
        
    }

    private gb_bus bus;
    private gb_handle handle;
    private gb_handle.GAMEBOY_TYPE type;
    private registers cpureg;
    private Thread thread;
 
    private vertex<Integer> get_16bit(int get){
        if(get < gb_bitfunctions.__u_word_min__ && get >= gb_bitfunctions.__u_word_max__)
            get &= 0xffff;
        int hi = get & 0x00ff, lo = get & 0xff;
        return new vertex<Integer>(hi, lo);  
    }

    private int set_16bit(vertex<Integer> set){
        Integer[] hilo = set.get_data_list();
        int hi = hilo[__word16_hi__], lo = hilo[__word16_lo__];
        if((hi < gb_bitfunctions.__u_byte_min__ && hi >= gb_bitfunctions.__u_byte_max__) || (lo >= gb_bitfunctions.__u_byte_min__ && lo < gb_bitfunctions.__u_byte_max__))
        {
            hi &= 0x00ff;
            lo &= 0xff;
        }
        int result = (hi << 8);
        return result + lo;
    }
    
    private gb_applog applog;
    private boolean debug;
    private op_functions functions = new op_functions();

    public gb_cpu(Thread _thread, gb_handle _handle, gb_bus _gbbus, gb_applog _applog) 
    {
        
        thread = _thread;
        handle = _handle;
        bus = _gbbus;
        type = handle.gbtype;
        if(applog == null)
            debug = false;
        else
        {
            applog = _applog;
            debug = true;
        }
        cpureg = new registers(type);
    }


   
    public final int __word16_hi__ = 0;
    public final int __word16_lo__ = 1;  
    public final int __ppu_address_max__ = 0xfff, __ppu_address_min__ = 0;


    public void snoopbus(int[]data)
    {
        opperands = data;
    }

    public boolean update = true;
    public TimerTask scheduler;
    public long duration;
    int[]opperands = null;
    
    public void start()
    {   
        if(debug == true)
            applog = new gb_applog("cpu debug");
        run();
    }

    public void run()
    {
        duration = (handle.gbtype == gb_handle.GAMEBOY_TYPE.GBC)? gb_handle.__gbc_hzclock__ / 60: gb_handle.__gb_hzclock__ / 60;
        while(update == true)
        {
            int opcode = 0, prefix = 0;
            instruction instruction = operations[prefix][opcode]; 
            instruction.executeinstruction(cpureg, opperands);   
        }
    }


    instruction[][] operations = {
        {
            new instruction("NOP", "__NOP", 0, functions),
            new instruction("STOP", "__STOP", 1, functions),
        },
        {
        
        }
    };





    public class op_functions
    {

        private void onecycle()
        {


            try {
                sleep(duration);
            } catch (InterruptedException e) {
                gb_execeptions.gb_exception(e.getMessage());
            }
        } 

 
      
        public void __NOP(registers reg, int[]opperands)
        {

            onecycle();
            return;
        }
        
        public void __LD_BC_A(registers reg, int[]opperands)
        {
            return;
        }

        public void __INC_BC(registers reg, int[]opperands)
        {
            return;
        }

        public void __LD_BC_d16(registers reg, int[]opperands){}
        public void __DEC_B(registers reg, int[]opperands){}
        public void __INC_B(registers reg, int[]opperands){}
        public void __RLCA(registers reg, int[]opperands){}
        public void __LD_B_d8(registers reg, int[]opperands){}
        public void __ADD_HL_BC(registers reg, int[]opperands){}
        public void __LD__a16__SP(registers reg, int[]opperands){}
        public void __DEC_BC(registers reg, int[]opperands){}
        public void __LD_A__BC__(registers reg, int[]opperands){}
        public void __DEC_C(registers reg, int[]opperands){}
        public void __INC_C(registers reg, int[]opperands){}
        public void __LD_C_d8(registers reg, int[]opperands){}
        public void __LD_DE_d16(registers reg, int[]opperands){}
        
        public void __STOP(registers reg, int[]opperands)
        {
            update = false;

            onecycle();
            return;
        }

        public void __INC_DE(registers reg, int[]opperands){}
        public void __LD__DE__A(registers reg, int[]opperands){}
        public void __DEC_D(registers reg, int[]opperands){}
        public void __INC_D(registers reg, int[]opperands){}
        public void __LD_D_d8(registers reg, int[]opperands){}
        public void __ADD_HL_DE(registers reg, int[]opperands){}
        public void __JR_r8(registers reg, int[]opperands){}
        public void __DEC_DE(registers reg, int[]opperands){}
        public void __LD_A__DE__(registers reg, int[]opperands){}
        public void __DEC_E(registers reg, int[]opperands){}
        public void __INC_E(registers reg, int[]opperands){}
        public void __LD_E_d8(registers reg, int[]opperands){}
        public void __LD_HL_d16(registers reg, int[]opperands){}
        public void __JR_NZ_r8(registers reg, int[]opperands){}
        public void __INC_HL(registers reg, int[]opperands){}
        public void __LD__HLplus__A(registers reg, int[]opperands){}
        public void __DEC_H(registers reg, int[]opperands){}
        public void __INC_H(registers reg, int[]opperands){}
        public void __DAA(registers reg, int[]opperands){}
        public void __LD_H_d8(registers reg, int[]opperands){}
        public void __ADD_HL_HL(registers reg, int[]opperands){}
        public void __JR_Z_r8(registers reg, int[]opperands){}
        public void __DEC_HL(registers reg, int[]opperands){}
        public void __LD_A__HLplus__(registers reg, int[]opperands){}
        public void __DEC_L(registers reg, int[]opperands){}
        public void __INC_L(registers reg, int[]opperands){}
        public void __CP(registers reg, int[]opperands){}
        public void __LD_L_d8(registers reg, int[]opperands){}
        public void __LD_SP_d16(registers reg, int[]opperands){}
        public void __JR_NC_r8(registers reg, int[]opperands){}
        public void __INC_SP(registers reg, int[]opperands){}
        public void __LD__HLminus__A(registers reg, int[]opperands){}
        public void __DEC__HL__(registers reg, int[]opperands){}
        public void __INC__HL__(registers reg, int[]opperands){}
        public void __SCF(registers reg, int[]opperands){}
        public void __LD__HL__d8(registers reg, int[]opperands){}
        public void __ADD_HL_SP(registers reg, int[]opperands){}
        public void __JR_C_r8(registers reg, int[]opperands){}
        public void __DEC_SP(registers reg, int[]opperands){}
        public void __LD_A__HLminus__(registers reg, int[]opperands){}
        public void __DEC_A(registers reg, int[]opperands){}
        public void __INC_A(registers reg, int[]opperands){}
        public void __LD_A_d8(registers reg, int[]opperands){}
        public void __LD_B_C(registers reg, int[]opperands){}
        public void __LD_B_B(registers reg, int[]opperands){}
        public void __LD_B_E(registers reg, int[]opperands){}
        public void __LD_B_D(registers reg, int[]opperands){}
        public void __LD_B_L(registers reg, int[]opperands){}
        public void __LD_B_H(registers reg, int[]opperands){}
        public void __LD_B_A(registers reg, int[]opperands){}
        public void __LD_B__HL__(registers reg, int[]opperands){}
        public void __LD_C_C(registers reg, int[]opperands){}
        public void __LD_C_B(registers reg, int[]opperands){}
        public void __LD_C_E(registers reg, int[]opperands){}
        public void __LD_C_D(registers reg, int[]opperands){}
        public void __LD_C_L(registers reg, int[]opperands){}
        public void __LD_C_H(registers reg, int[]opperands){}
        public void __LD_C_A(registers reg, int[]opperands){}
        public void __LD_C__HL_(registers reg, int[]opperands){}
        public void __LD_D_C(registers reg, int[]opperands){}
        public void __LD_D_B(registers reg, int[]opperands){}
        public void __LD_D_E(registers reg, int[]opperands){}
        public void __LD_D_D(registers reg, int[]opperands){}
        public void __LD_D_L(registers reg, int[]opperands){}
        public void __LD_D_H(registers reg, int[]opperands){}
        public void __LD_D_A(registers reg, int[]opperands){}
        public void __LD_D__HL__(registers reg, int[]opperands){}
        public void __LD_E_C(registers reg, int[]opperands){}
        public void __LD_E_B(registers reg, int[]opperands){}
        public void __LD_E_E(registers reg, int[]opperands){}
        public void __LD_E_D(registers reg, int[]opperands){}
        public void __LD_E_L(registers reg, int[]opperands){}
        public void __LD_E_H(registers reg, int[]opperands){}
        public void __LD_E_A(registers reg, int[]opperands){}
        public void __LD_E__HL_(registers reg, int[]opperands){}
        public void __LD_H_C(registers reg, int[]opperands){}
        public void __LD_H_B(registers reg, int[]opperands){}
        public void __LD_H_E(registers reg, int[]opperands){}
        public void __LD_H_D(registers reg, int[]opperands){}
        public void __LD_H_L(registers reg, int[]opperands){}
        public void __LD_H_H(registers reg, int[]opperands){}
        public void __LD_H_A(registers reg, int[]opperands){}
        public void __LD_H__HL__(registers reg, int[]opperands){}
        public void __LD_L_C(registers reg, int[]opperands){}
        public void __LD_L_B(registers reg, int[]opperands){}
        public void __LD_L_E(registers reg, int[]opperands){}
        public void __LD_L_D(registers reg, int[]opperands){}
        public void __LD_L_L(registers reg, int[]opperands){}
        public void __LD_L_H(registers reg, int[]opperands){}
        public void __LD_L_A(registers reg, int[]opperands){}
        public void __LD__HL__B(registers reg, int[]opperands){}
        public void __LD_L__HL__(registers reg, int[]opperands){}
        public void __LD__HL__D(registers reg, int[]opperands){}
        public void __LD__HL__C(registers reg, int[]opperands){}
        public void __LD__HL__H(registers reg, int[]opperands){}
        public void __LD__HL__E(registers reg, int[]opperands){}
        public void __HALT(registers reg, int[]opperands){}
        public void __LD__HL__L(registers reg, int[]opperands){}
        public void __LD_A_B(registers reg, int[]opperands){}
        public void __LD__HL__A(registers reg, int[]opperands){}
        public void __LD_A_D(registers reg, int[]opperands){}
        public void __LD_A_C(registers reg, int[]opperands){}
        public void __LD_A_H(registers reg, int[]opperands){}
        public void __LD_A_E(registers reg, int[]opperands){}
        public void __LD_A__HL_(registers reg, int[]opperands){}
        public void __LD_A_L(registers reg, int[]opperands){}
        public void __ADD_A_B(registers reg, int[]opperands){}
        public void __LD_A_A(registers reg, int[]opperands){}
        public void __ADD_A_D(registers reg, int[]opperands){}
        public void __ADD_A_C(registers reg, int[]opperands){}
        public void __ADD_A_H(registers reg, int[]opperands){}
        public void __ADD_A_E(registers reg, int[]opperands){}
        public void __ADD_A__HL_(registers reg, int[]opperands){}
        public void __ADD_A_L(registers reg, int[]opperands){}
        public void __ADC_A_B(registers reg, int[]opperands){}
        public void __ADD_A_A(registers reg, int[]opperands){}
        public void __ADC_A_D(registers reg, int[]opperands){}
        public void __ADC_A_C(registers reg, int[]opperands){}
        public void __ADC_A_H(registers reg, int[]opperands){}
        public void __ADC_A_E(registers reg, int[]opperands){}
        public void __ADC_A__HL_(registers reg, int[]opperands){}
        public void __ADC_A_L(registers reg, int[]opperands){}
        public void __SUB_B(registers reg, int[]opperands){}
        public void __ADC_A_(registers reg, int[]opperands){}
        public void __SUB_D(registers reg, int[]opperands){}
        public void __SUB_C(registers reg, int[]opperands){}
        public void __SUB_H(registers reg, int[]opperands){}
        public void __SUB_E(registers reg, int[]opperands){}
        public void __SUB__HL_(registers reg, int[]opperands){}
        public void __SUB_L(registers reg, int[]opperands){}
        public void __SBC_A_B(registers reg, int[]opperands){}
        public void __SUB_A(registers reg, int[]opperands){}
        public void __SBC_A_D(registers reg, int[]opperands){}
        public void __SBC_A_C(registers reg, int[]opperands){}
        public void __SBC_A_H(registers reg, int[]opperands){}
        public void __SBC_A_E(registers reg, int[]opperands){}
        public void __SBC_A__HL_(registers reg, int[]opperands){}
        public void __SBC_A_L(registers reg, int[]opperands){}
        public void __AND_B(registers reg, int[]opperands){}
        public void __SBC_A_A(registers reg, int[]opperands){}
        public void __AND_D(registers reg, int[]opperands){}
        public void __AND_C(registers reg, int[]opperands){}
        public void __AND_H(registers reg, int[]opperands){}
        public void __AND_E(registers reg, int[]opperands){}
        public void __AND__HL_(registers reg, int[]opperands){}
        public void __AND_L(registers reg, int[]opperands){}
        public void __XOR_B(registers reg, int[]opperands){}
        public void __AND_A(registers reg, int[]opperands){}
        public void __XOR_D(registers reg, int[]opperands){}
        public void __XOR_C(registers reg, int[]opperands){}
        public void __XOR_H(registers reg, int[]opperands){}
        public void __XOR_E(registers reg, int[]opperands){}
        public void __XOR__HL_(registers reg, int[]opperands){}
        public void __XOR_L(registers reg, int[]opperands){}
        public void __OR_B(registers reg, int[]opperands){}
        public void __XOR_A(registers reg, int[]opperands){}
        public void __OR_D(registers reg, int[]opperands){}
        public void __OR_C(registers reg, int[]opperands){}
        public void __OR_H(registers reg, int[]opperands){}
        public void __OR_E(registers reg, int[]opperands){}
        public void __OR__HL__(registers reg, int[]opperands){}
        public void __OR_L(registers reg, int[]opperands){}
        public void __CP_B(registers reg, int[]opperands){}
        public void __OR_A(registers reg, int[]opperands){}
        public void __CP_D(registers reg, int[]opperands){}
        public void __CP_C(registers reg, int[]opperands){}
        public void __CP_H(registers reg, int[]opperands){}
        public void __CP_E(registers reg, int[]opperands){}
        public void __CP__HL_(registers reg, int[]opperands){}
        public void __CP_L(registers reg, int[]opperands){}
        public void __RET_NZ(registers reg, int[]opperands){}
        public void __CP_A(registers reg, int[]opperands){}
        public void __JP_NZ_a16(registers reg, int[]opperands){}
        public void __POP_BC(registers reg, int[]opperands){}
        public void __CALL_NZ_a16(registers reg, int[]opperands){}
        public void __JP_a16(registers reg, int[]opperands){}
        public void __ADD_A_d8(registers reg, int[]opperands){}
        public void __PUSH_BC(registers reg, int[]opperands){}
        public void __RET_Z(registers reg, int[]opperands){}
        public void __RST(registers reg, int[]opperands){}
        public void __JP_Z_a16(registers reg, int[]opperands){}
        public void __RET(registers reg, int[]opperands){}
        public void __CALL_Z_a16(registers reg, int[]opperands){}
        public void __PREFIX_CB(registers reg, int[]opperands){}
        public void __CALL_a16(registers reg, int[]opperands){}
        public void __ADC_A_d8(registers reg, int[]opperands){}
        public void __RET_NC(registers reg, int[]opperands){}
        public void __RST_08(registers reg, int[]opperands){}
        public void __JP_NC_a16(registers reg, int[]opperands){}
        public void __POP_DE(registers reg, int[]opperands){}
        public void __PUSH_DE(registers reg, int[]opperands){}
        public void __CALL_NC_a16(registers reg, int[]opperands){}
        public void __RST_10H(registers reg, int[]opperands){}
        public void __SUB_d8(registers reg, int[]opperands){}
        public void __RETI(registers reg, int[]opperands){}
        public void __RET_C(registers reg, int[]opperands){}
        public void __CALL_C_a16(registers reg, int[]opperands){}
        public void __JP_C_a16(registers reg, int[]opperands){}
        public void __RST_18(registers reg, int[]opperands){}
        public void __SBC_A_d8(registers reg, int[]opperands){}
        public void __POP_HL(registers reg, int[]opperands){}
        public void __LDH__a8__A(registers reg, int[]opperands){}
        public void __PUSH_HL(registers reg, int[]opperands){}
        public void __LD__C__A(registers reg, int[]opperands){}
        public void __RST_20H(registers reg, int[]opperands){}
        public void __AND_d8(registers reg, int[]opperands){}
        public void __JP_HL_(registers reg, int[]opperands){}
        public void __ADD_SP_r8(registers reg, int[]opperands){}
        public void __XOR_d8(registers reg, int[]opperands){}
        public void __LD_a16_A(registers reg, int[]opperands){}
        public void __LDH_A_a8(registers reg, int[]opperands){}
        public void __RST_28(registers reg, int[]opperands){}
        public void __LD_A__C__(registers reg, int[]opperands){}
        public void __POP_AF(registers reg, int[]opperands){}
        public void __PUSH_AF(registers reg, int[]opperands){}
        public void __DI(registers reg, int[]opperands){}
        public void __RST_30H(registers reg, int[]opperands){}
        public void __OR_d8(registers reg, int[]opperands){}
        public void __LD_SP_HL(registers reg, int[]opperands){}
        public void __LD_HL_SP_plus_r8(registers reg, int[]opperands){}
        public void __EI(registers reg, int[]opperands){}
        public void __LD_A_a16(registers reg, int[]opperands){}
        public void __RST_38H(registers reg, int[]opperands){}
        public void __CP_d8(registers reg, int[]opperands){}
    }



   
}
