package pt.ulisboa.tecnico.cmu.ubibike.remote.rest;

import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Bike;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Coordinates;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserServiceREST {

    String USER_DETAIL_MEDIUM = "medium";

    @FormUrlEncoded
    @POST("user/login/{username}")
    Call<User> login(@Path("username")String username,@Field("password")String password);

    @FormUrlEncoded
    @POST("user/{username}")
    Call<ResponseBody> createUsername(@Path("username")String username,@Field("password")String password);

    @GET("user/{username}/{detail}")
    Call<User> getUserWithDetailSpecified(@Path("username")String username,@Path("detail") String detail);

    @GET("users/{usernamePrefix}")
    Call<List<User>> getAllUsers(@Path("usernamePrefix") String usernamePrefix);

    @GET("user/{username}/trajectory/{date}")
    Call<Trajectory> getUserTrajectory(@Path("username") String username,
                                 @Path("date") long date);

    @POST("user/{username}/points/{points}")
    Call<ResponseBody> synchronizeUser(@Header(UtilREST.ACCEPT_HEADER) String acceptValue,
                                 @Header(UtilREST.CONTENT_TYPE_HEADER) String typeValue,
                                 @Path("username")String username,
                                 @Path("points")long points,
                                 @Body List<Trajectory> trajectories);


    @PUT("user/{username}/pick/bike/{id}")
    Call<ResponseBody> pickBike(@Path("username") String username,@Path("id")String id);

    @PUT("user/drop/bike/{id}/station/{station}")
    Call<ResponseBody> dropBike(@Header(UtilREST.ACCEPT_HEADER) String acceptValue,
                                   @Header(UtilREST.CONTENT_TYPE_HEADER) String typeValue,
                                   @Path("id")String id,
                                   @Path("station")String station,
                                   @Body Coordinates position);

    @PUT("user/{username}/book/bike/{id}")
    Call<Bike> bookBike(@Path("username") String username,@Path("id") String id);

}
