package emulator_core;

public class gb_handle {
    /*
     * gb = gameboy
     * gbp = gameboy pocket
     * gbc = gameboy colour
     * gbs = gameboy super
     */
    public static enum GAMEBOY_TYPE {
        GB, GBP, GBC, GBS
    }

    public static enum HANDLE_TYPE {
        DEBUG, RELEASE
    }

    public static final int __gb_hzclock__ = 1050000,
            __gbc_hzclock__ = 1050000 * 2;

    public GAMEBOY_TYPE gbtype;
    public HANDLE_TYPE handletype;

 
    public gb_handle()
    {



    }
}