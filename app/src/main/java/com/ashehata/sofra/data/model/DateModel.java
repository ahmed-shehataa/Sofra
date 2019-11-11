package com.ashehata.sofra.data.model;

public class DateModel {
    private String  dateTxt="" ;
    private String  day = "1" ;
    private String  month = "1" ;
    private String  year ="2019";

    public String getDateTxt() {
        return dateTxt;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public void setDateTxt(String dateTxt) {
        this.dateTxt = dateTxt;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
