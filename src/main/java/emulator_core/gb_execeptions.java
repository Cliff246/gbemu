package emulator_core;

public class gb_execeptions {
    public static void  gb_exception(String fmt, Object [] args) throws Exception
    {
        String output = String.format(fmt, args);
        throw new Exception(output);
    }

    public static gb_applog applog = new gb_applog("errors");

    public static void gb_putlog(String log){
        applog.push_log(log);
    }

    public static String gb_poplog(){
        return applog.pop_log();
    }
}