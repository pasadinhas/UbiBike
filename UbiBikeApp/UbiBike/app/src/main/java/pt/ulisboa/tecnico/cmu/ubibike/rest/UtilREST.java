package pt.ulisboa.tecnico.cmu.ubibike.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by André on 21-03-2016.
 */
public final class UtilREST {

    private final static String DATE_FORMAT_REST = "dd-MM-yyyy HH:mm:ss";

    /* Important Note: Use 10.0.2.2 when using emulator otherwise use PC IP
    *  Server IP: 51.255.175.95
    */
    private final static String WEB_SERVER_IP = "10.0.2.2";

    private final static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://" + WEB_SERVER_IP + ":8080/ubibike/")
            .addConverterFactory(GsonConverterFactory.create(
                    new GsonBuilder()
                            .setDateFormat(DATE_FORMAT_REST)
                            .create()))
            .build();

    public static Retrofit getRetrofit(){
       return retrofit;
    }

}
