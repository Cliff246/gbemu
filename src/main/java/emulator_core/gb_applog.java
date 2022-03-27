package emulator_core;

import java.util.Vector;



public class gb_applog {

    private Vector<String> applog = new Vector<String>();

    public gb_applog(String start) {
        push_log(start);
    }

    public void push_log(String log) {
        applog.add(log);
    }

    public String pop_log() {
        if (applog.size() > 0) {
            String pop = applog.lastElement();
            applog.remove(applog.lastElement());
            return pop;
        } else
            throw new NullPointerException("there are no more elements in the log");
    }

    public boolean chk_log()
    {
        if(applog.size() >= 1)
            return true;
        else 
            return false;
    }

    public void clear_log() {
        applog.clear();
    }

    public String peak_log(int index) {
        return applog.lastElement();
    }
}
