package net.jspiner.crowd.api;

import net.jspiner.crowd.model.Company;
import net.jspiner.crowd.model.Review;
import net.jspiner.crowd.model.User;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/companys")
    Single<ArrayList<Company>> getCompanys();

    @GET("/companys/{companyId}/users/{phoneNumber}")
    Single<Company> getCompanyDetail(
            @Path("companyId") int companyId,
            @Path("phoneNumber") String phoneNumber
    );

    @GET("/companys/{companyId}/users/{phoneNumber}/finds/friends")
    Single<ArrayList<User>> getCompanyFriends(
            @Path("companyId") int companyId,
            @Path("phoneNumber") String phoneNumber
    );

    @POST("/users")
    Completable createUser(
            @Body HashMap<String, String> body
    );

}
