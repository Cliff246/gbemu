package emulator_core;

public class gb_bus extends Thread {

    public gb_cpu gbcpu;
    public gb_cartridge gbcart;
    public gb_ppu gbppu;
    public gb_memory gbmem;
    public gb_sysio gbio;
    public gb_handle handle;
    public Thread thread;
    public String name;
    public boolean[] flags;

    public gb_bus(String _name, gb_handle _handle, boolean[] _flags) {
        name = _name;
        handle = _handle;
        flags = _flags;
    }

    public void start() {
        thread = currentThread();
        {
            if (flags == null) {
                flags = new boolean[] {
                        false, false
                };
            }
            gbcpu = new gb_cpu(thread, handle, this, null, flags);

        }

        {
            gbppu = new gb_ppu(thread, handle, this);

        }

        {
            vertex<Integer> ramrange = new vertex<Integer>(0xc000, 0xe000);

            gbmem = new gb_memory(thread, handle, this, ramrange);
        }

        {

            gbio = new gb_sysio(thread, handle, this);
        }

        gbcpu.start();
        gbcart.run();
    }

    public void run() {

        if (gbcpu != null && gbppu != null && gbmem != null) {

        } else {

        }
    }

    public void insert_gb_cart(gb_cartridge gb_cartridge) {
        gbcart = gb_cartridge;
    }

    public void remove_gb_cart(gb_cartridge gb_cartridge) {
        if (gb_cartridge == gbcart)
            gbcart = null;
    }

    public void bus_send(int address, int[] data) {
        if (address <= gb_bitfunctions.__u_word_max__ || address >= gb_bitfunctions.__u_word_min__) {
            int copy = address;
            address = gb_bitfunctions.chk_word(address);
            gb_execeptions.gb_putlog("address at (over/under)flowed: original = %d, new = %d", copy, address);
        }

        if (handle.gbtype == gb_handle.GAMEBOY_TYPE.GBC) {

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
        gbcpu.snoopbus(data);
    }

    public void sendto_cart(int[] data, int address) {
        if (gbcart == null)
            gb_execeptions.gb_exception("cart not inserted");
        else
            gbcart.snoopbus(data, address);
    }

    public void sendto_mem(int[] data, int address) {
        if (gbmem == null)
            gb_execeptions.gb_exception("memory not initalized");
        else
            gbmem.snoopbus(data, address);
    }

    public void sendto_sysio(int[] data, int address) {
        if (gbio == null)
            gb_execeptions.gb_exception("sysio not initalized");
        else
            gbio.snoopbus(data, address);
    }

    public void sendto_ppu(int[] data, int address) {
        if (gbppu == null)
            gb_execeptions.gb_exception("ppu not initalized");
        else
            gbppu.snoopbus(data, address);
    }

    public void sendto_cpu(int[] data, int address) {

    }
}