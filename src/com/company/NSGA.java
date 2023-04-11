package com.company;
import com.sun.source.tree.NewArrayTree;

import java.awt.image.AreaAveragingScaleFilter;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Scanner;


public class NSGA {

    /*                             Paramètres de la population                            */
    int popsize;
    double mutationprob; //Probabilité de mutation
    int numiter; //nombre d'itérations par algorithm

    ArrayList<Individu> population = new ArrayList<>(); //cette variable va contenir tous les individus généré
    ArrayList<Individu> initial_pop = new ArrayList<>(); //cette variable va contenir la population initiale
    ArrayList<Individu> parent_pop = new ArrayList<>(); //cette variable va contenir la population en cours des parents
    ArrayList<Individu> children_pop = new ArrayList<>(); //cette variable va contenir la population en cours des enfants
    ArrayList<ArrayList<Integer>> poplists = new ArrayList<>(); //cette variable va contenir les indices des éléments de la population à chaque itération de NSGA II
    ArrayList<ArrayList <ArrayList<Integer>>> fronts = new ArrayList<>(); //pour conserver tous les fronts des populations parents
    ArrayList<ArrayList <ArrayList<Integer>>> fronts_c =  new ArrayList<>(); //pour conserver l'indice des solutions de la population contenant les enfants après le tri en fronts
    ArrayList<ArrayList<Double>> fitness = new ArrayList<>(); //Pour conserver les valeurs des fonctions objectifs
    ArrayList<ArrayList<Integer>> fitness_r = new ArrayList<>(); //pour calculer le fitness à partir du front
    ArrayList<Double> crossproba = new ArrayList<>(); //Variable pour accorder une probabilité de crossover de chaque individu



    //Getters

    public ArrayList<ArrayList<ArrayList<Integer>>> getFronts() {
        return fronts;
    }

    public ArrayList<ArrayList<Double>> getFitness() {
        return fitness;
    }

    public ArrayList<ArrayList<Integer>> getPoplists() {
        return poplists;
    }

    public ArrayList<Double> getCrossproba() {
        return crossproba;
    }

    public ArrayList<Individu> getPopulation() {
        return population;
    }

    public double getMutationprob() {
        return mutationprob;
    }

    public int getPopsize() {
        return popsize;
    }

    public ArrayList<Individu> getChildren_pop() {
        return children_pop;
    }

    public ArrayList<ArrayList<ArrayList<Integer>>> getFronts_c() {
        return fronts_c;
    }

    public ArrayList<ArrayList<Integer>> getFitness_r() {
        return fitness_r;
    }

    public ArrayList<Individu> getInitial_pop() {
        return initial_pop;
    }

    public ArrayList<Individu> getParent_pop() {
        return parent_pop;
    }

    public int getNumiter() {
        return numiter;
    }

    //Setters

    public void setCrossproba(ArrayList<Double> crossproba) {
        this.crossproba = crossproba;
    }

    public void setFitness(ArrayList<ArrayList<Double>> fitness) {
        this.fitness = fitness;
    }

    public void setPoplists(ArrayList<ArrayList<Integer>> poplists) {
        this.poplists = poplists;
    }

    public void setFronts(ArrayList<ArrayList<ArrayList<Integer>>> fronts) {
        this.fronts = fronts;
    }

    public void setPopsize(int popsize) {
        this.popsize = popsize;
    }

    public void setPopulation(ArrayList<Individu> population) {
        this.population = population;
    }

    public void setMutationprob(double mutationprob) {
        this.mutationprob = mutationprob;
    }

    public void setChildren_pop(ArrayList<Individu> children_pop) {
        this.children_pop = children_pop;
    }

    public void setFitness_r(ArrayList<ArrayList<Integer>> fitness_r) {
        this.fitness_r = fitness_r;
    }

    public void setFronts_c(ArrayList<ArrayList<ArrayList<Integer>>> fronts_c) {
        this.fronts_c = fronts_c;
    }

    public void setInitial_pop(ArrayList<Individu> initial_pop) {
        this.initial_pop = initial_pop;
    }

    public void setParent_pop(ArrayList<Individu> parent_pop) {
        this.parent_pop = parent_pop;
    }

    public void setNumiter(int numiter) {
        this.numiter = numiter;
    }

