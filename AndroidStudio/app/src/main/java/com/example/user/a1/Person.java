package com.example.user.a1;
//package com.sample.postjson;

/**
 * Created by Administrator on 2016-06-07.
 */
public class Person {
    private String name;
    private String gender;
    private String age;
    private String username;
    private String pass;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String min;
    private String foodname;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getID() {
        return username;
    }

    public void setID(String username) { this.username = username; }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) { this.pass = pass; }

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

    public String getFood() {
        return foodname;
    }

    public void setFood(String foodname) {
        this.foodname = foodname;
    }


    @Override
    public String toString() {
        return "Person [name=" + name + ", username=" + username+ ", password=" + pass +", age=" + age + ", gender="
                + gender +  ", year=" + year + ", month=" + month + ", day=" + day + ", hour=" + hour + ", min=" + min +", FOODNAME="
                + foodname + "]";
    }
}