package emulator_core;


import src.app.emulator_core.*;

public class gb_errorstate {

    static gb_handle handle;
    static boolean execute;
    static boolean error;
    static gb_applog exceptlogs = new gb_applog("error log");
    static gb_applog warninglogs = new gb_applog("error log");


    public gb_errorstate(gb_handle _handle){
        sethandle(_handle);
    }


    public static void sethandle(gb_handle _handle){
        handle = _handle;
    }


    public static void exception(Thread thread, String message, String ... extra) {
        String function = thread.getStackTrace()[0].getMethodName();
        String cat = String.format("exception at: %s | msg-> %s -", function, message);
        for (int i = 0; i < extra.length; i++)
            cat += String.format(" (%s)", extra[i]);
        exceptlogs.push_log(cat);
        error_stop(thread);
    }


    public static void warning(Thread thread, String message, String ... extra) {
        String function = thread.getStackTrace()[1].getMethodName();
        String cat = String.format("exception at: %s | msg-> %s -", function, message);
        for (int i = 0; i < extra.length; i++)
            cat += String.format(" (%s)", extra[i]);
        exceptlogs.push_log(cat);
        error_stop(thread);
    }




    public static void error_stop(Thread thread) {
        execute = false;
        error = true;
        error_present();
    }

    public static void error_restart(Thread thread){
        
        
        handle.handler_reset();
        execute = false;
        error = true;
        error_present();
    }

    public static void error_continue(Thread thread){
        error = false;
        execute = true;
    }

    public static void error_present(){

    }
}
