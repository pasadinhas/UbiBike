package pt.ulisboa.tecnico.cmu.ubibike.remote.rest;

import pt.ulisboa.tecnico.cmu.ubibike.domain.Bike;
import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BikeServiceREST {

    @PUT("bike/pick/up/{id}")
    Call<Bike> pickUpBike(@Path("id")String id);

    @PUT("bike/pick/off/{id}")
    Call<Bike> pickOffBike(@Path("id")String id);

    @PUT("bike/book/{id}")
    Call<Bike> bookABike(@Path("id")String id);

}