    //Génération de la taille de la population et de la probabilité de mutation
    public void gennumpop(){
        Scanner sc = new Scanner(System.in); //variable pour la lecture
        //Scanner sc1 = new Scanner(System.in);

        System.out.print("Population size : ");
        //taille de la population
        int sp = sc.nextInt();
        setPopsize(sp);
        System.out.println();

        System.out.print("Mutation probability : ");
        //probabilité de mutation
        double mp = sc.nextDouble();
        setMutationprob(mp);
        System.out.println();

        System.out.println("Iteration number");
        //Nombre d'itérations
        sp = sc.nextInt();
        setNumiter(sp);
    }

    //génération du premier individu de la population
    public Individu genoneind(){
        Individu pep = new Individu();
        ArrayList<Double> fit = new ArrayList<>();
        pep.setId(0);
        pep.genperiod();
        pep.genprod();
        pep.display_product();
        pep.gengrade();
        pep.gencli();
        pep.genmdis();
        pep.display_mdis();
        pep.genmatgrade();
        pep.genfreq();
        pep.display_freq();
        pep.gencode1_3();
        pep.gencode2();
        pep.interpretation();
        pep.totalcost();
        fit.add(pep.total_cost);
        pep.totalcfp();
        fit.add(pep.total_cfp);
        //pep.display_stock();
        //pep.display_remcom();
        //pep.display_perfopro();
        //pep.display_percom();
        //pep.display_hamcy();
        //pep.display_procli();
        this.fitness.add(fit);
        this.population.add(pep);
        this.initial_pop.add(pep);
        this.parent_pop.add(pep);
        return pep;
    }



    //Génération de la première population de Parents
    public void firstpopgen(){
        //On génère le premier individu
        ArrayList<Double> fit = new ArrayList<>();
        Individu pep = population.get(0);

        Individu indp;
        ArrayList<Product> pro;
        Product prod;
        for(int i = 1; i < popsize; i++){
            fit = new ArrayList<>();
            indp = new Individu();
            pro = new ArrayList<>();
            for(int j = 0; j < pep.products.size(); j++){
                prod = pep.products.get(j).clone();
                prod.gencom();
                for(int l = 0; l < prod.number_components; l++){
                    prod.com.get(l).setImportance_c(pep.products.get(j).com.get(l).importance_c);
                }
                prod.setState(-1);
                prod.cal_rem();
                prod.calculperf();
                pro.add(prod);
            }
            indp.setId(i);
            indp.setNclient(pep.nclient);
            indp.setNproduct(pep.nproduct);
            indp.setHorizon(pep.horizon);
            indp.setPeriod(pep.period);
            indp.setGrade(pep.grade);
            indp.setMatgrade(pep.matgrade);
            indp.setMdis(pep.mdis);
            indp.setMdisp(pep.mdisp);
            indp.setFrequencies(pep.frequencies);
            indp.setClients(pep.clients);
            indp.setProducts(pro);
            indp.gencode1_3();
            indp.gencode2();
            indp.interpretation();
            indp.totalcost();
            fit.add(indp.total_cost);
            indp.totalcfp();
            fit.add(indp.total_cfp);
            this.fitness.add(fit);
            //System.out.println("Hashcode de " + i + " est : " + indp.products.hashCode());
            //indp.display_procli();
            //indp.display_percom();
            //indp.display_perfopro();
            //indp.display_remcom();
            //indp.display_hamcy();
            //indp.display_stock();
            this.population.add(indp);
            this.initial_pop.add(indp);
            this.parent_pop.add(indp);
        }
    }

