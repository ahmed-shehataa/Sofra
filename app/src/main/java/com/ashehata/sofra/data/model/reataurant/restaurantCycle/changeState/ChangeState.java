
package com.ashehata.sofra.data.model.reataurant.restaurantCycle.changeState;

import com.ashehata.sofra.data.model.reataurant.restaurantCycle.Profile.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangeState {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private User data;

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

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

}
