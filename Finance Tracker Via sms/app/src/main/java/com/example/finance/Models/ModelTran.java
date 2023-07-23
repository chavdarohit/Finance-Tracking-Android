package com.example.finance.Models;

public class ModelTran {
    private String type, date,amt;
    private String BankImage;

    ModelTran()
    {

    }

    public ModelTran(String type, String date,String amt, int bankimage) {
        this.type = type;
        this.date = date;
        this.amt = amt;
        this.BankImage = BankImage;
    }

    public String getBankImage() {
        return BankImage;
    }

    public void setBankImage(String BankImage) {
        this.BankImage = BankImage;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getAmt() {
        return amt;
    }
}