    //Détermination des fronts
    public ArrayList<ArrayList<Integer>> genfronts(ArrayList<Integer> sol){ //la variable sol est un élément de poplists
        ArrayList<ArrayList<Integer>> niv1 = new ArrayList<>();
        ArrayList<Integer> niv2 ;
        ArrayList<Integer> visit = new ArrayList<>();
        ArrayList<Integer> supp;


        while(visit.size() !=  sol.size()){ //tant que tous les éléments ne sont pas visités
            //System.out.println("Fronts while i = " + i);
            niv2 =  new ArrayList<>();
            for(int j = 0; j < sol.size(); j++){
                if(!(visit.contains(sol.get(j)))){
                   niv2.add(sol.get(j));
                   visit.add(sol.get(j));
                   break;
                }
            }
            for(int l = 0; l < sol.size(); l++) {
                supp = new ArrayList<>();
                if ((!visit.contains(sol.get(l))) && (!(niv2.contains(sol.get(l))))) {
                    niv2.add(sol.get(l));
                    visit.add(sol.get(l));
                    for (int k = 0; k < niv2.size(); k++) {
                        if ((fitness.get(sol.get(l)).get(0) < fitness.get(niv2.get(k)).get(0)) && (fitness.get(sol.get(l)).get(1) < fitness.get(niv2.get(k)).get(1))) {
                            supp.add(niv2.get(k));
                        } else if ((fitness.get(sol.get(l)).get(0) > fitness.get(niv2.get(k)).get(0)) && (fitness.get(sol.get(l)).get(1) > fitness.get(niv2.get(k)).get(1))) {
                            supp.add(sol.get(l));
                            break;
                        }
                    }
                    for(int k = 0; k < supp.size(); k++){
                        niv2.remove(niv2.indexOf(supp.get(k)));
                        visit.remove(visit.indexOf(supp.get(k)));
                    }
                }
            }


            niv1.add(niv2);
            //niv2 = new ArrayList<>();
        }
        return niv1;
    }

    public int cal_fit(int ind){
        int fr = 0;

        for(int j = 0; j < fronts.get(fronts.size() - 1).size(); j++){
            for(int k= 0; k < fronts.get(fronts.size() - 1).get(j).size(); k++){
                if(fronts.get(fronts.size() - 1).get(j).get(k) == ind){
                    fr = j;
                }
            }
        }

        return fr;
    }

    public void crossover1(int i, int j){
        Individu indp;
        ArrayList<Product> pro = new ArrayList<>();
        Product prod;
        ArrayList<Double> fit;
        fit = new ArrayList<>();
        indp = new Individu();
        indp.setId(population.size());
        indp.setNclient(population.get(i).nclient);
        indp.setNproduct(population.get(i).nproduct);
        indp.setHorizon(population.get(i).horizon);
        indp.setPeriod(population.get(i).period);
        indp.setGrade(population.get(i).grade);
        indp.setMatgrade(population.get(i).matgrade);
        indp.setMdis(population.get(i).mdis);
        indp.setMdisp(population.get(i).mdisp);
        indp.setFrequencies(population.get(i).frequencies);
        indp.setClients(population.get(i).clients);
        for(int k = 0; k < population.get(i).products.size(); k++){
            prod = population.get(i).products.get(k).clone();
            prod.gencom();
            for(int l = 0; l < prod.number_components; l++){
                prod.com.get(l).setImportance_c(population.get(i).products.get(k).com.get(l).importance_c);
            }
            prod.setState(-1);
            prod.cal_rem();
            prod.calculperf();
            pro.add(prod);
        }
        indp.setProducts(pro);

        //Déterminer le point de croisement
        int point = (int) (Math.random() * (indp.horizon - 2)) + 1;
        //Déterminer si il y a mutation
        double mut = Math.random();
        int[][] cod1 = new int[indp.nclient][indp.horizon];
        double[][] cod2 = new double[indp.nclient][indp.horizon];
        int[][] cod3 = new int[indp.nclient][indp.horizon];
        for(int l = 0; l < indp.nclient; l++){
            for(int k = 0; k < indp.horizon; k++){
                if(k < point) {
                    cod1[l][k] = population.get(i).codage1[l][k];
                    cod2[l][k] = population.get(i).codage2[l][k];
                    cod3[l][k] = population.get(i).codage3[l][k];
                }
                else{
                    cod1[l][k] = population.get(j).codage1[l][k];
                    cod2[l][k] = population.get(j).codage2[l][k];
                    cod3[l][k] = population.get(j).codage3[l][k];
                }
            }
        }

        //Opération de mutation
        if(mut > mutationprob){
            int mt1 = 0;
            int mt2 = 0;
            while(mt1 == mt2){
                mt1 = (int) (Math.random() * (indp.horizon - 1));
                mt2 = (int) (Math.random() * (indp.horizon - 2));
            }
            int med;
            double medi;
            for(int l = 0; l < indp.nclient; l++){
                med = cod1[l][mt1];
                cod1[l][mt1] = cod1[l][mt2];
                cod1[l][mt2] = med;

                medi = cod2[l][mt1];
                cod2[l][mt1] = cod2[l][mt2];
                cod2[l][mt2] = medi;

                med = cod3[l][mt1];
                cod3[l][mt1] = cod3[l][mt2];
                cod3[l][mt2] = med;
            }

        }
        indp.setCodage1(cod1);
        indp.setCodage2(cod2);
        indp.setCodage3(cod3);
        indp.interpretation();
        //indp.display_product();
        indp.totalcost();
        fit.add(indp.total_cost);
        indp.totalcfp();
        fit.add(indp.total_cfp);
        //indp.display_procli();
        //indp.display_percom();
        //indp.display_perfopro();
        //indp.display_remcom();
        //indp.display_hamcy();
        //indp.display_stock();
        this.fitness.add(fit);
        this.population.add(indp);
        this.children_pop.add(indp);
    }

