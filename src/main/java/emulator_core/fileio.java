package emulator_core;

import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;

import java.util.Scanner;
import java.util.Vector;
import emulator_core.*;

public class fileio {

    private File myfile;
    private int myaccess = 0;
    public final int __readonly__ = 1;
    public final int __writeonly__ = 2;
    public final int __readwrite__ = 3;
    public final int __noaccess__ = 4;

    public fileio(String _mypath) throws IOException {
        myfile = new File(_mypath);
        if (myfile.canRead()) {
            myaccess = __readwrite__;
        } else {
            throw new IOException("myfile could not be read");
        }
    }

    public fileio(File _myfile) throws IOException {
        if (myfile.canRead() && myfile.canWrite()) {
            myaccess = __readwrite__;
            myfile = _myfile;
        } else
            throw new IOException("myfile could not be read");
    }

    public fileio(File _myfile, int _myaccess) throws IOException {
        if (myfile.exists()) {
            myfile = _myfile;
            myaccess = _myaccess;
        } else
            throw new IOException("myfile could not be read");
    }

    public int writefile(byte[][] output, int[] offset) throws IOException {
        if (myaccess == __noaccess__ || myaccess == __readonly__)
            return 1;

        FileOutputStream fout = new FileOutputStream(myfile);
        for (int i = 0; i < offset.length; i++) {
            byte[] tbuffer = output[i];
            int off = offset[i];
            fout.write(tbuffer, off, tbuffer.length);
        }
        fout.close();
        return 0;
    }

    public byte[][] readfile(int chunksize) throws IOException {
        if (myaccess == __noaccess__ || myaccess == __writeonly__)
            return null;

        Vector<Vector<Byte>> temp = new Vector<Vector<Byte>>();
        Scanner fin = new Scanner(myfile);

        while (fin.hasNextByte()) {
            Vector<Byte> chunk = new Vector<Byte>(chunksize);
            for (int i = 0; i < chunksize; i++) {
                if (fin.hasNextByte())
                    chunk.set(i, fin.nextByte());
                else
                    break;
            }
            temp.add(chunk);
        }
        byte[][] bytes = new byte[temp.size()][];
        for (int i = 0; i < temp.size(); i++) {
            byte[] btemp = new byte[temp.elementAt(i).size()];
            int index = 0;
            for (byte current : temp.elementAt(i))
                btemp[index++] = current;
            bytes[i] = btemp;
        }
        fin.close();
        return bytes;
    }

    public int fileaccess() {
        if (myfile != null) {
            return myaccess;
        }
        return -1;
    }
}