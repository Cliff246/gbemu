package emulator_core;

import java.security.InvalidParameterException;

import emulator_core.*;

public class gb_interface extends Thread
{

    enum busstat
    {
        read,
        write
    }

    private gb_bus gbbus;
    private int gbdata;
    private int gbbus_status;

    

    public gb_interface(gb_bus bus)
    {
        gbbus = bus;
    }

    private int get_busstatus()
    {
        return gbbus_status;
    }

    private void set_busstatus(int status)
    {
        gbbus_status = status;
    }

    private void send_overbus(int data)
    { 
        gbdata = data;
    }

    private int recive_overbus()
    {

        return gbdata;
    }

 
}
