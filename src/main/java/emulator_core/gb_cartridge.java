package emulator_core;


import java.io.IOException;
import emulator_core.*;

public class gb_cartridge extends Thread
{

    public byte[]data;   
    private gb_file_input io;
    public gb_cartridge(String path)
    {
        try{
            io = new gb_file_input(path);
        }
        catch (IOException ioexc){
            gb_errorstate.exception(currentThread(), "%s -> ioexception",ioexc.getMessage());
            
        }
       


    }

    
}
