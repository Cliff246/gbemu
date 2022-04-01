package emulator_core;

public class gb_sysio extends Thread
{
    private gb_handle handle;
    private gb_bus bus;

    public gb_sysio(Thread _thread, gb_handle _handle, gb_bus _bus)
    {
        handle = _handle;
        bus = _bus;
    }

    public void snoopbus(int[]data, int address)
    {

    }
}
