package emulator_core;


import java.io.IOException;

public class gb_cartridge extends Thread
{

    public byte[]data;   
    private fileio io;
    public gb_cartridge(String path)
    {
        try{
            io = new fileio(path);
        }
        catch (IOException ioexc){
            gb_errorstate.exception(currentThread(), "%s -> ioexception",ioexc.getMessage());
            
        }
       


    }

    public void snoopbus()
    {
        
    }
}
