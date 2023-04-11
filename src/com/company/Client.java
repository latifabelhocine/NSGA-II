package com.company;

import java.util.ArrayList;

public class Client {

    int id;  //numero qui représente le client
    int profile; //profil du client entre 1 et 10 (concernant la fréquence d'utilisation) (pas sûr de l'utiliser)

    //Getters
    public int getId() {
        return id;
    }

    /*public double getFrequency() {
        return frequency;
    }*/

    public int getProfile() {
        return profile;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }


    public void setProfile(int profile) {
        this.profile = profile;
    }


}
