package pt.ulisboa.tecnico.cmu.ubibike.rest;

import pt.ulisboa.tecnico.cmu.ubibike.domain.Bike;
import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by André on 21-03-2016.
 */
public interface BikeRestCalls {

    @PUT("ubibike/bike/pick/up/{id}")
    Call<Bike> pickUpBike(@Path("id")int id);

}
