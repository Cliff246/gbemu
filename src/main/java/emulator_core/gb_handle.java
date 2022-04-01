package emulator_core;




public class gb_handle
{
    /*gb = gameboy
    gbp = gameboy pocket
    gbc = gameboy colour
    gbs = gameboy super*/
    public static enum GAMEBOY_TYPE
    {
        GB, GBP, GBC, GBS
    }

    public static enum HANDLE_TYPE
    {
        DEBUG, RELEASE
    }

    public static final int
        __gb_hzclock__ = 1050000,
        __gbc_hzclock__ = 1050000 * 2;
    

    
    public Thread thread;
    public String name; 
    public GAMEBOY_TYPE gbtype;
    public HANDLE_TYPE handletype;    
    public void handler_reset()
    {

    }




    public gb_handle(GAMEBOY_TYPE _gbtype)
    {
        handletype = HANDLE_TYPE.RELEASE;
        gbtype = _gbtype;
    }

    public gb_handle(gb_cartridge cartridge, GAMEBOY_TYPE _gbtype)
    {
        handletype = HANDLE_TYPE.RELEASE;
        gbtype = _gbtype;
    }

    public gb_handle(HANDLE_TYPE type)
    {
        gbtype = GAMEBOY_TYPE.GB;
        handletype = HANDLE_TYPE.DEBUG;
        try {
            create_bus_thread("cputhread");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void create_bus_thread(String _name) throws Exception
    {

        if(thread == null){
        
            name = _name;            
            thread = new gb_bus(_name, this);
            thread.start();
        }
        else
            gb_execeptions.gb_exception("could not start thread", "");
    }




}