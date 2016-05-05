package pt.ulisboa.tecnico.cmu.ubibike.remote.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by André on 21-03-2016.
 */
public final class UtilREST {

    public final static String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";

    public final static Gson GSON = new GsonBuilder().setDateFormat(DATE_FORMAT).create();

    public final static int HTTP_OK = 200;

    public final static int HTTP_CONFLICT = 409;

    public final static String ACCEPT_HEADER = "Accept";

    public final static String CONTENT_TYPE_HEADER = "Content-Type";

    public final static String CONTENT_HEADER = "application/json";

    /* Important Note: Use 10.0.2.2 when using emulator otherwise use PC IP
    *  Server IP: 51.255.175.95 */
    private final static String WEB_SERVER_IP = "10.0.2.2";

    private final static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://" + WEB_SERVER_IP + ":8080/ubibike/")
            .addConverterFactory(GsonConverterFactory.create(GSON)).build();

    public static Retrofit getRetrofit(){
       return retrofit;
    }

}
