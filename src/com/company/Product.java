package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.*;

public class Product {

    int type; //type de produit
    int id; //identifiant produit
    int number_components; //nombre de composants
    int number_features; //nombre de fonctionnalités
    int state; //état du produit (-1 si le produit est en stock sinon il prend le numéro du client qui l'utilise)

    ArrayList<Component> com = new ArrayList<>();
    ArrayList<Feature> fea = new ArrayList<>();

    double[][] comfea = new double[number_features][number_components]; //implication d'un composant dans le fonctionnement d'une fonctionnalité

    double total_performance; //performance totale d'un produit
    double total_rem; //remanufacturabilité totale d'un produit

    public Product(){

    }

    public Product(int type, int id, int number_components, int number_features, int state, ArrayList<Component> com, ArrayList<Feature> fea, double[][] comfea, double total_performance, double total_rem){
        this.type =type;
        this.id = id;
        this.number_features = number_features;
        this.number_components = number_components;
        this.state= state;
        this.com = com;
        this.fea = fea;
        this.comfea = comfea;
        this.total_performance = total_performance;
        this.total_rem= total_rem;
    }

    public Product(Product other) {
        this.type = other.getType();
        this.id = other.getId();
        this.number_features = other.getNumber_features();
        this.number_components = other.getNumber_components();
        this.state= other.getState();
        this.com = other.getCom();
        this.fea = other.getFea();
        this.comfea = other.getComfea();
        this.total_performance = other.getTotal_performance();
        this.total_rem= other.getTotal_rem();
    }

    public Product clone() {
        Product newObj = new Product(this.getType(), this.getId(), this.getNumber_components(), this.getNumber_features(), this.getState(), this.getCom(), this.getFea(), this.getComfea(), this.getTotal_performance(), this.getTotal_rem());
        return newObj;
    }


    //Getters
    public int getId() {
        return id;
    }

    public ArrayList<Component> getCom() {
        return com;
    }

    public int getNumber_components() {
        return number_components;
    }

    public ArrayList<Feature> getFea() {
        return fea;
    }

    public double getTotal_performance() {
        return total_performance;
    }

    public double[][] getComfea() {
        return comfea;
    }

    public int getNumber_features() {
        return number_features;
    }

    public int getType() {
        return type;
    }

    public int getState() {
        return state;
    }

    public double getTotal_rem() {
        return total_rem;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setCom(ArrayList<Component> com) {
        this.com = com;
    }

    public void setComfea(double[][] comfea) {
        this.comfea = comfea;
    }

    public void setNumber_components(int number_components) {
        this.number_components = number_components;
    }

    public void setFea(ArrayList<Feature> fea) {
        this.fea = fea;
    }

    public void setNumber_features(int number_features) {
        this.number_features = number_features;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTotal_performance(double total_performance) {
        this.total_performance = total_performance;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setTotal_rem(double total_rem) {
        this.total_rem = total_rem;
    }

    //Calcul de la performance totale d'un produit
    public void calculperf(){
        double sum;
        double total = 0;
        for(int i = 0; i < number_features;  i++){
            sum = 0;
            for(int j = 0; j < number_components; j++){
                sum += comfea[i][j] * com.get(j).performance;
            }
            total += fea.get(i).importance * sum;
        }
        setTotal_performance(total);
    }

    //Génération aléatoirement de l'implication d'un composant dans le fonctionnement d'une fonctionnalité
    public void genfeatpartr(){ //aléatoirement

        double[][] rfeatpartp = new double [number_features][number_components];
        double rand1 = 0; //bernoulli
        double tot = 0;
        for(int i = 0; i < number_features; i++){
            for(int j = 0; j < number_components; j++){
                rand1 = Math.random(); //on tire aléatoirement un nombre compris entre 0 et 1
                if(rand1 < 0.35){ // Si le nombre tiré est inférieur 0.25 (Bernoulli biaisé) le composant n'aura aucun impact sur la fonctionnalité
                    rfeatpartp[i][j] = 0;
                }
                else{ // Sinon (nombre tiré > 0.25) on tire un nombre aléatoire compris entre 0 et 1 qui sera l'influence du composant sur la fonctionnalité (qui sera ensuite modifié)
                    rfeatpartp[i][j] = Math.random();
                }
            }
        }

        for(int i = 0; i < number_features; i++){ //On va calculer la somme des nombres aléatoires attribués sur chaque ligne
            tot = 0;
            for(int j = 0; j < number_components; j++){
                tot += rfeatpartp[i][j];
            }

            if(tot != 0){
                for(int j = 0; j < number_components; j++){ //On divise chaque élément non nul de la ligne par la somme des éléments de la ligne
                    rfeatpartp[i][j] =  rfeatpartp[i][j] / tot;
                }
            }
            else{
                for(int j = 0; j < number_components ; j++){
                    rfeatpartp[i][j] = Math.random();
                    tot += rfeatpartp[i][j];
                }
                for(int j = 0; j < number_components; j++){
                    rfeatpartp[i][j] = rfeatpartp[i][j] / tot;
                }
            }
        }
        setComfea(rfeatpartp);
    }

    //Génération du tableau dynamique des composants
    public void gencom (){
        ArrayList<Component> comp = new ArrayList<>();
        Component c;
        for(int i = 0; i < number_components; i++){
            c = new Component();
            c.setId(i);
            c.InitPerfo();
            c.InitRem();
            comp.add(c);
        }
        setCom(comp);
    }

    //Génération du tableau dynamique des fonctionnalités
    public void genfea(){

        double tot = 0;
        double[] impfeatp = new double[number_features];
        ArrayList<Feature> feap = new ArrayList<>();

        Feature f;
        for(int i = 0; i < number_features; i++){
            impfeatp[i] = Math.random();
            tot += impfeatp[i];
        }
        for(int i = 0; i < number_features; i++){
            impfeatp[i] = impfeatp[i] / tot;
        }

        for(int i = 0; i < number_features; i++){
            f = new Feature();
            f.setId(i);
            f.setImportance(impfeatp[i]);
            feap.add(f);
        }
        setFea(feap);
    }

    //Calcul de l'importance d'un composant
    public void genimppart(){
        double[] t  = new double[number_components];
        double c;

        for(int i = 0; i < number_components ; i++){
            c = 0;
            for(int j = 0; j < number_features; j++){
                c += comfea[j][i] * fea.get(j).importance;
            }
            t[i] = c;
            com.get(i).setImportance_c(t[i]);
        }
    }

    //calcul de la remanufacturabilité totale d'un produit
    public void cal_rem(){
        double t = 0;
        for(int i = 0; i < number_components; i++){
            t += com.get(i).rem;
        }
        t /= number_components;
        setTotal_rem(t);
    }

    public void display_comfea() {

        System.out.println("Produit relation  :");
        for (int i = 0; i < number_features; i++) {
            for (int j = 0; j < number_components; j++) {
                System.out.print(comfea[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public void changePerfocom(int i, double p){
        this.com.get(i).setPerformance(p);
    }

    public void changeRemcom(int i, double r){
        this.com.get(i).setRem(r);
    }

    public void display_com(){
        System.out.println("Liste des composant : ");
        for(int i = 0; i < number_components; i++){
            System.out.println("Composant " + com.get(i).id + " performance " + com.get(i).performance + " importance " + com.get(i).importance_c + " rem " + com.get(i).rem );
        }
    }

    public void display_fea(){
        System.out.println("Liste des fonctionnalité : ");
        for(int i = 0; i < number_features; i++){
            System.out.println("Feature " + fea.get(i).id +  " importance " + fea.get(i).importance);
        }
    }
}
