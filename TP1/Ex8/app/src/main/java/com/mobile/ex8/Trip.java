package com.mobile.ex8;


public class Trip {

    private String start;
    private String finish;
    private String hStart;
    private String hFinish;
    private String prix;



    public Trip(String start, String finish, String hStart, String hFinish, String prix){

        this.start = start;
        this.finish = finish;
        this.hStart = hStart;
        this.hFinish = hFinish;
        this.prix = prix;}



    public String getStart(){return this.start;}
    public void setStart(String start) {this.start = start;}


    public String getFinish() {return finish;}
    public void setFinish(String finish) {this.finish = finish;}

    public String getPrix() {return prix;}
    public void setPrix(String prix) {this.prix = prix;}

    public String gethStart(){return hStart;}
    public void sethStart(String hStart) {this.hStart = hStart;}

    public String gethFinish(){return hFinish;}
    public void sethFinish(String hFinish) {this.hFinish = hFinish;}




}
