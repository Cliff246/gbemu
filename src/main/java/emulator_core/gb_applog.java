package emulator_core;

import java.util.Vector;



public class gb_applog {

    private enum Logtype {
        Header,
        Datalog,

    }

    private class Log {
        private String lLeadString = "";
        private Log lParentLog;
        private Vector<Log> lSubLogs;
        private Logtype lType;

        public Log(String llog) {
            lLeadString = llog;
            lParentLog = baselog;
            lSubLogs = null;
            lType = Logtype.Datalog;
        }

        public Log(String vlog, Logtype Logtype) {
            lParentLog = baselog;
            lSubLogs = null;
            lLeadString = vlog;
            lType = Logtype;
        }

        public Log(String llog, Log parent) {
            lParentLog = parent;
            lSubLogs = null;
            lLeadString = llog;
            lType = Logtype.Datalog;
        }


        public void updateparent(Log parent) {
            if (!equals(baselog)) {
                if (parent == null || lParentLog == null) {
                    parent = baselog;
                } else
                    lParentLog = parent;
            }
        }

        public void updatelog(String log) {
            lLeadString = log;
        }

        public void addsub(Log log) {
            if (lType == Logtype.Datalog)
                lType = Logtype.Header;
            log.updateparent(this);
            if (lSubLogs == null)
                lSubLogs = new Vector<Log>();
            lSubLogs.add(log);
        }

        public void addsub(String log) {
            Log sub = new Log(log);
            if (lType == Logtype.Datalog)
                lType = Logtype.Header;
            sub.updateparent(this);
            if (lSubLogs == null)
                lSubLogs = new Vector<Log>();
            lSubLogs.add(sub);
        }

        public void removesub(Log log) {
            if (lType == Logtype.Datalog || lSubLogs == null)
                return;
            else {
                if (lSubLogs != null)
                    lSubLogs.remove(log);
            }
        }

        public void removesub(String log) {
            if (lType == Logtype.Datalog || lSubLogs == null)
                return;
            else if (lSubLogs != null) {
                for (int i = 0; i < lSubLogs.size(); i++) {
                    String isub = lSubLogs.elementAt(i).toString();
                    if (isub.toString() == log)
                        lSubLogs.removeElementAt(i);
                }
            }
        }

        public Log getsub(String log) {
            if (lType == Logtype.Datalog || lSubLogs == null)
                return null;
            else if (lSubLogs != null) {
                for (int i = 0; i < lSubLogs.size(); i++) {
                    String isub = lSubLogs.elementAt(i).toString();
                    if (isub == log)
                        return lSubLogs.get(i);
                }
            }
            return null;
        }

        public Logtype gettype() {
            return lType;
        }

        public Log[] getheaders() {
            if (lType == Logtype.Datalog || lSubLogs == null)
                return null;
            else if (lSubLogs != null) {
                Vector<Log> headers = new Vector<Log>();
                for (int i = 0; i < lSubLogs.size(); i++) {
                    Log isub = lSubLogs.elementAt(i);
                    if (isub.gettype() == Logtype.Header)
                        headers.add(isub);
                }
                return (Log[]) headers.toArray();
            } else
                return null;
        }

        public Log[] getdatalogs() {
            if (lType == Logtype.Datalog || lSubLogs == null)
                return null;
            else if (lSubLogs != null) {
                Vector<Log> dLogs = new Vector<Log>();
                for (int i = 0; i < lSubLogs.size(); i++) {
                    Log isub = lSubLogs.elementAt(i);
                    if (isub.gettype() == Logtype.Datalog)
                        dLogs.add(isub);
                }
                return (Log[]) dLogs.toArray();
            } else
                return null;
        }

        public String[] getlogsrecurse()
        {
            total.clear();
            if (lType == Logtype.Datalog || lSubLogs == null)
                return new String[]{lLeadString};
            else if (lSubLogs != null) {
                depthrecurse(this);
                return (String[])total.toArray();
            }
            else
                return null;
        }

        private Vector<String> total = new Vector<String>();
        private void depthrecurse(Log header)
        {
            Log[]dlogs = header.getdatalogs();
            for(Log diter: dlogs)
                total.add(diter.toString() + header.toString());
            Log[] hlogs = header.getheaders();
            for(Log hiter: hlogs)
                depthrecurse(hiter);

        }

