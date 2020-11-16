package com.example.user.a1;
//package com.sample.postjson;

/**
 * Created by Administrator on 2016-06-07.
 */
public class Food {
    private String username;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String min;
    private String cal;
    private String tan;
    private String dan;
    private String gi;
    private String dang;
    private String na;
    private String foodname;


    public String getID() {
        return username;
    }

    public void setID(String username) { this.username = username; }

    public String getYear() { return year; }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) { this.hour = hour; }

    public String getMin() { return min; }

    public void setMin(String min) { this.min = min; }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) { this.foodname = foodname; }

    public String getCal() {
        return cal;
    }

    public void setCal(String cal) {
        this.cal = cal;
    }

    public String getTan() {
        return tan;
    }

    public void setTan(String tan) {
        this.tan = tan;
    }

    public String getDan() {
        return dan;
    }

    public void setDan(String dan) {
        this.dan = dan;
    }


    public String getGi() {
        return gi;
    }

    public void setGi(String gi) { this.gi = gi; }

    public String getDang() { return dang; }

    public void setDang(String dang) {
        this.dang = dang;
    }

    public String getNa() {
        return na;
    }

    public void setNa(String na) {
        this.na = na;
    }



    @Override
    public String toString() {
        return "Food [username=" + username + ", CAL=" + cal+ ", TAN=" + tan +", DAN=" + dan + ", GI="
                + gi +  ", DANG=" + dang +", NA="
                + na + "]";
    }
}