package emulator_core;

public class gb_components {

    protected gb_cpu cpu;
    protected gb_bus bus;
    protected gb_cartridge cartridge;
    protected gb_ppu ppu;
    protected gb_memory memory;
    protected gb_sysio sysio;
    protected boolean[] flags;
    protected gb_handle.GAMEBOY_TYPE version;
    protected gb_handle.HANDLE_TYPE htype;

    public gb_components()
    {
        gb_execeptions.gb_putlog("new component");
    }

    public void initsystem(boolean[] flags, gb_handle.GAMEBOY_TYPE version, gb_handle.HANDLE_TYPE htype)
    {

        setflags(flags);
        setversion(version);
        sethtype(htype);
        setbus(new gb_bus());
        setcartridge(new gb_cartridge());
        setcpu(new gb_cpu());
        setppu(new gb_ppu());
        setmemory(new gb_memory());
        setsysio(new gb_sysio());
    }


    public void setversion(gb_handle.GAMEBOY_TYPE set) {
        version = set;
    }

    public void sethtype(gb_handle.HANDLE_TYPE set) {
        htype = set;
    }

    public void setflags(boolean[] set) {
        flags = set;
    }

    public void setsysio(gb_sysio set) {
        sysio = set;
    }

    public void setcpu(gb_cpu set) {
        cpu = set;
    }

    public void setppu(gb_ppu set) {
        ppu = set;
    }

    public void setmemory(gb_memory set) {
        memory = set;
    }

    public void setbus(gb_bus set) {
        bus = set;
    }

    public void setcartridge(gb_cartridge set) {
        cartridge = set;
    }

    public gb_cpu getcpu() {
        return cpu;
    }

    public gb_bus getbus() {
        return bus;
    }

    public gb_cartridge getcartridge() {
        return cartridge;
    }

    public gb_memory getmemory() {
        return memory;
    }

    public gb_ppu getppu() {
        return ppu;
    }

    public gb_handle.GAMEBOY_TYPE getversion() {
        return version;
    }


    public gb_handle.HANDLE_TYPE gethtype() {
        return htype;
    }

    public boolean[] getflags() {
        return flags;
    }

    public gb_sysio getsysio() {
        return sysio;
    }
}
