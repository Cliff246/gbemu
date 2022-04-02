package emulator_core;

public class gb_components extends Thread {

    protected gb_cpu cpu;
    protected gb_bus bus;
    protected gb_cartridge cartridge;
    protected gb_ppu ppu;
    protected gb_memory memory;
    protected gb_sysio sysio;
    protected boolean[] flags;
    protected gb_handle.GAMEBOY_TYPE version;
    protected gb_handle.HANDLE_TYPE htype;
    protected static gb_execeptions excpt;

    public void run() {

    }

    public void threadlog()
    {
        Thread ref = currentThread();
        if(ref != null)
        {
            String threadname = ref.getName();
            long id = ref.getId();
            boolean alive = ref.isAlive();
            boolean daemon = ref.isDaemon();
            int priority = ref.getPriority();

            excpt.gb_putlog("thread name -> %s", threadname);
            excpt.gb_putlog("thread id -> %d", id);
            excpt.gb_putlog("thread is alive -> %s", Boolean.toString(alive));
            excpt.gb_putlog("thread is daemon -> %s", Boolean.toString(daemon));
            excpt.gb_putlog("thread priority -> %d", priority);
            
        }
        
        
    }

    public void delay(long milisec, int nanosec) {
        
        try {
            sleep(milisec, nanosec);
        } catch (InterruptedException ex) {
            gb_execeptions.gb_putlog("delay failed");
            ex.printStackTrace();
        }
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

    public gb_handle.GAMEBOY_TYPE setversion() {
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
