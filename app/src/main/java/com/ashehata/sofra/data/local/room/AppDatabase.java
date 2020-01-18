package com.ashehata.sofra.data.local.room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.ashehata.sofra.data.model.reataurant.foodItem.FoodItemData;

@Database(entities = {FoodItemData.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    public abstract FoodItemDao userDao();


    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context,
                    AppDatabase.class, "my-database").build();
        }
        return INSTANCE;
    }
}