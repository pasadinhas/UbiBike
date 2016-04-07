package pt.ulisboa.tecnico.cmu.ubibike.files;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by André on 07-04-2016.
 */
public abstract class UtilFile {

    public static final String USER_FILE = "User";

    public static void writeToFile(Context c,Object o,String fileName){
        try {
            FileOutputStream fos = c.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object readFromFile(Context c,String fileName) {
        try{
            FileInputStream fis = c.openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fis);
            Object o =  is.readObject();
            is.close();
            fis.close();
            return o;
        }catch(Exception e){
            e.printStackTrace();;
        }
        return null;
    }

}