    //Calcul crowding distance
    public double[] crowding(ArrayList<Integer> fr){
        double[] cd = new double [fr.size()];
        int[] sort1 = new int[fr.size()];
        int[] sort2 = new int[fr.size()];

        for(int i = 0; i < fr.size(); i++){
            cd[i] = 0.;
            sort1[i] = fr.get(i);
            sort2[i] = fr.get(i);
        }

        int inter;
        for(int i = 0; i < fr.size(); i++){
            for(int j = i + 1; j < fr.size(); j++){
                if(fitness.get(sort1[i]).get(0) < fitness.get(sort1[j]).get(0)){
                    inter = sort1[i];
                    sort1[i] = sort1[j];
                    sort1[j] = inter;
                }
            }
        }

        for(int i = 0; i < fr.size(); i++){
            for(int j = i + 1; j < fr.size(); j++){
                if(fitness.get(sort2[i]).get(1) < fitness.get(sort2[j]).get(1)){
                    inter = sort2[i];
                    sort2[i] = sort2[j];
                    sort2[j] = inter;
                }
            }
        }

        cd[fr.indexOf(sort1[0])] = Double.POSITIVE_INFINITY;
        cd[fr.indexOf(sort1[fr.size() - 1])] = Double.POSITIVE_INFINITY;

        for(int i = 1; i < fr.size() - 1; i++){
            cd[fr.indexOf(sort1[i])] = cd[fr.indexOf(sort1[i])] + (fitness.get(sort1[i+1]).get(0) - fitness.get(sort1[i-1]).get(0));
        }

        cd[fr.indexOf(sort2[0])] = Double.POSITIVE_INFINITY;
        cd[fr.indexOf(sort2[fr.size() - 1])] = Double.POSITIVE_INFINITY;

        for(int i = 1; i < fr.size() - 1; i++){
            cd[fr.indexOf(sort1[i])] = cd[fr.indexOf(sort1[i])] + (fitness.get(sort2[i+1]).get(1) - fitness.get(sort2[i-1]).get(1));
        }

        return cd;
    }

    public boolean existe(ArrayList<ArrayList<Integer>> tempo, int n1, int n2){ //pour voir si un couple de nombre a déjà été choisi pour un croisement durant la période en cours
        boolean exi = false;

        for(int i = 0; i < tempo.size();i++){
            if(((tempo.get(i).get(0) == n1) && (tempo.get(i).get(1) == n2)) || ((tempo.get(i).get(0) == n2) && (tempo.get(i).get(1) == n1))){
                exi = true;
            }
        }
        return exi;
    }

    public int[] decsort(double[] c, ArrayList<Integer> fr){ //tri décroissant pour la crowding distance
        int[] res = new int[fr.size()];
        double[] cp = new double[fr.size()];
        double n;
        ArrayList<Integer> visit = new ArrayList<>();

        for(int i = 0; i < fr.size(); i++){
            cp[i] = c[i];
        }
        for(int i  = 0; i < fr.size(); i++){
            for(int j = i+1; j < fr.size(); j++){
                if(cp[i]< cp[j]){
                    n = cp[i];
                    cp[i] = cp[j];
                    cp[j] = n;
                }
            }
        }

        for(int i = 0; i < fr.size(); i++){
            for(int j = 0; j < fr.size(); j++){
                if((cp[i] == c[j]) && (!(visit.contains(j)))){
                    visit.add(j);
                    res[i] = fr.get(j);
                    break;
                }
            }
        }

        return res;
    }

