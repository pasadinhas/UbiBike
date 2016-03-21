package pt.ulisboa.tecnico.cmu.ubibike.rest;

/**
 * Created by André on 21-03-2016.
 */
public class RestUtil {

    public final static String UBIBIKE_COMMON_URI = "ubibike";

    public final static String UBIBIKE_GET_USER = UBIBIKE_COMMON_URI + "user/";

    public static String buildURI(String ip,String path){
        return "http://"+ip+":8080/"+path;
    }

}
