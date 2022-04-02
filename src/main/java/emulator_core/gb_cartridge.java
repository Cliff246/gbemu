package emulator_core;

import java.io.IOException;

public class gb_cartridge extends Thread {

    public byte[] data;
    private fileio io;

    public gb_cartridge(Thread _thread, String path) throws Exception {
        try {
            io = new fileio(path);
        } catch (IOException ioexc) {
            throw new IOException("file could not be gathered ");
        }

    }

    public void snoopbus(int[] data, int address) {

    }
}