    public void NSGALGO(){

        //décalartion des variables intermédiaires

        ArrayList<ArrayList<Integer>> fro;
        ArrayList<ArrayList<Integer>> couples = new ArrayList<>();
        ArrayList<Integer> temp;
        ArrayList<Integer> tempp;
        ArrayList<ArrayList<Integer>> temp1;
        ArrayList<Double> tempd;
        boolean ex;
        int in1, in2;
        double ran;


        //génération de la population intiale
        //gennumpop();
        //firstpopgen();

        int i = 0;
        while (i < numiter) { //condition d'arrêt: avoir qu'un seul front (le front de Pareto) ou 5 itérations atteintes
            children_pop.clear();
            System.out.println("Itération " + (i+1));
            //trier la population des parents en fronts
            temp = new ArrayList<>();
            System.out.println("Parent pop size:" + parent_pop.size() + " fitness size :" + fitness.size());
            for(int j = 0; j < parent_pop.size(); j++){
                temp.add(parent_pop.get(j).getId());
            }
            poplists.add(temp);
            System.out.println("Calcul des fronts");
            fro = genfronts(temp);
            System.out.println("Fronts: " + fro);
            fronts.add(fro);
            displaypareto();

            //Calculer le fitness de chaque individu de la population parents
            temp = new ArrayList<>();
            System.out.println("Calcul fitness");
            for(int j = 0; j < parent_pop.size(); j++){
                 temp.add(cal_fit(parent_pop.get(j).getId()));
                 //System.out.println("Fitnes de " + parent_pop.get(j).getId() +" est : " + cal_fit(parent_pop.get(j).getId()));
            }
            fitness_r.add(temp);
            //System.out.println("Fitness: " + temp);

            //Probabilité de croisement
            tempd = new ArrayList<>();
            //System.out.println("Calcul proba de croisement");
            for(int j = 0; j < parent_pop.size(); j++){ //proba de croisement = (nombre de fronts - front de l'individu en cours) / (nombre de fronts +1)
                tempd.add(((fro.size() - ((double)(fitness_r.get(fitness_r.size() -1).get(j)))) / (fro.size() +1)));
            }
            setCrossproba(tempd); //affecter les probabilité des croisements à la variable crossproba dela classe NSGA
            //System.out.println(tempd);
            //Opérations de croisements - partie 1: choix des couples

            int cr = 0;
            temp1 = new ArrayList<>();
            //System.out.println("Croisement 1");
            while (cr < popsize){
                //System.out.println("Boucle while 1");
                ran = Math.random() * (((double)(fro.size())) / (fro.size() + 1)); //Nombre aléatoire au plus égal à la plus grande probabilité de croisement
                //System.out.println("Proba min de croisement : " + ran);
                temp = new ArrayList<>();
                tempp = new ArrayList<>();
                for(int j = 0; j < popsize; j++){
                    if(crossproba.get(j) >= ran){ //si la probilité de croisement de l'individu à la position j de la population parents courante a une probabilité de croisement supérieure ou égale au nombre généré aléatoirement
                        temp.add(poplists.get(poplists.size() - 1).get(j)); //On ajoute l'indice de l'individu conrrespondant à une liste temporaire
                    }
                }

                if((temp.size() ==2)){ // Si la taille des individus choisis préalablement est = 2 (pour pouvoir en choisir deux pour faire un croisement)
                    in1 = temp.get(0); //le croisement s'effectue entre ces deux individus
                    in2 = temp.get(1);
                    tempp.add(in1);
                    tempp.add(in2);
                    temp1.add(tempp); //on enregistre que ce duo a déjà été choisi
                    cr = cr+1;
                }
                else if (temp.size() > 2){
                    in1 = temp.get((int) (Math.random() * temp.size())); //on génère aléatoirement un nombre
                    //System.out.println("in1 =" + in1);
                    for(int j = 0; j < temp.size(); j++){
                        in2 = temp.get(j); //on choisit un second nombre parmi cux qui se trouve dans temp
                        //System.out.println("in2 =" + in2);
                        ex = existe(couples, in1, in2) || existe(temp1, in1, in2); //on vérifie si le couple de nombres choisi a déjà été visité
                        tempp = new ArrayList<>();
                        if((in1 != in2) && (ex == false)){ //si les deux nombres n'ont pas déjà choisis et ne sont pas identiques
                            //System.out.println("Ils sont choisis");
                            tempp.add(in1);
                            tempp.add(in2);
                            temp1.add(tempp); //on les enregistre comme étant choisis
                            couples.add(tempp); //on les enregistre pour qu'ils ne soient pas repris
                            cr = cr + 1; //on incrémente le compteur des couples choisis pour le croisement
                        }
                    }
                }
            }


            //Opérations de croisements - partie 2 : croisement + mutation
            //System.out.println("Croisement 2");
            //System.out.println("Les couples choisis :" + temp1);
            for(int j = 0; j < popsize; j++){
                crossover1(temp1.get(j).get(0), temp1.get(j).get(1));
            }

            //Sélection de la nouvelle population
            temp = new ArrayList<>(); //enregistrer les indices des individus parents et enfants de l'itération en cours
            //System.out.println("Nouvelle pop");
            for(int j = 0; j < popsize; j++){
                temp.add(parent_pop.get(j).getId());
            }

            for(int j = 0; j < popsize; j++){
                temp.add(children_pop.get(j).getId());
            }

            System.out.println("Parents et enfants: " + temp);

            fro = genfronts(temp); //tri en fronts dominants
            System.out.println("Fronts: " + fro);
            fronts_c.add(fro);
            //displaypareto();
            tempp = new ArrayList<>();
            int fr = 0;
            int s = fro.get(fr).size();
            while (s < popsize){ //On ajoute les indices des individus front par front
                //System.out.println("Boucle while 2");
                for(int j = 0; j < fro.get(fr).size(); j++){
                    tempp.add(fro.get(fr).get(j));
                }
                fr = fr +1;
                s += fro.get(fr).size();
            }
            //Le dernier front qui dépasse la taille de la population on calcule la crowding distance des individus le composant
            double[] crowd = crowding(fro.get(fr));
            //on trie les individus du dernier front en ordre décroissant de leur crwoding distance
            int resu[] = decsort(crowd, fro.get(fr));
            s -= fro.get(fr).size();
            //System.out.print("Tri des distances de foule: " );
            /*for(int j = 0; j < resu.length; j++){
                System.out.print("\t " + resu[j]);
            }*/
            for(int j = 0; j < popsize - s; j++){
                tempp.add(resu[j]);
            }
            //System.out.println("Nouvelle population : " + tempp);
            poplists.add(tempp); //on ajoute la liste des indices de la nouvelle population

            //On affecte les individus qu'il faut à la nouvelle population des parents
            parent_pop.clear();
            for(int j = 0; j < popsize; j++){
                parent_pop.add(population.get(tempp.get(j)));
            }
            //On incrémente le compteur d'itérations
            i = i + 1;
        }
    }

