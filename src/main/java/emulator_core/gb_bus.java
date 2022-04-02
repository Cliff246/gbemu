package emulator_core;

public class gb_bus extends gb_components{


    

        

    public void insert_gb_cart(gb_cartridge gb_cartridge) {
        cartridge = null;
    }

    public void remove_gb_cart(gb_cartridge gb_cartridge) {
        if (gb_cartridge == cartridge)
            cartridge = null;
    }

    public void bus_send(int address, int[] data) {
        if (address <= gb_bitfunctions.__u_word_max__ || address >= gb_bitfunctions.__u_word_min__) {
            int copy = address;
            address = gb_bitfunctions.chk_word(address);
            gb_execeptions.gb_putlog("address at (over/under)flowed: original = %d, new = %d", copy, address);
        }

        if (version == gb_handle.GAMEBOY_TYPE.GBC) {

        } else {
            if (address >= 0x0000 && address < 0x4000)
                sendto_cart(data, address);
            else if (address >= 0x4000 && address < 0x8000)
                sendto_cart(data, address);
            else if (address >= 0x8000 && address < 0xa000)
                sendto_ppu(data, address);
            else if (address >= 0xa000 && address < 0xc000)
                sendto_cart(data, address);
            else if (address >= 0xc000 && address < 0xe000)
                sendto_mem(data, address);
            else if (address >= 0xe000 && address < 0xfe00)
                sendto_mem(data, address);
            else if (address >= 0xfe00 && address < 0xfea0)
                sendto_ppu(data, address);
            else if (address >= 0xfea0 && address < 0xff00)
                gb_execeptions.gb_exception("this address should not be used %d", address);
            else if (address >= 0xff00 && address < 0xff4c)
                sendto_sysio(data, address);
            else if (address >= 0xff4c && address < 0xff8c)
                gb_execeptions.gb_exception("this address should not be used %d", address);
            else if (address >= 0xff8c && address < 0xff80)
                sendto_mem(data, address);
            else if (address >= 0xff80 && address < 0xffff)
                sendto_mem(data, address);

        }

    }

    public void bus_recieve(int[] data) {
        cpu.gb_snoopbus(data);
    }

    public void sendto_cart(int[] data, int address) {
        if (cartridge == null)
            gb_execeptions.gb_exception("cart not inserted");
        else
            cartridge.snoopbus(data, address);
    }

    public void sendto_mem(int[] data, int address) {
        if (memory == null)
            gb_execeptions.gb_exception("memory not initalized");
        else
            memory.snoopbus(data, address);
    }

    public void sendto_sysio(int[] data, int address) {
        if (sysio == null)
            gb_execeptions.gb_exception("sysio not initalized");
        else
            sysio.snoopbus(data, address);
    }

    public void sendto_ppu(int[] data, int address) {
        if (ppu == null)
            gb_execeptions.gb_exception("ppu not initalized");
        else
            ppu.snoopbus(data, address);
    }

    public void sendto_cpu(int[] data, int address) {

    }
}