        public void print() {
            System.out.println(sprint());
        }

        public String sprint(){
            String fmt = "child %s | parent %s";
            String sprint = String.format(fmt, lLeadString, lParentLog.toString()); 
            return sprint;
        }

        @Override
        public String toString() {
            return lLeadString;
        }
    }

    private Vector<Log> applog = new Vector<Log>();
    private Log baselog = new Log("baselog", Logtype.Header);

    public gb_applog(String start) {
        applog.add(baselog);

    }


    public void update_log(Log log, String nstring)
    {
        if(log != null && nstring != null)
        {
            for(Log iter: applog)
            {
                if(iter == log)
                    iter.updatelog(nstring);
            }

        }
        return;
    }

    public void insert_log(String log, int index) {
      
        if (index >= 0 && index < applog.size())
        {
            Log temp = new Log(log);
            applog.insertElementAt(temp, index);
        }
        else
            push_log(log);
    }

    public void remove_log(String log) {
        for (int i = 0; i < applog.size(); i++) {
            String str = applog.elementAt(i).toString();
            if (str == log)
                applog.removeElementAt(i);
        }
    }

    public void push_log(String log) {
        Log temp = new Log(log);
        applog.add(temp);
    }

    public String pop_log() {
        if (applog.size() > 0) {
            String pop = applog.lastElement().toString();
            applog.remove(applog.lastElement());
            return pop;
        } else
            throw new NullPointerException("there are no more elements in the log");
    }

    public boolean chk_log() {
        if (applog.size() >= 1)
            return true;
        else
            return false;
    }

    public void clear_log() {
        applog.clear();
    }

    public String peak_log(int index) {
        return applog.elementAt(index).toString();
    }

    public void push_header(String title) {
        Log header = new Log(title, Logtype.Header);
        applog.add(header);

    }

    public void print_log(Log log)
    {
        if(log != null)
            System.out.println(log.toString());
    }

    public void print_markedlog(Log log)
    {
        if(log != null)
            log.print();
    }

    public String getprint_log(Log log)
    {
        if(log != null)
            return log.sprint();
        return "";
    }

    public String getprint_markedlog(Log log)
    {
        if(log != null)
            return log.sprint();
        return "";
    }

    public String[] pop_header(String title) {
        
        Vector<String> buffer = new Vector<String>();
        for(int i = 0;i < applog.size();i++)
        {
            Log scroll = applog.elementAt(i);
            if(scroll.lType == Logtype.Header && scroll.toString() == title)
            {
                String[] search = scroll.getlogsrecurse();
                for(String iter: search)
                    buffer.add(iter);
            }

        }
        return (String[])buffer.toArray();

    }

    public Log get_header(String title) {
        for(int i = 0; i < applog.size();i++)
        {
            Log header = applog.elementAt(i);
            if(header.lType == Logtype.Header)
                return header;
        }
        return null;
    }

    public void addtoheader(String title, Log[] logs)
    {
        Log header = get_header(title);
        if(header != null)
        {
            for(Log iter: logs)
                header.addsub(iter);
        }
        else
            return;
    }   

    public void removefromheader(String title, Log[] logs)
    {
        Log header = get_header(title);
        if(header != null)
        {
            for(Log iter: logs)
                header.removesub(iter);
        }
        else
            return;
    }
    public void addtoheader(String title, String[] logs)
    {
        Log header = get_header(title);
        if(header != null)
        {
            for(String iter: logs)
            {
                Log temp = new Log( iter, header);
                header.addsub(temp);
            }
        }
        else
            return;
    }

    public void removefromheader(String title, String[] logs)
    {
        Log header = get_header(title);
        if(header != null)
        {
            for(String iter: logs)
                header.removesub(iter);
        }
        else
            return;
    }

    public Log getfromheader(String title, String log)
    {
        Log header = get_header(title);
        if(header != null)
        {
            Log[] subs = header.getdatalogs();
            for(Log iter: subs)
            {
                if(iter.toString() == log)
                    return iter;
            }
        }
        return null;
    }
}
