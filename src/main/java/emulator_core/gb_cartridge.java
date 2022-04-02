package emulator_core;

import java.io.IOException;

public class gb_cartridge extends gb_components {

    public byte[] data;
    private fileio io;

    public gb_cartridge( String path) throws Exception {
        try {
            io = new fileio(path);
        } catch (IOException ioexc) {
            throw new IOException("file could not be gathered ");
        }
    }

    public void snoopbus(int[] data, int address) {

    }
}
