package net.jspiner.crowd.api;

import net.jspiner.crowd.model.Company;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("/companys")
    Single<ArrayList<Company>> getCompanys();

    @POST("/users")
    Completable createUser(
            @Body HashMap<String, String> body
    );

}
