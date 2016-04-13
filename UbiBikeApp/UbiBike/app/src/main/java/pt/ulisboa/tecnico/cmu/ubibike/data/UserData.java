package pt.ulisboa.tecnico.cmu.ubibike.data;

import android.content.Context;

import pt.ulisboa.tecnico.cmu.ubibike.data.files.UtilFile;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;

public abstract class UserData {

    private static User user = null;

    public synchronized static User getUserData(Context c){
        if(user == null)
            user = (User) UtilFile.readFromFile(c,UtilFile.USER_FILE);
        return user;
    }

    public synchronized static void setUserData(Context c,User usr){
        if(usr != null)
            user = usr;
    }

    public synchronized static void saveUserData(Context c){
        if(user != null)
            UtilFile.writeToFile(c,user,UtilFile.USER_FILE);
    }

    public synchronized static void removeUserData(Context c){
        UtilFile.deleteFile(c,UtilFile.USER_FILE);
    }

}