    public void freepop(){

        ArrayList<Double> fit = new ArrayList<>();
        while (population.size() != 1){
            population.remove(1);
        }

        initial_pop = new ArrayList<>();
        fitness = new ArrayList<>();
        parent_pop = new ArrayList<>();
        poplists = new ArrayList<>();
        children_pop = new ArrayList<>();
        fronts = new ArrayList<>();
        fronts_c = new ArrayList<>();
        fitness_r = new ArrayList<>();
        crossproba = new ArrayList<>();

        initial_pop.add(population.get(0));
        parent_pop.add(population.get(0));
        fit.add(population.get(0).total_cost);
        fit.add(population.get(0).total_cfp);
        fitness.add(fit);

    }

    public void displaypop(){
        for(int i = 0; i < population.size(); i++){
            population.get(i).display_codage();
        }
        for(int i = 0; i < poplists.size(); i++){
            System.out.println("Solution a à l'itération " + (i+1) + " est: " + poplists.get(i));
        }
        for(int i = 0; i < fronts.size(); i++){
            System.out.println("Fronts à l'intération " + (i+1) + " est: " + fronts.get(i));
        }
    }

    public void  displaypareto(){
        System.out.println("Front de Pareto: " + fronts.get(fronts.size() - 1).get(0));

        for(int i = 0; i < fronts.get(fronts.size() - 1).get(0).size(); i++){
            System.out.println("Fitness de " + fronts.get(fronts.size() - 1).get(0).get(i) + " \t " + fitness.get(fronts.get(fronts.size() - 1).get(0).get(i)).get(0) + " \t " + (fitness.get(fronts.get(fronts.size() - 1).get(0).get(i)).get(1))) ;
        }
    }

