package com.company;

public class Component {
    int id; //variable qui représente le numéro identifiant du composant
    double performance; //variable qui représente la performance d'un composant
    double importance_c; //variable qui va permettre de calculer l'importance d'un composant
    double rem; //remanufacturabilité d'un produit

    //Getters
    public int getId() {
        return id;
    }

    public double getPerformance() {
        return performance;
    }

    public double getImportance_c() {
        return importance_c;
    }

    public double getRem() {
        return rem;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPerformance(double performance) {
        this.performance = performance;
    }

    public void setImportance_c(double importance_c) {
        this.importance_c = importance_c;
    }

    public void setRem(double rem) {
        this.rem = rem;
    }

    //Initialisation de la performance du composant à 1 (100%)
    public void InitPerfo(){
        setPerformance(1.);
    }

    //Initialisation de la remanufacturabilité
    public void InitRem(){ setRem(1.);}


}
