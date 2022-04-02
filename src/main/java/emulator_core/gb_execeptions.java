package emulator_core;

public class gb_execeptions {
    public static void gb_exception(String fmt, Object... args) throws RuntimeException {
        String output = String.format(fmt, args);
        applog.push_log(output);
        while (applog.chk_log() == true)
            System.err.println(applog.pop_log());

        throw new RuntimeException(output);
    }

    public static gb_applog applog = new gb_applog("errors");

    public static void gb_putlog(String log) {
        applog.push_log(log);
    }

    public static void gb_putlog(String fmt, Object... args) {
        String log = String.format(fmt, args);
        applog.push_log(log);
    }

    public static String gb_poplog() {
        return applog.pop_log();
    }

    public static void gb_printlogs()
    {
        while (applog.chk_log() == true)
            System.err.println(applog.pop_log());
    }
}