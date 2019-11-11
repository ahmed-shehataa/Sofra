
package com.ashehata.sofra.data.model.reataurant.foodItem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FoodItem {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private FoodItemPagination data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public FoodItemPagination getData() {
        return data;
    }

    public void setData(FoodItemPagination data) {
        this.data = data;
    }

}
