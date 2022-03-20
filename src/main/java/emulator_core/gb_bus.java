package emulator_core;


import src.app.emulator_core.*;

public class gb_bus extends Thread{

    private gb_cpu gbcpu;
    private gb_cartridge gbcart;
    private gb_ppu gbppu;
    private gb_memory gbmem;
    private gb_handle handle;
    private Thread thread;
    private String name;

    public gb_bus(String _name, gb_handle _handle){
        name = _name;
        handle = _handle;
    }

    public void start()
    {    
        thread = currentThread();
        gbcpu = new gb_cpu(thread, handle, this);
        gbppu = new gb_ppu(handle, this);
        gbmem = new gb_memory(handle, this);
    }

    public void run(){
        
        

    

        if(gbcpu != null && gbppu != null && gbmem != null)
        {

        }
        else
        {
            
        }
    }

    public void insert_gb_cart(gb_cartridge gb_cartridge) {

    }

    public void remove_gb_cart(gb_cartridge gb_cartridge) {

    }

    private int data;
    
    public int pull_off_bus(){
        return data;
    }

    public void push_onto_bus(int _data){
        data = _data;
    }
    

}