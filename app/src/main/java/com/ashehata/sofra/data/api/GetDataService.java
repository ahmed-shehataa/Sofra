package com.ashehata.sofra.data.api;


import com.ashehata.sofra.data.model.general.regions.Regions;
import com.ashehata.sofra.data.model.general.restaurants.Restaurants;
import com.ashehata.sofra.data.model.reataurant.GeneralResponse;
import com.ashehata.sofra.data.model.reataurant.Categories.Categories;
import com.ashehata.sofra.data.model.reataurant.foodItem.FoodItem;
import com.ashehata.sofra.data.model.reataurant.offer.Offer;
import com.ashehata.sofra.data.model.reataurant.order.Order;
import com.ashehata.sofra.data.model.reataurant.restaurantCycle.Profile.User;
import com.ashehata.sofra.data.model.reataurant.restaurantCycle.changeState.ChangeState;
import com.ashehata.sofra.data.model.reataurant.restaurantCycle.Profile.Profile;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface GetDataService {


    @POST("restaurant/login")
    @FormUrlEncoded
    Call<Profile> restaurantLogin(@Field("email") String email,
                                  @Field("password") String password);


    @GET("restaurant/my-categories")
    Call<Categories> getCategories(@Query("api_token") String apiToken);


    @POST("restaurant/new-category")
    @Multipart
    Call<Categories> addCategory(@Part MultipartBody.Part photo
            , @Part("name") RequestBody name
            , @Part("api_token") RequestBody apiToken);


    @POST("restaurant/delete-category")
    @FormUrlEncoded
    Call<GeneralResponse> deleteCategory(@Field("api_token") String apiToken,
                                         @Field("category_id") String categoryId);


    @POST("restaurant/update-category")
    @Multipart
    Call<Categories> updateCategory(@Part MultipartBody.Part photo
            , @Part("name") RequestBody name
            , @Part("api_token") RequestBody apiToken
            , @Part("category_id") RequestBody categoryId);


    @POST("restaurant/change-password")
    @FormUrlEncoded
    Call<GeneralResponse> changePassword(@Field("api_token") String apiToken,
                                         @Field("old_password") String oldPassword,
                                         @Field("password") String password,
                                         @Field("password_confirmation") String passwordConfirmation);


    @POST("restaurant/sign-up")
    @Multipart
    Call<GeneralResponse> register(@Part MultipartBody.Part photo,
                                   @Part("name") RequestBody name,
                                   @Part("email") RequestBody email,
                                   @Part("delivery_time") RequestBody delivery_time,
                                   @Part("password") RequestBody password,
                                   @Part("password_confirmation") RequestBody password_confirmation,
                                   @Part("phone") RequestBody phone,
                                   @Part("whatsapp") RequestBody whatsapp,
                                   @Part("region_id") RequestBody region_id,
                                   @Part("delivery_cost") RequestBody delivery_cost,
                                   @Part("minimum_charger") RequestBody minimum_charger);

    @GET("cities")
    Call<Regions> getCities();

    @GET("regions")
    Call<Regions> getRegion(@Query("city_id") int cityId);


    @GET("restaurant/my-items")
    Call<FoodItem> getFoodItem(@Query("api_token") String apiToken
            , @Query("category_id") String categoryId);


    @POST("restaurant/delete-item")
    @FormUrlEncoded
    Call<FoodItem> deleteFoodItem(@Field("api_token") String apiToken,
                                  @Field("item_id") String itemId);


    @POST("restaurant/new-item")
    @Multipart
    Call<FoodItem> addFoodItem(@Part MultipartBody.Part photo,
                               @Part("name") RequestBody name,
                               @Part("description") RequestBody description,
                               @Part("price") RequestBody price,
                               @Part("api_token") RequestBody apiToken,
                               @Part("offer_price") RequestBody offerPrice,
                               @Part("category_id") RequestBody categoryId);

    @POST("restaurant/update-item")
    @Multipart
    Call<FoodItem> updateFoodItem(@Part MultipartBody.Part photo,
                                  @Part("item_id") RequestBody itemId,
                                  @Part("name") RequestBody name,
                                  @Part("description") RequestBody description,
                                  @Part("price") RequestBody price,
                                  @Part("api_token") RequestBody apiToken,
                                  @Part("offer_price") RequestBody offerPrice,
                                  @Part("category_id") RequestBody categoryId);

    @POST("restaurant/change-state")
    @FormUrlEncoded
    Call<ChangeState> changeState(@Field("api_token") String apiToken,
                                  @Field("state") String state);


    @POST("restaurant/profile")
    @Multipart
    Call<Profile> editProfile(@Part MultipartBody.Part photo,
                              @Part("name") RequestBody name,
                              @Part("email") RequestBody email,
                              @Part("delivery_time") RequestBody delivery_time,
                              @Part("phone") RequestBody phone,
                              @Part("whatsapp") RequestBody whatsapp,
                              @Part("region_id") RequestBody region_id,
                              @Part("delivery_cost") RequestBody delivery_cost,
                              @Part("minimum_charger") RequestBody minimum_charger,
                              @Part("api_token") RequestBody apiToken,
                              @Part("availability") RequestBody availability);

    @GET("restaurant/my-offers")
    Call<Offer> getOffers(@Query("api_token") String apiToken , @Query("page") int page );


    @POST("restaurant/new-offer")
    @Multipart
    Call<Offer> addOffer(@Part MultipartBody.Part photo,
                         @Part("name") RequestBody name,
                         @Part("description") RequestBody description,
                         @Part("price") RequestBody price,
                         @Part("offer_price") RequestBody offer_price,
                         @Part("api_token") RequestBody apiToken,
                         @Part("starting_at") RequestBody starting_at,
                         @Part("ending_at") RequestBody ending_at);
    @POST("restaurant/delete-offer")
    @FormUrlEncoded
    Call<Offer> deleteOffer(@Field("api_token") String apiToken,
                                  @Field("offer_id") int itemId);

    @GET("restaurant/my-orders")
    Call<Order> getOrders(@Query("api_token") String apiToken ,
                          @Query("state") String state ,
                          @Query("page") int page );

    @GET("restaurants")
    Call<Restaurants> getRestaurants();


    @GET("items")
    Call<FoodItem> getRestaurantFoodList(
            @Query("restaurant_id") int restaurantId ,
                          @Query("category_id") int categoryId);

}
