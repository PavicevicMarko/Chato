package com.example.chato.User;

public class Uporabnik {

    private String ime, fonska, uid;

    public Uporabnik ( String uid, String ime, String fonska){
        this.uid = uid;
        this.ime = ime;
        this.fonska = fonska;

    }

    public String getIme() {
        return ime;
    }

    public String getFonska() {
        return fonska;
    }

    public String getUid() {
        return uid;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }
}
