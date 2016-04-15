package pt.ulisboa.tecnico.cmu.ubibike.remote.rest;

import java.util.List;

import okhttp3.ResponseBody;
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

public interface UserServiceREST {

    @FormUrlEncoded
    @POST("user/login/{username}")
    Call<User> login(@Path("username")String username,@Field("password")String password);

    @FormUrlEncoded
    @POST("user/{username}")
    Call<ResponseBody> createUsername(@Path("username")String username,@Field("password")String password);

    @PUT("user/{username}/points/{points}")
    Call<ResponseBody> updateUserPoints(@Path("username")String username,@Path("points") long points);

    @GET("user/{username}")
    Call<User> getUser(@Path("username")String username);

    @GET("users/{usernamePrefix}")
    Call<List<String>> getUsernames(@Path("usernamePrefix")String usernamePrefix);

    @POST("user/{username}/trajectory")
    Call<ResponseBody> addTrajectory(@Header(UtilREST.ACCEPT_HEADER) String acceptValue,
                             @Header(UtilREST.CONTENT_TYPE_HEADER) String typeValue,
                             @Path("username")String username,
                             @Body Trajectory trajectory);

}
