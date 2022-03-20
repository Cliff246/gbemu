package emulator_core;

import java.io.File;
import java.io.IOException;

import emulator_core.*;

public class gb_file_input{

    private File gb_file;

    public gb_file_input(String gb_path) throws IOException
    {
        File temp = new File(gb_path);
        if(temp.canRead()){
            gb_file = temp;
        }       
        else{
           throw new IOException("gb_file could not be read");
        } 
    }

    public gb_file_input(File gb_file) throws IOException
    {
        if(gb_file.canRead())
        {

        }
        else
           throw new IOException("gb_file could not be read");
    }
}