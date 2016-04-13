package pt.ulisboa.tecnico.cmu.ubibike.remote.rest;

import java.util.List;

import pt.ulisboa.tecnico.cmu.ubibike.domain.Station;
import retrofit2.Call;
import retrofit2.http.GET;


public interface StationServiceREST {

    @GET("station")
    Call<List<Station>> getStations();

}
