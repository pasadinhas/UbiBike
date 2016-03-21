package pt.ulisboa.tecnico.cmu.ubibike.rest;

import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by André on 21-03-2016.
 */
public interface UserServiceREST {

    @FormUrlEncoded
    @POST("user/{username}")
    Call<User> createUsername(@Path("username")String username,@Field("password")String password);

    @PUT("user/{username}/points/{points}")
    Call<User> updateUserPoints(@Path("username")String username,@Path("points") int points);

    @GET("user/{username}")
    Call<User> getUser(@Path("username")String username);

}
