package pt.ulisboa.tecnico.cmu.ubibike.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by André on 21-03-2016.
 */
public final class UtilREST {

    private final static String DATE_FORMAT_REST = "dd-MM-yyyy";

    /* Important Note: Use 10.0.2.2 when using emulator otherwise use PC IP */
    private final static String WEB_SERVER_IP = "192.168.1.8";

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

    /*  (Sample Code) HOW MAKE THE REST CALLS

    ...ServiceREST service = UtilREST.getRetrofit().create(...ServiceREST.class);
    Call<ObjectCallReturn> call = service.specificCall(..arguments..);
    call.enqueue(new Callback<ObjectCallReturn>() {
            @Override
            public void onResponse(Call<ObjectCallReturn> call, Response<ObjectCallReturn> response) {

                //NOTE: This code should be processed by UI thread.
                ObjectCallReturn o = response.body();
                //Here is the code when the call was successful i.e: fill
                //UI elements with information from ObjetCallReturn.
                // BUT
            }
            @Overr!!!!: The call was successful but server could answer with an error
                 //(see response.code())
ide
            public void onFailure(Call<ObjectCallReturn> call, Throwable t) {

                //NOTE: This code should be processed by UI thread.
                //NOTE: Here there was a major problem for example: host unreachable, no internet etc

            }
        });

        NOTE!!!!!! call.enqueue can be called by UI thread because it launches a worker to make the request.

     */

}
