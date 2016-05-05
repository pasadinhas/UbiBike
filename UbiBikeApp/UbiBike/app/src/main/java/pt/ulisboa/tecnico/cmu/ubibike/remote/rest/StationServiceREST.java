package pt.ulisboa.tecnico.cmu.ubibike.remote.rest;

import java.util.List;

import pt.ulisboa.tecnico.cmu.ubibike.domain.Station;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface StationServiceREST {

    String STATION_DETAIL_LOW = "low";

    @GET("station")
    Call<List<Station>> getStations(@Query("detail")String detail);

}