    public int[] TOPSIS(double par1, double par2){
        //int[] order = new int[fronts.get(fronts.size() - 1).get(0).size()]; //ordre donné par TOPSIS
        int[] parfro =  new int[fronts_c.get(fronts_c.size() - 1).get(0).size()]; // récupérer les solutions qui sont dans le front de Pareto à la derniere itération
        double[][] objmat = new double[fronts_c.get(fronts_c.size() - 1).get(0).size()][2]; //conserver les valeurs des fonctions objectifs
        double[][] vj =  new double [2][2];

        //récupérer les valeurs des fonctions objectifs
        System.out.println("Les solutions du front de Pareto: ");
        for(int i = 0; i < fronts_c.get(fronts_c.size() - 1).get(0).size(); i++){
            parfro[i] = fronts_c.get(fronts_c.size() - 1).get(0).get(i);
            System.out.print("\t" + parfro[i]);
        }
        System.out.println();
        System.out.println("Les valeurs des fonctions objectifs: ");
        for(int i = 0; i < parfro.length; i++){
            objmat[i][0] = fitness.get(parfro[i]).get(0);
            System.out.print(objmat[i][0] + "\t");
            objmat[i][1] = fitness.get(parfro[i]).get(1);
            System.out.print(objmat[i][1]);
            System.out.println();
        }

        //Normalisation
        double s1, s2;
        s1 = 0;
        s2 = 0;
        for(int i = 0; i < parfro.length; i++){
            s1 += objmat[i][0] * objmat[i][0];
            s2 += objmat[i][1] * objmat[i][1];
        }
        System.out.println("S1 = " + s1 + " S2 = " + s2);
        s1 =  Math.sqrt(s1);
        s2 = Math.sqrt(s2);
        System.out.println("S1 = " + s1 + " S2 = " + s2);

        for(int i = 0; i < parfro.length; i++){
            objmat[i][0] /= s1;
            System.out.print(objmat[i][0] + "\t");
            objmat[i][1] /= s2;
            System.out.print(objmat[i][1] + "\t");
            System.out.println();
        }

        //on multiplie par les poids
        for(int i = 0; i < parfro.length; i++){
            objmat[i][0] *= par1 ;
            System.out.print(objmat[i][0] + "\t");
            objmat[i][1] *= par2;
            System.out.print(objmat[i][1]);
            System.out.println();
        }

        //On calcule ideal best et ideal worst
        double[] t1 = new double[parfro.length];
        double[] t2 = new double[parfro.length];
        for(int i = 0; i < parfro.length; i++){
            t1[i] = objmat[i][0];
            t2[i] = objmat[i][1];
        }

        vj[0][0] = bestvalue(t1);
        vj[0][1] = bestvalue(t2);
        vj[1][0] = worstvalue(t1);
        vj[1][1] = worstvalue(t2);

        double[][] si =  new double[parfro.length][2];

        for(int i = 0; i < parfro.length; i++){
            si[i][0] = Math.sqrt((objmat[i][0] - vj[0][0]) * (objmat[i][0] - vj[0][0]) + (objmat[i][1] - vj[0][1]) * (objmat[i][1] - vj[0][1]));
            si[i][1] = Math.sqrt((objmat[i][0] - vj[1][0]) * (objmat[i][0] - vj[1][0]) + (objmat[i][1] - vj[1][1]) * (objmat[i][1] - vj[1][1]));
        }

        double[] ci = new double[parfro.length];

        for(int i = 0; i < parfro.length; i++){
            ci[i] = si[i][1] / (si[i][0] + si[i][1]);
        }

        double best;
        int bestie;
        for(int i = 0; i < parfro.length; i++){
            for(int j = i; j < parfro.length; j++){
                if(ci[i] > ci[j]){
                    best = ci[i];
                    bestie = parfro[i];
                    ci[i] = ci[j];
                    parfro[i] = parfro[j];
                    ci[j] = best;
                    parfro[j] = bestie;
                }
            }
        }

        return parfro;
    }

    public double worstvalue(double[] t){
        double v = t[0];
        for(int i = 0; i < t.length; i++){
            for(int j = i; j < t.length; j++){
                if(t[j] > t[i]){
                    v = t[j];
                }
            }
        }

        return v;
    }

    public double bestvalue(double[] t){
        double v = t[0];
        for(int i = 0; i < t.length; i++){
            for(int j = i; j < t.length; j++){
                if(t[j] < t[i]){
                    v = t[j];
                }
            }
        }

        return v;
    }
}
