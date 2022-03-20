package src.app.emulator_core;

import java.lang.management.ThreadInfo;

import src.app.emulator_core.*;

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

    public static final int
        gb_hzclock = 1050000,
        gbc_hzclock = 1050000 * 2;
    

    
    public Thread thread;
    public String name; 
    public GAMEBOY_TYPE gbtype;
    
    public void handler_reset()
    {

    }

    public gb_handle(GAMEBOY_TYPE _gbtype)
    {
        gbtype = _gbtype;
    }

    public gb_handle(gb_cartridge cartridge, GAMEBOY_TYPE _gbtype)
    {
        gbtype = _gbtype;
    }

    public void create_bus_thread(String _name)
    {

        if(thread == null){
        
            name = _name;            
            thread = new gb_bus(_name, this);
            thread.start();
        }
        else
            gb_errorstate.exception(this.thread,"thread could not be started", _name);
    }




}