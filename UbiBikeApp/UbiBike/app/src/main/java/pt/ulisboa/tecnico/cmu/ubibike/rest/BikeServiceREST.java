package pt.ulisboa.tecnico.cmu.ubibike.rest;

import pt.ulisboa.tecnico.cmu.ubibike.domain.Bike;
import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by André on 21-03-2016.
 */
public interface BikeServiceREST {

    @PUT("bike/pick/up/{id}")
    Call<String> pickUpBike(@Path("id")int id);

    @PUT("bike/pick/off/{id}")
    Call<String> pickOffBike(@Path("id")int id);

    @PUT("bike/book/{id}")
    Call<String> bookABike(@Path("id")int id);

}
