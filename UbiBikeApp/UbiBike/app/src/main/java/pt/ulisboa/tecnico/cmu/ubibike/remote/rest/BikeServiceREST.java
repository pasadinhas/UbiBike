package pt.ulisboa.tecnico.cmu.ubibike.remote.rest;

import okhttp3.ResponseBody;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Bike;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Coordinates;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BikeServiceREST {

    @PUT("bike/pick/up/{id}")
    Call<ResponseBody> pickUpBike(@Path("id")String id);

    @PUT("bike/pick/off/{id}/station/{station}")
    Call<ResponseBody> pickOffBike(@Header(UtilREST.ACCEPT_HEADER) String acceptValue,
                                   @Header(UtilREST.CONTENT_TYPE_HEADER) String typeValue,
                                   @Path("id")String id,
                                   @Path("station")String station,
                                   @Body Coordinates position);

    @PUT("bike/book/{id}")
    Call<ResponseBody> bookABike(@Path("id")String id);

}
