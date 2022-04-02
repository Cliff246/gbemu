package emulator_core;

import java.util.Vector;

import org.javatuples.Pair;

public class gb_cpu extends gb_components{

    public static enum REGISTER {
        Accumulator, Flag,
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

    public class registers {
        private int rA, rF, rB, rC, rD, rE, rH, rL;
        private int spLO, spHI, pcLO, pcHI;

        public registers(int _rA, int _rF, int _rB, int _rC, int _rD, int _rE, int _rH, int _rL, int _spLO, int _spHI,
                int _pcLO, int _pcHI) {
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

        public registers(registers copy) {
            int[] clone = copy.get_register_ary().clone();
            set_register_ary(clone);
        }

        public registers(gb_handle.GAMEBOY_TYPE type) {
            init_registers(type);
        }

        public registers() {
            clr_registers();
        }

        public void init_registers(gb_handle.GAMEBOY_TYPE type) {
            int af = 0;
            switch (type) {
                case GB: {
                    af = 0x01;
                    break;
                }
                case GBC: {
                    af = 0x11;
                    break;
                }
                case GBP: {
                    af = 0xff;
                    break;
                }
                case GBS: {
                    af = 0x01;
                    break;
                }
                default: {
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

        public void clr_registers() {
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

        public int get_register(REGISTER op) {
            switch (op) {
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

        public void set_register(REGISTER op, int value) {
            if (value < gb_bitfunctions.__u_word_min__ || value > gb_bitfunctions.__u_word_max__)
                return;

            switch (op) {
                case Accumulator: {
                    rA = value;
                    break;
                }
                case Flag: {
                    rF = value;
                    break;
                }
                case RegisterB: {
                    rB = value;
                    break;
                }
                case RegisterC: {
                    rC = value;
                    break;
                }
                case RegisterD: {
                    rD = value;
                    break;
                }
                case RegisterE: {
                    rE = value;
                    break;
                }
                case RegisterH: {
                    rH = value;
                    break;
                }
                case RegisterL: {
                    rL = value;
                    break;
                }
                case StackPointer: {
                    Pair<Integer, Integer> hilo = get_16bit(value);
                    spHI = hilo.getValue1();
                    spLO = hilo.getValue0();
                    break;
                }
                case ProgramCounter: {
                    Pair<Integer, Integer> hilo = get_16bit(value);
                    pcHI = hilo.getValue1();
                    pcLO =  hilo.getValue0();
                    break;
                }
                case RegisterAF: {
                    Pair<Integer, Integer> hilo = get_16bit(value);
                    rA = hilo.getValue1();
                    rF = hilo.getValue0();
                    break;
                }
                case RegisterBC: {
                    Pair<Integer, Integer> hilo = get_16bit(value);
                    rB = hilo.getValue1();
                    rC = hilo.getValue0();
                    break;
                }
                case RegisterDE: {
                    Pair<Integer, Integer> hilo = get_16bit(value);
                    rD = hilo.getValue1();
                    rE =  hilo.getValue0();
                    break;
                }
                case RegisterHL: {
                    Pair<Integer, Integer> hilo = get_16bit(value);
                    rH = hilo.getValue1();
                    rL =  hilo.getValue0();
                    break;
                }
                default:
                    break;

            }
        }

        public void set_register_ary(int[] values) {
            if (values.length == 12) {
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
            } else
                clr_registers();
        }

        public int[] get_register_ary() {
            int[] out = {
                    rA, rF, rB, rC, rD, rE, rH, rL, spLO, spHI, pcLO, pcHI
            };
            return out;
        }

    }

    public class instruction {
        public String idisassembly;
        public int ilength;
        public op_functions ifunction;
        public boolean defined;
        public int opcode, prefix;

        instruction(String _idisassembly) {
            idisassembly = _idisassembly;
            defined = false;
        }

        instruction(String _idisassembly, int _opcode, int _prefix, int _ilength, op_functions _ifunction) {
            idisassembly = _idisassembly;
            ilength = _ilength;
            ifunction = _ifunction;
            opcode = _opcode;
            prefix = _prefix;
        }

        void executeinstruction(registers reg, int[] opperands) {
            if (prefix == 0) {
                switch (opcode) {
                    case 0x0: {
                        functions.__NOP(reg, opperands);
                        break;
                    }
                    case 0x1: {
                        functions.__LD_BC_d16(reg, opperands);
                        break;
                    }
                    case 0x2: {
                        functions.__LD_BC_A(reg, opperands);
                        break;
                    }
                    case 0x3: {
                        functions.__INC_BC(reg, opperands);
                        break;
                    }
                    case 0x4: {
                        functions.__INC_B(reg, opperands);
                        break;
                    }
                    case 0x5: {
                        functions.__DEC_B(reg, opperands);
                        break;
                    }
                    case 0x6: {
                        functions.__LD_B_d8(reg, opperands);
                        break;
                    }
                    case 0x7: {
                        functions.__RLCA(reg, opperands);

                        break;
                    }
                    case 0x8: {
                        functions.__LD__a16__SP(reg, opperands);
                        break; 
                    }
                    case 0x9: {
                        functions.__ADD_HL_BC(reg, opperands);
                        break;
                    }
                    case 0xa: {
                        functions.__LD_A__BC__(reg, opperands);
                        break;
                    }
                    case 0xb: {
                        functions.__DEC_C(reg, opperands);
                        break;
                    }
                    case 0xc: {
                        functions.__INC_C(reg, opperands);
                        break;
                    }
                    case 0xd: {
                        functions.__DEC_C(reg, opperands);
                        break;
                    }
                    case 0xe: {
                        functions.__LD_C_d8(reg, opperands);
                        break;
                    }
                    case 0xf: {
                        functions.__RRCA(reg, opperands);
                        break;
                    }
                    case 0x10: {
                        functions.__STOP(reg, opperands);
                        break;
                    }
                    case 0x11: {
                        functions.__LD_DE_d16(reg, opperands);
                        break;
                    }
                    case 0x12: {
                        functions.__LD__DE__A(reg, opperands);
                        break;
                    }
                    case 0x13: {
                        functions.__INC_DE(reg, opperands);
                        break;
                    }
                    case 0x14: {
                        functions.__INC_D(reg, opperands);
                        break;
                    }
                    case 0x15: {
                        functions.__DEC_D(reg, opperands);
                        break;
                    }
                    case 0x16: {
                        functions.__LD_D_d8(reg, opperands);
                        break;
                    }
                    case 0x17: {
                        functions.__RLA(reg, opperands);
                        break;
                    }
                    case 0x18: {
                        functions.__JR_r8(reg, opperands);
                        break;
                    }
                    case 0x19: {
                        functions.__ADD_HL_DE(reg, opperands);
                        break;
                    }
                    case 0x1a: {
                        functions.__LD_A__DE__(reg, opperands);
                        break;
                    }
                    case 0x1b: {
                        functions.__DEC_DE(reg, opperands);
                        break;
                    }
                    case 0x1c: {
                        functions.__INC_E(reg, opperands);
                        break;
                    }
                    case 0x1d: {
                        functions.__DEC_E(reg, opperands);
                        break;
                    }
                    case 0x1e: {
                        functions.__LD_E_d8(reg, opperands);
                        break;
                    }
                    case 0x1f: {
                        functions.__RRA(reg, opperands);
                        break;
                    }
                    case 0x20: {
                        functions.__JR_NZ_r8(reg, opperands);
                        break;
                    }
                    case 0x21: {
                        functions.__LD_HL_d16(reg, opperands);
                        break;
                    }
                    case 0x22: {
                        functions.__LD_H_A(reg, opperands);
                        break;
                    }
                    case 0x23: {
                        functions.__INC_HL(reg, opperands);
                        break;
                    }
                    case 0x24: {
                        functions.__INC_H(reg, opperands);
                        break;
                    }
                    case 0x25: {
                        functions.__DEC_H(reg, opperands);
                        break;
                    }
                    case 0x26: {
                        functions.__LD_H_d8(reg, opperands);
                        break;
                    }
                    case 0x27: {
                        functions.__DAA(reg, opperands);
                        break;
                    }
                    case 0x28: {
                        functions.__JR_Z_r8(reg, opperands);
                        break;
                    }
                    case 0x29: {
                        functions.__ADD_HL_HL(reg, opperands);
                        break;
                    }
                    case 0x2a: {
                        functions.__LD_A__HLI__(reg, opperands);
                        break;
                    }
                    case 0x2b: {
                        functions.__DEC_HL(reg, opperands);
                        break; 
                    }
                    case 0x2c: {
                        functions.__INC_L(reg, opperands);
                        break;
                    }
                    case 0x2d: {
                        functions.__DEC_L(reg, opperands);
                        break;
                    }
                    case 0x2e: {
                        functions.__LD_L_d8(reg, opperands);
                        break;
                    }
                    case 0x2f: {
                        functions.__CPL(reg, opperands);
                        break;
                    }
                    case 0x30: {

                        break;
                    }
                    case 0x31: {

                        break;
                    }
                    case 0x32: {

                        break;
                    }
                    case 0x33: {

                        break;
                    }
                    case 0x34: {

                        break;
                    }
                    case 0x35: {

                        break;
                    }
                    case 0x36: {

                        break;
                    }
                    case 0x37: {

                        break;
                    }
                    case 0x38: {

                        break;
                    }
                    case 0x39: {

                        break;
                    }
                    case 0x3a: {

                        break;
                    }
                    case 0x3b: {

                        break;
                    }
                    case 0x3c: {

                        break;
                    }
                    case 0x3d: {

                        break;
                    }
                    case 0x3e: {

                        break;
                    }
                    case 0x3f: {

                        break;
                    }
                    case 0x40: {

                        break;
                    }
                    case 0x41: {

                        break;
                    }
                    case 0x42: {

                        break;
                    }
                    case 0x43: {

                        break;
                    }
                    case 0x44: {

                        break;
                    }
                    case 0x45: {

                        break;
                    }
                    case 0x46: {

                        break;
                    }
                    case 0x47: {

                        break;
                    }
                    case 0x48: {

                        break;
                    }
                    case 0x49: {

                        break;
                    }
                    case 0x4a: {

                        break;
                    }
                    case 0x4b: {

                        break;
                    }
                    case 0x4c: {

                        break;
                    }
                    case 0x4d: {

                        break;
                    }
                    case 0x4e: {

                        break;
                    }
                    case 0x4f: {

                        break;
                    }
                    case 0x50: {

                        break;
                    }
                    case 0x51: {

                        break;
                    }
                    case 0x52: {

                        break;
                    }
                    case 0x53: {

                        break;
                    }
                    case 0x54: {

                        break;
                    }
                    case 0x55: {

                        break;
                    }
                    case 0x56: {

                        break;
                    }
                    case 0x57: {

                        break;
                    }
                    case 0x58: {

                        break;
                    }
                    case 0x59: {

                        break;
                    }
                    case 0x5a: {

                        break;
                    }
                    case 0x5b: {

                        break;
                    }
                    case 0x5c: {

                        break;
                    }
                    case 0x5d: {

                        break;
                    }
                    case 0x5e: {

                        break;
                    }
                    case 0x5f: {

                        break;
                    }
                    case 0x60: {

                        break;
                    }
                    case 0x61: {

                        break;
                    }
                    case 0x62: {

                        break;
                    }
                    case 0x63: {

                        break;
                    }
                    case 0x64: {

                        break;
                    }
                    case 0x65: {

                        break;
                    }
                    case 0x66: {

                        break;
                    }
                    case 0x67: {

                        break;
                    }
                    case 0x68: {

                        break;
                    }
                    case 0x69: {

                        break;
                    }
                    case 0x6a: {

                        break;
                    }
                    case 0x6b: {

                        break;
                    }
                    case 0x6c: {

                        break;
                    }
                    case 0x6d: {

                        break;
                    }
                    case 0x6e: {

                        break;
                    }
                    case 0x6f: {

                        break;
                    }
                    case 0x70: {

                        break;
                    }
                    case 0x71: {

                        break;
                    }
                    case 0x72: {

                        break;
                    }
                    case 0x73: {

                        break;
                    }
                    case 0x74: {

                        break;
                    }
                    case 0x75: {

                        break;
                    }
                    case 0x76: {

                        break;
                    }
                    case 0x77: {

                        break;
                    }
                    case 0x78: {

                        break;
                    }
                    case 0x79: {

                        break;
                    }
                    case 0x7a: {

                        break;
                    }
                    case 0x7b: {

                        break;
                    }
                    case 0x7c: {

                        break;
                    }
                    case 0x7d: {

                        break;
                    }
                    case 0x7e: {

                        break;
                    }
                    case 0x7f: {

                        break;
                    }
                    case 0x80: {

                        break;
                    }
                    case 0x81: {

                        break;
                    }
                    case 0x82: {

                        break;
                    }
                    case 0x83: {

                        break;
                    }
                    case 0x84: {

                        break;
                    }
                    case 0x85: {

                        break;
                    }
                    case 0x86: {

                        break;
                    }
                    case 0x87: {

                        break;
                    }
                    case 0x88: {

                        break;
                    }
                    case 0x89: {

                        break;
                    }
                    case 0x8a: {

                        break;
                    }
                    case 0x8b: {

                        break;
                    }
                    case 0x8c: {

                        break;
                    }
                    case 0x8d: {

                        break;
                    }
                    case 0x8e: {

                        break;
                    }
                    case 0x8f: {

                        break;
                    }
                    case 0x90: {

                        break;
                    }
                    case 0x91: {

                        break;
                    }
                    case 0x92: {

                        break;
                    }
                    case 0x93: {

                        break;
                    }
                    case 0x94: {

                        break;
                    }
                    case 0x95: {

                        break;
                    }
                    case 0x96: {

                        break;
                    }
                    case 0x97: {

                        break;
                    }
                    case 0x98: {

                        break;
                    }
                    case 0x99: {

                        break;
                    }
                    case 0x9a: {

                        break;
                    }
                    case 0x9b: {

                        break;
                    }
                    case 0x9c: {

                        break;
                    }
                    case 0x9d: {

                        break;
                    }
                    case 0x9e: {

                        break;
                    }
                    case 0x9f: {

                        break;
                    }
                    case 0xa0: {

                        break;
                    }
                    case 0xa1: {

                        break;
                    }
                    case 0xa2: {

                        break;
                    }
                    case 0xa3: {

                        break;
                    }
                    case 0xa4: {

                        break;
                    }
                    case 0xa5: {

                        break;
                    }
                    case 0xa6: {

                        break;
                    }
                    case 0xa7: {

                        break;
                    }
                    case 0xa8: {

                        break;
                    }
                    case 0xa9: {

                        break;
                    }
                    case 0xaa: {

                        break;
                    }
                    case 0xab: {

                        break;
                    }
                    case 0xac: {

                        break;
                    }
                    case 0xad: {

                        break;
                    }
                    case 0xae: {

                        break;
                    }
                    case 0xaf: {

                        break;
                    }
                    case 0xb0: {

                        break;
                    }
                    case 0xb1: {

                        break;
                    }
                    case 0xb2: {

                        break;
                    }
                    case 0xb3: {

                        break;
                    }
                    case 0xb4: {

                        break;
                    }
                    case 0xb5: {

                        break;
                    }
                    case 0xb6: {

                        break;
                    }
                    case 0xb7: {

                        break;
                    }
                    case 0xb8: {

                        break;
                    }
                    case 0xb9: {

                        break;
                    }
                    case 0xba: {

                        break;
                    }
                    case 0xbb: {

                        break;
                    }
                    case 0xbc: {

                        break;
                    }
                    case 0xbd: {

                        break;
                    }
                    case 0xbe: {

                        break;
                    }
                    case 0xbf: {

                        break;
                    }
                    case 0xc0: {

                        break;
                    }
                    case 0xc1: {

                        break;
                    }
                    case 0xc2: {

                        break;
                    }
                    case 0xc3: {

                        break;
                    }
                    case 0xc4: {

                        break;
                    }
                    case 0xc5: {

                        break;
                    }
                    case 0xc6: {

                        break;
                    }
                    case 0xc7: {

                        break;
                    }
                    case 0xc8: {

                        break;
                    }
                    case 0xc9: {

                        break;
                    }
                    case 0xca: {

                        break;
                    }
                    case 0xcb: {

                        break;
                    }
                    case 0xcc: {

                        break;
                    }
                    case 0xcd: {

                        break;
                    }
                    case 0xce: {

                        break;
                    }
                    case 0xcf: {

                        break;
                    }
                    case 0xd0: {

                        break;
                    }
                    case 0xd1: {

                        break;
                    }
                    case 0xd2: {

                        break;
                    }
                    case 0xd3: {

                        break;
                    }
                    case 0xd4: {

                        break;
                    }
                    case 0xd5: {

                        break;
                    }
                    case 0xd6: {

                        break;
                    }
                    case 0xd7: {

                        break;
                    }
                    case 0xd8: {

                        break;
                    }
                    case 0xd9: {

                        break;
                    }
                    case 0xda: {

                        break;
                    }
                    case 0xdb: {

                        break;
                    }
                    case 0xdc: {

                        break;
                    }
                    case 0xdd: {

                        break;
                    }
                    case 0xde: {

                        break;
                    }
                    case 0xdf: {

                        break;
                    }
                    case 0xe0: {

                        break;
                    }
                    case 0xe1: {

                        break;
                    }
                    case 0xe2: {

                        break;
                    }
                    case 0xe3: {

                        break;
                    }
                    case 0xe4: {

                        break;
                    }
                    case 0xe5: {

                        break;
                    }
                    case 0xe6: {

                        break;
                    }
                    case 0xe7: {

                        break;
                    }
                    case 0xe8: {

                        break;
                    }
                    case 0xe9: {

                        break;
                    }
                    case 0xea: {

                        break;
                    }
                    case 0xeb: {

                        break;
                    }
                    case 0xec: {

                        break;
                    }
                    case 0xed: {

                        break;
                    }
                    case 0xee: {

                        break;
                    }
                    case 0xef: {

                        break;
                    }
                    case 0xf0: {

                        break;
                    }
                    case 0xf1: {

                        break;
                    }
                    case 0xf2: {

                        break;
                    }
                    case 0xf3: {

                        break;
                    }
                    case 0xf4: {

                        break;
                    }
                    case 0xf5: {

                        break;
                    }
                    case 0xf6: {

                        break;
                    }
                    case 0xf7: {

                        break;
                    }
                    case 0xf8: {

                        break;
                    }
                    case 0xf9: {

                        break;
                    }
                    case 0xfa: {

                        break;
                    }
                    case 0xfb: {

                        break;
                    }
                    case 0xfc: {

                        break;
                    }
                    case 0xfd: {

                        break;
                    }
                    case 0xfe: {

                        break;
                    }
                    default:{
                        gb_execeptions.gb_exception("0x%x notdefined",opcode);
                        break;
                    }
                }
            }
            if (prefix == 1) {
                switch (opcode) {
                    case 0x0: {

                        break;
                    }
                    case 0x1: {

                        break;
                    }
                    case 0x2: {

                        break;
                    }
                    case 0x3: {

                        break;
                    }
                    case 0x4: {

                        break;
                    }
                    case 0x5: {

                        break;
                    }
                    case 0x6: {

                        break;
                    }
                    case 0x7: {

                        break;
                    }
                    case 0x8: {

                        break;
                    }
                    case 0x9: {

                        break;
                    }
                    case 0xa: {

                        break;
                    }
                    case 0xb: {

                        break;
                    }
                    case 0xc: {

                        break;
                    }
                    case 0xd: {

                        break;
                    }
                    case 0xe: {

                        break;
                    }
                    case 0xf: {

                        break;
                    }
                    case 0x10: {

                        break;
                    }
                    case 0x11: {

                        break;
                    }
                    case 0x12: {

                        break;
                    }
                    case 0x13: {

                        break;
                    }
                    case 0x14: {

                        break;
                    }
                    case 0x15: {

                        break;
                    }
                    case 0x16: {

                        break;
                    }
                    case 0x17: {

                        break;
                    }
                    case 0x18: {

                        break;
                    }
                    case 0x19: {

                        break;
                    }
                    case 0x1a: {

                        break;
                    }
                    case 0x1b: {

                        break;
                    }
                    case 0x1c: {

                        break;
                    }
                    case 0x1d: {

                        break;
                    }
                    case 0x1e: {

                        break;
                    }
                    case 0x1f: {

                        break;
                    }
                    case 0x20: {

                        break;
                    }
                    case 0x21: {

                        break;
                    }
                    case 0x22: {

                        break;
                    }
                    case 0x23: {

                        break;
                    }
                    case 0x24: {

                        break;
                    }
                    case 0x25: {

                        break;
                    }
                    case 0x26: {

                        break;
                    }
                    case 0x27: {

                        break;
                    }
                    case 0x28: {

                        break;
                    }
                    case 0x29: {

                        break;
                    }
                    case 0x2a: {

                        break;
                    }
                    case 0x2b: {

                        break;
                    }
                    case 0x2c: {

                        break;
                    }
                    case 0x2d: {

                        break;
                    }
                    case 0x2e: {

                        break;
                    }
                    case 0x2f: {

                        break;
                    }
                    case 0x30: {

                        break;
                    }
                    case 0x31: {

                        break;
                    }
                    case 0x32: {

                        break;
                    }
                    case 0x33: {

                        break;
                    }
                    case 0x34: {

                        break;
                    }
                    case 0x35: {

                        break;
                    }
                    case 0x36: {

                        break;
                    }
                    case 0x37: {

                        break;
                    }
                    case 0x38: {

                        break;
                    }
                    case 0x39: {

                        break;
                    }
                    case 0x3a: {

                        break;
                    }
                    case 0x3b: {

                        break;
                    }
                    case 0x3c: {

                        break;
                    }
                    case 0x3d: {

                        break;
                    }
                    case 0x3e: {

                        break;
                    }
                    case 0x3f: {

                        break;
                    }
                    case 0x40: {

                        break;
                    }
                    case 0x41: {

                        break;
                    }
                    case 0x42: {

                        break;
                    }
                    case 0x43: {

                        break;
                    }
                    case 0x44: {

                        break;
                    }
                    case 0x45: {

                        break;
                    }
                    case 0x46: {

                        break;
                    }
                    case 0x47: {

                        break;
                    }
                    case 0x48: {

                        break;
                    }
                    case 0x49: {

                        break;
                    }
                    case 0x4a: {

                        break;
                    }
                    case 0x4b: {

                        break;
                    }
                    case 0x4c: {

                        break;
                    }
                    case 0x4d: {

                        break;
                    }
                    case 0x4e: {

                        break;
                    }
                    case 0x4f: {

                        break;
                    }
                    case 0x50: {

                        break;
                    }
                    case 0x51: {

                        break;
                    }
                    case 0x52: {

                        break;
                    }
                    case 0x53: {

                        break;
                    }
                    case 0x54: {

                        break;
                    }
                    case 0x55: {

                        break;
                    }
                    case 0x56: {

                        break;
                    }
                    case 0x57: {

                        break;
                    }
                    case 0x58: {

                        break;
                    }
                    case 0x59: {

                        break;
                    }
                    case 0x5a: {

                        break;
                    }
                    case 0x5b: {

                        break;
                    }
                    case 0x5c: {

                        break;
                    }
                    case 0x5d: {

                        break;
                    }
                    case 0x5e: {

                        break;
                    }
                    case 0x5f: {

                        break;
                    }
                    case 0x60: {

                        break;
                    }
                    case 0x61: {

                        break;
                    }
                    case 0x62: {

                        break;
                    }
                    case 0x63: {

                        break;
                    }
                    case 0x64: {

                        break;
                    }
                    case 0x65: {

                        break;
                    }
                    case 0x66: {

                        break;
                    }
                    case 0x67: {

                        break;
                    }
                    case 0x68: {

                        break;
                    }
                    case 0x69: {

                        break;
                    }
                    case 0x6a: {

                        break;
                    }
                    case 0x6b: {

                        break;
                    }
                    case 0x6c: {

                        break;
                    }
                    case 0x6d: {

                        break;
                    }
                    case 0x6e: {

                        break;
                    }
                    case 0x6f: {

                        break;
                    }
                    case 0x70: {

                        break;
                    }
                    case 0x71: {

                        break;
                    }
                    case 0x72: {

                        break;
                    }
                    case 0x73: {

                        break;
                    }
                    case 0x74: {

                        break;
                    }
                    case 0x75: {

                        break;
                    }
                    case 0x76: {

                        break;
                    }
                    case 0x77: {

                        break;
                    }
                    case 0x78: {

                        break;
                    }
                    case 0x79: {

                        break;
                    }
                    case 0x7a: {

                        break;
                    }
                    case 0x7b: {

                        break;
                    }
                    case 0x7c: {

                        break;
                    }
                    case 0x7d: {

                        break;
                    }
                    case 0x7e: {

                        break;
                    }
                    case 0x7f: {

                        break;
                    }
                    case 0x80: {

                        break;
                    }
                    case 0x81: {

                        break;
                    }
                    case 0x82: {

                        break;
                    }
                    case 0x83: {

                        break;
                    }
                    case 0x84: {

                        break;
                    }
                    case 0x85: {

                        break;
                    }
                    case 0x86: {

                        break;
                    }
                    case 0x87: {

                        break;
                    }
                    case 0x88: {

                        break;
                    }
                    case 0x89: {

                        break;
                    }
                    case 0x8a: {

                        break;
                    }
                    case 0x8b: {

                        break;
                    }
                    case 0x8c: {

                        break;
                    }
                    case 0x8d: {

                        break;
                    }
                    case 0x8e: {

                        break;
                    }
                    case 0x8f: {

                        break;
                    }
                    case 0x90: {

                        break;
                    }
                    case 0x91: {

                        break;
                    }
                    case 0x92: {

                        break;
                    }
                    case 0x93: {

                        break;
                    }
                    case 0x94: {

                        break;
                    }
                    case 0x95: {

                        break;
                    }
                    case 0x96: {

                        break;
                    }
                    case 0x97: {

                        break;
                    }
                    case 0x98: {

                        break;
                    }
                    case 0x99: {

                        break;
                    }
                    case 0x9a: {

                        break;
                    }
                    case 0x9b: {

                        break;
                    }
                    case 0x9c: {

                        break;
                    }
                    case 0x9d: {

                        break;
                    }
                    case 0x9e: {

                        break;
                    }
                    case 0x9f: {

                        break;
                    }
                    case 0xa0: {

                        break;
                    }
                    case 0xa1: {

                        break;
                    }
                    case 0xa2: {

                        break;
                    }
                    case 0xa3: {

                        break;
                    }
                    case 0xa4: {

                        break;
                    }
                    case 0xa5: {

                        break;
                    }
                    case 0xa6: {

                        break;
                    }
                    case 0xa7: {

                        break;
                    }
                    case 0xa8: {

                        break;
                    }
                    case 0xa9: {

                        break;
                    }
                    case 0xaa: {

                        break;
                    }
                    case 0xab: {

                        break;
                    }
                    case 0xac: {

                        break;
                    }
                    case 0xad: {

                        break;
                    }
                    case 0xae: {

                        break;
                    }
                    case 0xaf: {

                        break;
                    }
                    case 0xb0: {

                        break;
                    }
                    case 0xb1: {

                        break;
                    }
                    case 0xb2: {

                        break;
                    }
                    case 0xb3: {

                        break;
                    }
                    case 0xb4: {

                        break;
                    }
                    case 0xb5: {

                        break;
                    }
                    case 0xb6: {

                        break;
                    }
                    case 0xb7: {

                        break;
                    }
                    case 0xb8: {

                        break;
                    }
                    case 0xb9: {

                        break;
                    }
                    case 0xba: {

                        break;
                    }
                    case 0xbb: {

                        break;
                    }
                    case 0xbc: {

                        break;
                    }
                    case 0xbd: {

                        break;
                    }
                    case 0xbe: {

                        break;
                    }
                    case 0xbf: {

                        break;
                    }
                    case 0xc0: {

                        break;
                    }
                    case 0xc1: {

                        break;
                    }
                    case 0xc2: {

                        break;
                    }
                    case 0xc3: {

                        break;
                    }
                    case 0xc4: {

                        break;
                    }
                    case 0xc5: {

                        break;
                    }
                    case 0xc6: {

                        break;
                    }
                    case 0xc7: {

                        break;
                    }
                    case 0xc8: {

                        break;
                    }
                    case 0xc9: {

                        break;
                    }
                    case 0xca: {

                        break;
                    }
                    case 0xcb: {

                        break;
                    }
                    case 0xcc: {

                        break;
                    }
                    case 0xcd: {

                        break;
                    }
                    case 0xce: {

                        break;
                    }
                    case 0xcf: {

                        break;
                    }
                    case 0xd0: {

                        break;
                    }
                    case 0xd1: {

                        break;
                    }
                    case 0xd2: {

                        break;
                    }
                    case 0xd3: {

                        break;
                    }
                    case 0xd4: {

                        break;
                    }
                    case 0xd5: {

                        break;
                    }
                    case 0xd6: {

                        break;
                    }
                    case 0xd7: {

                        break;
                    }
                    case 0xd8: {

                        break;
                    }
                    case 0xd9: {

                        break;
                    }
                    case 0xda: {

                        break;
                    }
                    case 0xdb: {

                        break;
                    }
                    case 0xdc: {

                        break;
                    }
                    case 0xdd: {

                        break;
                    }
                    case 0xde: {

                        break;
                    }
                    case 0xdf: {

                        break;
                    }
                    case 0xe0: {

                        break;
                    }
                    case 0xe1: {

                        break;
                    }
                    case 0xe2: {

                        break;
                    }
                    case 0xe3: {

                        break;
                    }
                    case 0xe4: {

                        break;
                    }
                    case 0xe5: {

                        break;
                    }
                    case 0xe6: {

                        break;
                    }
                    case 0xe7: {

                        break;
                    }
                    case 0xe8: {

                        break;
                    }
                    case 0xe9: {

                        break;
                    }
                    case 0xea: {

                        break;
                    }
                    case 0xeb: {

                        break;
                    }
                    case 0xec: {

                        break;
                    }
                    case 0xed: {

                        break;
                    }
                    case 0xee: {

                        break;
                    }
                    case 0xef: {

                        break;
                    }
                    case 0xf0: {

                        break;
                    }
                    case 0xf1: {

                        break;
                    }
                    case 0xf2: {

                        break;
                    }
                    case 0xf3: {

                        break;
                    }
                    case 0xf4: {

                        break;
                    }
                    case 0xf5: {

                        break;
                    }
                    case 0xf6: {

                        break;
                    }
                    case 0xf7: {

                        break;
                    }
                    case 0xf8: {

                        break;
                    }
                    case 0xf9: {

                        break;
                    }
                    case 0xfa: {

                        break;
                    }
                    case 0xfb: {

                        break;
                    }
                    case 0xfc: {

                        break;
                    }
                    case 0xfd: {

                        break;
                    }
                    case 0xfe: {

                        break;
                    }
                    default:{
                        gb_execeptions.gb_exception("0x%x notdefined",opcode);
                        break;
                    }
                }
            }

        }

    }

    private gb_bus bus;
    private gb_handle handle;
    private gb_handle.GAMEBOY_TYPE type;
    private registers cpureg;


    private Pair<Integer, Integer> get_16bit(int get) {
        if (get < gb_bitfunctions.__u_word_min__ && get >= gb_bitfunctions.__u_word_max__)
            get &= 0xffff;
        int hi = get & 0x00ff, lo = get & 0xff;
        return new Pair<Integer,Integer>(lo, hi);
    }

    private int set_16bit(Pair<Integer,Integer> set) {
        int lo = set.getValue0();
        int hi = set.getValue1();
        if ((hi < gb_bitfunctions.__u_byte_min__ && hi >= gb_bitfunctions.__u_byte_max__)
                || (lo >= gb_bitfunctions.__u_byte_min__ && lo < gb_bitfunctions.__u_byte_max__)) {
            hi &= 0x00ff;
            lo &= 0xff;
        }
        int result = (hi << 8);
        return result + lo;
    }

    private final int flaglen = 2;
    private boolean debug, definedinstructions;
    private op_functions functions = new op_functions();

    public gb_cpu(gb_bus _gbbus, boolean[] flags) {

        bus = _gbbus;
        type = handle.gbtype;
        if (flags == null || flags.length != flaglen) {
            debug = false;
            definedinstructions = false;
        } else {
            debug = (flags[0]) ? true : false;
            definedinstructions = (flags[1]) ? true : false;

        }
        cpureg = new registers(type);
    }

    public final int __word16_hi__ = 0;
    public final int __word16_lo__ = 1;
    public final int __ppu_address_max__ = 0xfff, __ppu_address_min__ = 0;

    public void gb_snoopbus(int[] data) {
        opperands = data;
    }

    public boolean update = true;
    public long duration;
    public Vector<Pair<Integer,Integer>> opcodelist;
    int[] opperands = null;

    public void gb_cpurun() {
        int count = 0;
        duration = (handle.gbtype == gb_handle.GAMEBOY_TYPE.GBC) ? gb_handle.__gbc_hzclock__ / 60
                : gb_handle.__gb_hzclock__ / 60;
        while (update == true) {
            int opcode = 0, prefix = 0;

            if (definedinstructions == true) {
                Pair<Integer,Integer> temp = opcodelist.get(count++);
                prefix = temp.getValue0();
                opcode = temp.getValue1();

            } else if (definedinstructions == false) {
                
            }
            instruction instruction = operations[prefix][opcode];

            instruction.executeinstruction(cpureg, opperands);

        }

    }

    instruction[][] operations = {
        {
            //0x00-0x0f
            //no o
            new instruction("NOP",0x00,0,1,functions),
            new instruction("LD BC,d16",0x01,0,3,functions),
            new instruction("LD (BC),A",0x02,0,1,functions),
            new instruction("INC BC",0x03,0,1,functions),
            new instruction("INC B",0x04,0,1,functions),
            new instruction("DEC B",0x05,0,1,functions),
            new instruction("LD B,d8",0x06,0,1,functions),
            new instruction("RLCA",0x07,0,1,functions),
            new instruction("LD (a16),SP",0x08,0,3,functions),
            new instruction("ADD HL,BC",0x09,0,1,functions),
            new instruction("LD A,(BC)",0x0a,0,1,functions),
            new instruction("DEC BC",0x0b,0,1,functions),
            new instruction("INC C",0x0c,0,1,functions),
            new instruction("DEC C",0x0d,0,1,functions),
            new instruction("LD C,d8",0x0e,0,2,functions),
            new instruction("RRCA",0x0f,0,1,functions),
            //0x10-0x1f

            new instruction("STOP",0x10,0,2,functions),
            new instruction("LD DE,d16",0x11,0,3,functions),
            new instruction("LD (DE),A",0x12,0,1,functions),
            new instruction("INC DE",0x13,0,1,functions),
            new instruction("INC D",0x14,0,1,functions),
            new instruction("DEC D",0x15,0,1,functions),
            new instruction("LD D,d8",0x16,0,2,functions),
            new instruction("RLA",0x17,0,1,functions),
            new instruction("JR r8",0x18,0,2,functions),
            new instruction("ADD HL,DE",0x19,0,1,functions),
            new instruction("LD A,(DE)",0x1a,0,1,functions),
            new instruction("DEC DE",0x1b,0,1,functions),
            new instruction("INC E",0x1c,0,1,functions),
            new instruction("DEC E",0x1d,0,1,functions),
            new instruction("LD E,d8",0x1e,0,2,functions),
            new instruction("RRA",0x1f,0,1,functions),

            //0x20-0x2f

            new instruction("JR NZ R8",0x20,0,2,functions),
            new instruction("LD HL,d16",0x21,0,3,functions),
            new instruction("LD (HL),A",0x22,0,1,functions),
            new instruction("INC HL",0x23,0,1,functions),
            new instruction("INC H",0x24,0,1,functions),
            new instruction("DEC H",0x25,0,1,functions),
            new instruction("LD H,d8",0x26,0,2,functions),
            new instruction("DAA",0x27,0,1,functions),
            new instruction("JR Z,r8",0x28,0,2,functions),
            new instruction("ADD HL,HL",0x29,0,1,functions),
            new instruction("LD A,(HL+)",0x2a,0,1,functions),
            new instruction("DEC HL",0x2b,0,1,functions),
            new instruction("INC L",0x2c,0,1,functions),
            new instruction("DEC L",0x2d,0,1,functions),
            new instruction("LD L,d8",0x2e,0,2,functions),
            new instruction("CPL",0x2f,0,1,functions),
            //0x30-0x3f

            new instruction("JR NC,R8",0x30,0,2,functions),
            new instruction("LD SP,d16",0x31,0 ,3,functions),
            new instruction("LD (SP),A",0x32,0,1,functions),
            new instruction("INC SP",0x33,0,1,functions),
            new instruction("INC HL",0x34,0,1,functions),
            new instruction("DEC HL",0x35,0,1,functions),
            new instruction("LD HL,d8",0x36,0,2,functions),
            new instruction("SCF",0x37,0,1,functions),
            new instruction("JR C, r8",0x38,0,2,functions),
            new instruction("ADD HL,SP",0x39,0,1,functions),
            new instruction("LD A,(HL-)",0x3a,0,1,functions),
            new instruction("DEC SP",0x3b,0,1,functions),
            new instruction("INC A",0x3c,0,1,functions),
            new instruction("DEC A",0x3d,0,1,functions),
            new instruction("LD A,d8",0x3e,0,2,functions),
            new instruction("CCF",0x3f,0,1,functions),
            //0x40-0x4f

            new instruction("LD B,B",0x40,0,1,functions),

            //0x50-0x5f

            new instruction("LD D,B",0x50,0,1,functions),
            
            //0x60-0x6f

            new instruction("LD H,B",0x60,0,1,functions),

            //0x70-0x7f

            new instruction("LD (HL),B",0x70,0,1,functions),

            //0x80-0x8f

            new instruction("ADD A,B",0x80,0,1,functions),

            //0x90-0x9f

            new instruction("SUB B",0x90,0,1,functions),


            //0xa0-0xaf

            new instruction("AND B",0xa0,0,1,functions),
            
            //0xb0-0xbf

            new instruction("OR B",0xb0,0,1,functions),

            //0xc0-0xcf

            new instruction("RET NZ",0xc0,0,1,functions),
            
            //0xd0-0xdf

            new instruction("RET NC",0xd0,0,1,functions),

            //0xe0-0xef

            new instruction("LDH (a8),A",0xe0,0,2,functions),

            //0xf0-0xff

            new instruction("LDH A,(a8)",0xf0,0,2,functions),
        },
        {
        
        }
    };

    public class op_functions {

        private void onecycle() {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                gb_execeptions.gb_exception(e.getMessage());
            }
        }

        public void __NOP(registers reg, int[] opperands) {
            onecycle();
            return;
        }

        public void __LD_BC_A(registers reg, int[] opperands) {
            return;
        }

        public void __INC_BC(registers reg, int[] opperands) {
            return;
        }

        public void __LD_BC_d16(registers reg, int[] opperands) {
            return;
        }

        public void __DEC_B(registers reg, int[] opperands) {
            return;
        }

        public void __INC_B(registers reg, int[] opperands) {
            return;
        }

        public void __RLCA(registers reg, int[] opperands) {
            return;
        }

        public void __LD_B_d8(registers reg, int[] opperands) {
            return;
        }

        public void __ADD_HL_BC(registers reg, int[] opperands) {
        }

        public void __LD__a16__SP(registers reg, int[] opperands) {
        }

        public void __DEC_BC(registers reg, int[] opperands) {
        }

        public void __LD_A__BC__(registers reg, int[] opperands) {
        }

        public void __DEC_C(registers reg, int[] opperands) {
        }

        public void __INC_C(registers reg, int[] opperands) {
        }

        public void __LD_C_d8(registers reg, int[] opperands) {
        }

        public void __LD_DE_d16(registers reg, int[] opperands) {
        }

        public void __STOP(registers reg, int[] opperands) {
            update = false;
            onecycle();
            return;
        }

        public void __INC_DE(registers reg, int[] opperands) {
            return;
        }

        public void __LD__DE__A(registers reg, int[] opperands) {
            return;
        }

        public void __DEC_D(registers reg, int[] opperands) {
            return;
        }

        public void __INC_D(registers reg, int[] opperands) {
            return;
        }

        public void __LD_D_d8(registers reg, int[] opperands) {
            return;
        }

        public void __ADD_HL_DE(registers reg, int[] opperands) {
            return;
        }

        public void __JR_r8(registers reg, int[] opperands) {
            return;
        }

        public void __DEC_DE(registers reg, int[] opperands) {
            return;
        }

        public void __LD_A__DE__(registers reg, int[] opperands) {
            return;
        }

        public void __DEC_E(registers reg, int[] opperands) {
            return;
        }

        public void __INC_E(registers reg, int[] opperands) {
            return;
        }

        public void __LD_E_d8(registers reg, int[] opperands) {
            return;
        }

        public void __LD_HL_d16(registers reg, int[] opperands) {
            return;
        }

        public void __JR_NZ_r8(registers reg, int[] opperands) {
            return;
        }

        public void __INC_HL(registers reg, int[] opperands) {
            return;
        }

        public void __LD__HLI__A(registers reg, int[] opperands) {
            return;
        }

        public void __DEC_H(registers reg, int[] opperands) {
            return;
        }

        public void __INC_H(registers reg, int[] opperands) {
            return;
        }

        public void __DAA(registers reg, int[] opperands) {
            return;
        }

        public void __LD_H_d8(registers reg, int[] opperands) {
            return;
        }

        public void __ADD_HL_HL(registers reg, int[] opperands) {
            return;
        }

        public void __JR_Z_r8(registers reg, int[] opperands) {
            return;
        }

        public void __DEC_HL(registers reg, int[] opperands) {
            return;
        }

        public void __LD_A__HLI__(registers reg, int[] opperands) {
            return;
        }

        public void __DEC_L(registers reg, int[] opperands) {
            return;
        }

        public void __INC_L(registers reg, int[] opperands) {
            return;
        }

        public void __CP(registers reg, int[] opperands) {
            return;
        }

        public void __LD_L_d8(registers reg, int[] opperands) {
            return;
        }

        public void __LD_SP_d16(registers reg, int[] opperands) {
            return;
        }

        public void __JR_NC_r8(registers reg, int[] opperands) {
            return;
        }

        public void __INC_SP(registers reg, int[] opperands) {
            return;
        }

        public void __LD__HLD__A(registers reg, int[] opperands) {
            return;
        }

        public void __DEC__HL__(registers reg, int[] opperands) {
            return;
        }

        public void __INC__HL__(registers reg, int[] opperands) {
            return;
        }

        public void __SCF(registers reg, int[] opperands) {
            return;
        }

        public void __LD__HL__d8(registers reg, int[] opperands) {
            return;
        }

        public void __ADD_HL_SP(registers reg, int[] opperands) {
            return;
        }

        public void __JR_C_r8(registers reg, int[] opperands) {
            return;
        }

        public void __DEC_SP(registers reg, int[] opperands) {
            return;
        }

        public void __LD_A__HLD__(registers reg, int[] opperands) {
            return;
        }

        public void __DEC_A(registers reg, int[] opperands) {
            return;
        }

        public void __INC_A(registers reg, int[] opperands) {
            return;
        }

        public void __LD_A_d8(registers reg, int[] opperands) {
            return;
        }

        public void __LD_B_C(registers reg, int[] opperands) {
            return;
        }

        public void __LD_B_B(registers reg, int[] opperands) {
            return;
        }

        public void __LD_B_E(registers reg, int[] opperands) {
            return;
        }

        public void __LD_B_D(registers reg, int[] opperands) {
            return;
        }

        public void __LD_B_L(registers reg, int[] opperands) {
            return;
        }

        public void __LD_B_H(registers reg, int[] opperands) {
            return;
        }

        public void __LD_B_A(registers reg, int[] opperands) {
            return;
        }

        public void __LD_B__HL__(registers reg, int[] opperands) {
            return;
        }

        public void __LD_C_C(registers reg, int[] opperands) {
            return;
        }

        public void __LD_C_B(registers reg, int[] opperands) {
            return;
        }

        public void __LD_C_E(registers reg, int[] opperands) {
            return;
        }

        public void __LD_C_D(registers reg, int[] opperands) {
            return;
        }

        public void __LD_C_L(registers reg, int[] opperands) {
            return;
        }

        public void __LD_C_H(registers reg, int[] opperands) {
            return;
        }

        public void __LD_C_A(registers reg, int[] opperands) {
            return;
        }

        public void __LD_C__HL_(registers reg, int[] opperands) {
            return;
        }

        public void __LD_D_C(registers reg, int[] opperands) {
            return;
        }

        public void __LD_D_B(registers reg, int[] opperands) {
            return;
        }

        public void __LD_D_E(registers reg, int[] opperands) {
            return;
        }

        public void __LD_D_D(registers reg, int[] opperands) {
            return;
        }

        public void __LD_D_L(registers reg, int[] opperands) {
            return;
        }

        public void __LD_D_H(registers reg, int[] opperands) {
            return;
        }

        public void __LD_D_A(registers reg, int[] opperands) {
            return;
        }

        public void __LD_D__HL__(registers reg, int[] opperands) {
            return;
        }

        public void __LD_E_C(registers reg, int[] opperands) {
            return;
        }

        public void __LD_E_B(registers reg, int[] opperands) {
            return;
        }

        public void __LD_E_E(registers reg, int[] opperands) {
            return;
        }

        public void __LD_E_D(registers reg, int[] opperands) {
            return;
        }

        public void __LD_E_L(registers reg, int[] opperands) {
            return;
        }

        public void __LD_E_H(registers reg, int[] opperands) {
            return;
        }

        public void __LD_E_A(registers reg, int[] opperands) {
            return;
        }

        public void __LD_E__HL_(registers reg, int[] opperands) {
            return;
        }

        public void __LD_H_C(registers reg, int[] opperands) {
            return;
        }

        public void __LD_H_B(registers reg, int[] opperands) {
            return;
        }

        public void __LD_H_E(registers reg, int[] opperands) {
            return;
        }

        public void __LD_H_D(registers reg, int[] opperands) {
            return;
        }

        public void __LD_H_L(registers reg, int[] opperands) {
            return;
        }

        public void __LD_H_H(registers reg, int[] opperands) {
            return;
        }

        public void __LD_H_A(registers reg, int[] opperands) {
            return;
        }

        public void __LD_H__HL__(registers reg, int[] opperands) {
            return;
        }

        public void __LD_L_C(registers reg, int[] opperands) {
            return;
        }

        public void __LD_L_B(registers reg, int[] opperands) {
            return;
        }

        public void __LD_L_E(registers reg, int[] opperands) {
            return;
        }

        public void __LD_L_D(registers reg, int[] opperands) {
            return;
        }

        public void __LD_L_L(registers reg, int[] opperands) {
            return;
        }

        public void __LD_L_H(registers reg, int[] opperands) {
            return;
        }

        public void __LD_L_A(registers reg, int[] opperands) {
            return;
        }

        public void __LD__HL__B(registers reg, int[] opperands) {
            return;
        }

        public void __LD_L__HL__(registers reg, int[] opperands) {
            return;
        }

        public void __LD__HL__D(registers reg, int[] opperands) {
            return;
        }

        public void __LD__HL__C(registers reg, int[] opperands) {
            return;
        }

        public void __LD__HL__H(registers reg, int[] opperands) {
            return;
        }

        public void __LD__HL__E(registers reg, int[] opperands) {
            return;
        }

        public void __HALT(registers reg, int[] opperands) {
            return;
        }

        public void __LD__HL__L(registers reg, int[] opperands) {
            return;
        }

        public void __LD_A_B(registers reg, int[] opperands) {
            return;
        }

        public void __LD__HL__A(registers reg, int[] opperands) {
            return;
        }

        public void __LD_A_D(registers reg, int[] opperands) {
            return;
        }

        public void __LD_A_C(registers reg, int[] opperands) {
            return;
        }

        public void __LD_A_H(registers reg, int[] opperands) {
            return;
        }

        public void __LD_A_E(registers reg, int[] opperands) {
            return;
        }

        public void __LD_A__HL_(registers reg, int[] opperands) {
            return;
        }

        public void __LD_A_L(registers reg, int[] opperands) {
            return;
        }

        public void __ADD_A_B(registers reg, int[] opperands) {
            return;
        }

        public void __LD_A_A(registers reg, int[] opperands) {
            return;
        }

        public void __ADD_A_D(registers reg, int[] opperands) {
            return;
        }

        public void __ADD_A_C(registers reg, int[] opperands) {
            return;
        }

        public void __ADD_A_H(registers reg, int[] opperands) {
            return;
        }

        public void __ADD_A_E(registers reg, int[] opperands) {
            return;
        }

        public void __ADD_A__HL_(registers reg, int[] opperands) {
            return;
        }

        public void __ADD_A_L(registers reg, int[] opperands) {
            return;
        }

        public void __ADC_A_B(registers reg, int[] opperands) {
            return;
        }

        public void __ADD_A_A(registers reg, int[] opperands) {
            return;
        }

        public void __ADC_A_D(registers reg, int[] opperands) {
            return;
        }

        public void __ADC_A_C(registers reg, int[] opperands) {
            return;
        }

        public void __ADC_A_H(registers reg, int[] opperands) {
            return;
        }

        public void __ADC_A_E(registers reg, int[] opperands) {
            return;
        }

        public void __ADC_A__HL_(registers reg, int[] opperands) {
            return;
        }

        public void __ADC_A_L(registers reg, int[] opperands) {
            return;
        }

        public void __SUB_B(registers reg, int[] opperands) {
            return;
        }

        public void __ADC_A_(registers reg, int[] opperands) {
            return;
        }

        public void __SUB_D(registers reg, int[] opperands) {
            return;
        }

        public void __SUB_C(registers reg, int[] opperands) {
            return;
        }

        public void __SUB_H(registers reg, int[] opperands) {
            return;
        }

        public void __SUB_E(registers reg, int[] opperands) {
            return;
        }

        public void __SUB__HL_(registers reg, int[] opperands) {
            return;
        }

        public void __SUB_L(registers reg, int[] opperands) {
            return;
        }

        public void __SBC_A_B(registers reg, int[] opperands) {
            return;
        }

        public void __SUB_A(registers reg, int[] opperands) {
            return;
        }

        public void __SBC_A_D(registers reg, int[] opperands) {
            return;
        }

        public void __SBC_A_C(registers reg, int[] opperands) {
            return;
        }

        public void __SBC_A_H(registers reg, int[] opperands) {
            return;
        }

        public void __SBC_A_E(registers reg, int[] opperands) {
            return;
        }

        public void __SBC_A__HL_(registers reg, int[] opperands) {
            return;
        }

        public void __SBC_A_L(registers reg, int[] opperands) {
            return;
        }

        public void __AND_B(registers reg, int[] opperands) {
            return;
        }

        public void __SBC_A_A(registers reg, int[] opperands) {
            return;
        }

        public void __AND_D(registers reg, int[] opperands) {
            return;
        }

        public void __AND_C(registers reg, int[] opperands) {
            return;
        }

        public void __AND_H(registers reg, int[] opperands) {
            return;
        }

        public void __AND_E(registers reg, int[] opperands) {
            return;
        }

        public void __AND__HL_(registers reg, int[] opperands) {
            return;
        }

        public void __AND_L(registers reg, int[] opperands) {
            return;
        }

        public void __XOR_B(registers reg, int[] opperands) {
            return;
        }

        public void __AND_A(registers reg, int[] opperands) {
            return;
        }

        public void __XOR_D(registers reg, int[] opperands) {
            return;
        }

        public void __XOR_C(registers reg, int[] opperands) {
            return;
        }

        public void __XOR_H(registers reg, int[] opperands) {
            return;
        }

        public void __XOR_E(registers reg, int[] opperands) {
            return;
        }

        public void __XOR__HL_(registers reg, int[] opperands) {
            return;
        }

        public void __XOR_L(registers reg, int[] opperands) {
            return;
        }

        public void __OR_B(registers reg, int[] opperands) {
            return;
        }

        public void __XOR_A(registers reg, int[] opperands) {
            return;
        }

        public void __OR_D(registers reg, int[] opperands) {
            return;
        }

        public void __OR_C(registers reg, int[] opperands) {
            return;
        }

        public void __OR_H(registers reg, int[] opperands) {
            return;
        }

        public void __OR_E(registers reg, int[] opperands) {
            return;
        }

        public void __OR__HL__(registers reg, int[] opperands) {
            return;
        }

        public void __OR_L(registers reg, int[] opperands) {
            return;
        }

        public void __CP_B(registers reg, int[] opperands) {
            return;
        }

        public void __OR_A(registers reg, int[] opperands) {
            return;
        }

        public void __CP_D(registers reg, int[] opperands) {
            return;
        }

        public void __CP_C(registers reg, int[] opperands) {
            return;
        }

        public void __CP_H(registers reg, int[] opperands) {
            return;
        }

        public void __CP_E(registers reg, int[] opperands) {
            return;
        }

        public void __CP__HL_(registers reg, int[] opperands) {
            return;
        }

        public void __CP_L(registers reg, int[] opperands) {
            return;
        }

        public void __RET_NZ(registers reg, int[] opperands) {
            return;
        }

        public void __CP_A(registers reg, int[] opperands) {
            return;
        }

        public void __JP_NZ_a16(registers reg, int[] opperands) {
            return;
        }

        public void __POP_BC(registers reg, int[] opperands) {
            return;
        }

        public void __CALL_NZ_a16(registers reg, int[] opperands) {
            return;
        }

        public void __JP_a16(registers reg, int[] opperands) {
            return;
        }

        public void __ADD_A_d8(registers reg, int[] opperands) {
            return;
        }

        public void __PUSH_BC(registers reg, int[] opperands) {
            return;
        }

        public void __RET_Z(registers reg, int[] opperands) {
            return;
        }

        public void __RST(registers reg, int[] opperands) {
            return;
        }

        public void __JP_Z_a16(registers reg, int[] opperands) {
            return;
        }

        public void __RET(registers reg, int[] opperands) {
            return;
        }

        public void __CALL_Z_a16(registers reg, int[] opperands) {
            return;
        }

        public void __PREFIX_CB(registers reg, int[] opperands) {
            return;
        }

        public void __CALL_a16(registers reg, int[] opperands) {
            return;
        }

        public void __ADC_A_d8(registers reg, int[] opperands) {
            return;
        }

        public void __RET_NC(registers reg, int[] opperands) {
            return;
        }

        public void __RST_08(registers reg, int[] opperands) {
            return;
        }

        public void __JP_NC_a16(registers reg, int[] opperands) {
            return;
        }

        public void __POP_DE(registers reg, int[] opperands) {
            return;
        }

        public void __PUSH_DE(registers reg, int[] opperands) {
            return;
        }

        public void __CALL_NC_a16(registers reg, int[] opperands) {
            return;
        }

        public void __RST_10H(registers reg, int[] opperands) {
            return;
        }

        public void __SUB_d8(registers reg, int[] opperands) {
            return;
        }

        public void __RETI(registers reg, int[] opperands) {
            return;
        }

        public void __RET_C(registers reg, int[] opperands) {
            return;
        }

        public void __CALL_C_a16(registers reg, int[] opperands) {
            return;
        }

        public void __JP_C_a16(registers reg, int[] opperands) {
            return;
        }

        public void __RST_18(registers reg, int[] opperands) {
            return;
        }

        public void __SBC_A_d8(registers reg, int[] opperands) {
            return;
        }

        public void __POP_HL(registers reg, int[] opperands) {
            return;
        }

        public void __LDH__a8__A(registers reg, int[] opperands) {
            return;
        }

        public void __PUSH_HL(registers reg, int[] opperands) {
            return;
        }

        public void __LD__C__A(registers reg, int[] opperands) {
            return;
        }

        public void __RST_20H(registers reg, int[] opperands) {
            return;
        }

        public void __AND_d8(registers reg, int[] opperands) {
            return;
        }

        public void __JP_HL_(registers reg, int[] opperands) {
            return;
        }

        public void __ADD_SP_r8(registers reg, int[] opperands) {
            return;
        }

        public void __XOR_d8(registers reg, int[] opperands) {
            return;
        }

        public void __LD_a16_A(registers reg, int[] opperands) {
            return;
        }

        public void __LDH_A_a8(registers reg, int[] opperands) {
            return;
        }

        public void __RST_28(registers reg, int[] opperands) {
            return;
        }

        public void __LD_A__C__(registers reg, int[] opperands) {
            return;
        }

        public void __POP_AF(registers reg, int[] opperands) {
            return;
        }

        public void __PUSH_AF(registers reg, int[] opperands) {
            return;
        }

        public void __DI(registers reg, int[] opperands) {
            return;
        }

        public void __RST_30H(registers reg, int[] opperands) {
            return;
        }

        public void __OR_d8(registers reg, int[] opperands) {
            return;
        }

        public void __LD_SP_HL(registers reg, int[] opperands) {
            return;
        }

        public void __LD_HL_SP_I_r8(registers reg, int[] opperands) {
            return;
        }

        public void __EI(registers reg, int[] opperands) {
            return;
        }

        public void __LD_A_a16(registers reg, int[] opperands) {
            return;
        }

        public void __RST_38H(registers reg, int[] opperands) {
            return;
        }

        public void __CP_d8(registers reg, int[] opperands) {
            return;
        }

        public void __RRCA(registers reg, int[] opperands) {
            return;
        }

        public void __RRA(registers reg, int[] opperands) {
            return;
        }

        public void __RLA(registers reg, int[] opperands) {
            return;
        }

        public void __CPL(registers reg, int[] opperands) {
            return;
        }

        public void __CCF(registers reg, int[] opperands) {
            return;
        }
    }

}
