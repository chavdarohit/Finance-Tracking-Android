package com.example.finance.Models;


public class ModelBank {
    private String accno, bname,mbal;
    private int banklogo;


    ModelBank()
    {

    }

    public ModelBank(String accno, String bname,String mbal, int banklogo) {
        this.accno = accno;
        this.bname = bname;
        this.mbal = mbal;
        this.banklogo = banklogo;
    }

    public String getAccno() {
        return accno;
    }

    public void setAccno(String accno) {
        this.accno = accno;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getMbal() {
        return mbal;
    }

    public void setMbal(String mbal) {
        this.mbal = mbal;
    }

    public int getBanklogo() {
        return banklogo;
    }

    public void setBanklogo(int banklogo) {
        this.banklogo = banklogo;
    }
}

