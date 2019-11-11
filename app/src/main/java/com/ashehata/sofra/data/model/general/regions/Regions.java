
package com.ashehata.sofra.data.model.general.regions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Regions {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private RegionsPagination data;

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

    public RegionsPagination getData() {
        return data;
    }

    public void setData(RegionsPagination data) {
        this.data = data;
    }

}
