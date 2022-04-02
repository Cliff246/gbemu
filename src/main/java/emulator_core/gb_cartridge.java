package emulator_core;

import java.io.IOException;

public class gb_cartridge extends gb_components {

    public byte[] data;
    private String path;
    private fileio io;

    public gb_cartridge(String _path) {
        path = _path;
        iostart();
    }

    public gb_cartridge(){
        path = "";
        iostart();
    }

    private void iostart()
    {
        try {
            io = new fileio(path);
        } catch (IOException ioexc) {
            gb_execeptions.gb_exception("io exception");
        }
        catch (Exception exc){
            io = null;
            path = null;
        }
    }

    public boolean ready()
    {
        if(io == null)
            return false;
        else
            return true;
    }

    public void start(){
        iostart();
    }

    public void setpath(String _path){
        if(path == null)
            path = _path;
    }

    public String getpath(){
        return path;
    }

    public void snoopbus(int[] data, int address) {

    }
}
