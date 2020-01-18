package com.ashehata.sofra.data.local.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.ashehata.sofra.data.model.reataurant.foodItem.FoodItemData;
import java.util.List;

@Dao
public interface  FoodItemDao {

    @Query("SELECT * FROM FoodItemData")
    LiveData<List<FoodItemData>> getAllFoodItems();

    @Query("SELECT * FROM FoodItemData  WHERE _id = :userId")
    FoodItemData getFoodItem(int userId);

    @Insert
    void insertFoodItem(FoodItemData foodItemData);

    @Query("UPDATE FoodItemData SET quantity = :newQuantity WHERE _id = :userId ")
    void updateFoodItemQuantity(int userId ,int newQuantity);

    /*
    @Query("DELETE FROM FoodItemData WHERE _id = :userId")
    void deleteFoodItem(int userId);
     */

    @Delete
    void deleteFoodItem(FoodItemData foodItemData);

    @Query("DELETE FROM FoodItemData ")
    void deleteAllFoodItem();

}