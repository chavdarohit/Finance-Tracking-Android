package com.example.finance.Models;

public class ModelTask {

    private String date, day,month,title,desc,status;

    ModelTask()
    {

    }


    public ModelTask(String day, String date,String mon, String title,String desc,String status) {
        this.day=day;
        this.date=date;
        this.month=mon;
        this.title=title;
        this.desc=desc;
        this.status=status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
