package com.company;

public class Feature {
    int id; //variable qui représente le numéro identifiant la fonctionnalité
    double importance; //variable qui représente l'importance de la fonctionnalité

    //Getters
    public double getImportance() {
        return importance;
    }

    public int getId() {
        return id;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setImportance(double importance) {
        this.importance = importance;
    }
}
