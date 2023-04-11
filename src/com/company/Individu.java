package com.company;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;


public class Individu {
    /*                             Identification                            */
    int id; //chiffre représentant l'individu
    /*                             Les données de base                            */
    int nclient; //nombre de clients
    int nproduct; //nombre de produits
    int horizon; //le nombre de périodes
    double[] period = new double[horizon]; //instants de chaque période
    int grade; //le nombre de grades pour les produits
    double[] matgrade =  new double[grade];  //répartition des grande;
    /*                             Les données de groupes produits et clients                            */
    ArrayList<Product> products = new ArrayList<>();
    ArrayList<Client> clients = new ArrayList<>();
    double[][] mdis = new double[nclient + 1][nclient + 1]; //cout de transport entre clients et centre de remanufacturing
    double[][] mdisp = new double[nclient + 1][nclient + 1]; //impact carbone de transport entre clients et centre de remanufacturing
    double[][] frequencies = new double[nclient][horizon]; //permet de stocker les frequences d'utilisation des clients.

    /*                             codage pour NSGA                           */

    int[][] codage1 = new int[nclient][horizon]; //ordre de priorité pour les clients à la récupération
    double[][] codage2 = new double[nclient][horizon]; //les réels pour déterminer les grades de remanufacturing
    int[][] codage3 = new int[nclient][horizon]; //ordre des clients lors de la récupération

    /*                             Construction de la solution                      */
    int[][] procli = new int[nclient][horizon + 1]; //A chaque période on a le produit qui en possession de chaque client
    ArrayList<ArrayList<ArrayList<Double>>> perfocom = new ArrayList<>(); //Tableau qui contient l'évolution des composants des produits
    double[][] perfopro = new double[nproduct][horizon + 1]; //performance d'un produit à tout moment
    int[][] stock = new int[horizon + 1][grade]; //etat du stock a chaque période de récupération
    ArrayList<ArrayList<ArrayList<Double>>> remcom = new ArrayList<>(); //Tableau qui contient l'évolution de la remanufacturabilité des composants des produits
    double[][] remanufacturabilitymat = new double[nproduct][horizon+1]; //matrice de remanufacturabilité
    ArrayList<ArrayList<Integer>> hamiltonian_cycle = new ArrayList<>(); //ArrayList qui va contenir les cycles hamiltoniens obtenu à chaque période selon les récupérations

    /*                             Fonctions objectifs                    */
    double total_cost;
    double total_cfp;
    //Getters
    public ArrayList<Product> getProducts() {
        return products;
    }

    public ArrayList<ArrayList<ArrayList<Double>>> getPerfocom() {
        return perfocom;
    }

    public ArrayList<ArrayList<Integer>> getHamiltonian_cycle() {
        return hamiltonian_cycle;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public double[] getPeriod() {
        return period;
    }

    public int getGrade() {
        return grade;
    }

    public double[][] getFrequencies() {
        return frequencies;
    }

    public int getHorizon() {
        return horizon;
    }

    public double[][] getMdis() {
        return mdis;
    }

    public double[][] getMdisp() {
        return mdisp;
    }

    public double[][] getPerfopro() {
        return perfopro;
    }

    public double[][] getRemanufacturabilitymat() {
        return remanufacturabilitymat;
    }

    public int getNclient() {
        return nclient;
    }

    public int getNproduct() {
        return nproduct;
    }

    public int[][] getProcli() {
        return procli;
    }

    public int[][] getStock() {
        return stock;
    }

    public double[] getMatgrade() {
        return matgrade;
    }

    public double[][] getCodage2() {
        return codage2;
    }

    public int[][] getCodage1() {
        return codage1;
    }

    public int[][] getCodage3() {
        return codage3;
    }

    public ArrayList<ArrayList<ArrayList<Double>>> getRemcom() {
        return remcom;
    }

    public double getTotal_cfp() {
        return total_cfp;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public int getId() {
        return id;
    }

    //Setters
    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }

    public void setHorizon(int horizon) {
        this.horizon = horizon;
    }

    public void setNclient(int nclient) {
        this.nclient = nclient;
    }

    public void setFrequencies(double[][] frequencies) {
        this.frequencies = frequencies;
    }

    public void setNproduct(int nproduct) {
        this.nproduct = nproduct;
    }

    public void setHamiltonian_cycle(ArrayList<ArrayList<Integer>> hamiltonian_cycle) {
        this.hamiltonian_cycle = hamiltonian_cycle;
    }

    public void setPerfocom(ArrayList<ArrayList<ArrayList<Double>>> perfocom) {
        this.perfocom = perfocom;
    }

    public void setPeriod(double[] period) {
        this.period = period;
    }

    public void setMdis(double[][] mdis) {
        this.mdis = mdis;
    }

    public void setMdisp(double[][] mdisp) {
        this.mdisp = mdisp;
    }

    public void setPerfopro(double[][] perfopro) {
        this.perfopro = perfopro;
    }

    public void setProcli(int[][] procli) {
        this.procli = procli;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void setRemanufacturabilitymat(double[][] remanufacturabilitymat) {
        this.remanufacturabilitymat = remanufacturabilitymat;
    }

    public void setStock(int[][] stock) {
        this.stock = stock;
    }

    public void setMatgrade(double[] matgrade) {
        this.matgrade = matgrade;
    }

    public void setCodage1(int[][] codage1) {
        this.codage1 = codage1;
    }

    public void setCodage2(double[][] codage2) {
        this.codage2 = codage2;
    }

    public void setCodage3(int[][] codage3) {
        this.codage3 = codage3;
    }

    public void setRemcom(ArrayList<ArrayList<ArrayList<Double>>> remcom) {
        this.remcom = remcom;
    }

    public void setTotal_cfp(double total_cfp) {
        this.total_cfp = total_cfp;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

    public void setId(int id) {
        this.id = id;
    }

    //Nombre de périodes
    public void genperiod(){
        int h; //nombre de périodes
        Scanner sc = new Scanner(System.in); //variable pour la lecture

        System.out.print("Number of periods : ");
        h = sc.nextInt();
        setHorizon(h);
        System.out.println();

        double[] p = new double[21];
        p[0] = 0.;
        int t = 365;
        int count = 0;
        int j = 1;
        int l;
        int c;
        while (count < 5) {
            l = j;
            c = 1;
            for (int i = j; i < l + count + 2; i++) {
                p[i] = count * 365 + c * (t - count * 365) / (count + 2);
                j++;
                c++;
            }
            t += 365;
            count++;
        }

        /*for(int i = 0; i < horizon + 1; i++){
            System.out.print(p[i] +"\t");
        }
        System.out.println();*/
        double[] pt = new double[horizon + 1];
        for (int k = 0; k < horizon + 1; k++) {
            pt[k] = p[k];
        }
        setPeriod(pt);
    }

    //nombre de grade
    public void gengrade(){
        int gr;
        Scanner sc = new Scanner(System.in);
        System.out.print("Number of grades : ");
        gr = sc.nextInt();
        setGrade(gr);
        System.out.println();
    }

    //Générer les produits
    public void genprod(){
        int p, c, f; //déclaration des variables pour l'horizon les clients les produits et les qualités
        Scanner sc = new Scanner(System.in); //variable pour la lecture

        System.out.print("Number of products : ");
        p = sc.nextInt();
        setNproduct(p);
        System.out.println();

        System.out.print("Number of components : ");
        c = sc.nextInt();
        System.out.println();

        System.out.print("Number of features : ");
        f = sc.nextInt();
        System.out.println();

        //Créer un objet de type produit et commencer à remplir la arraylist des produits
        Product prp = new Product();
        prp.setType(1);
        prp.setId(0);
        prp.setNumber_components(c);
        prp.setNumber_features(f);
        prp.genfeatpartr();
        prp.genfea();
        prp.gencom();
        prp.genimppart();
        prp.setState(-1);
        prp.cal_rem();
        prp.calculperf();

        this.products.add(prp);

        Product pr;
        for(int i = 1 ; i < nproduct; i++){
            pr = new Product();
            pr.setType(1);
            pr.setId(i);
            pr.setNumber_components(c);
            pr.setNumber_features(f);
            pr.setState(-1);
            pr.setComfea(prp.getComfea());
            pr.gencom();
            for(int  j = 0; j < c; j++){
                pr.com.get(j).setImportance_c(prp.com.get(j).importance_c);
            }
            pr.setFea(prp.getFea());
            pr.cal_rem();
            pr.calculperf();
            this.products.add(pr);
        }
    }

    public void gencli(){
        int cl; //déclaration des variables pour l'horizon les clients les produits et les qualités
        Scanner sc = new Scanner(System.in); //variable pour la lecture

        System.out.print("Number of clients : ");
        cl = sc.nextInt();
        setNclient(cl);
        System.out.println();

        //Créer un objet de type produit et commencer à remplir la arraylist des produits
        Client cli;

        for(int i =0 ; i < nclient; i++){
            cli = new Client();
            cli.setId(i);
            cli.setProfile((int)(Math.random() * 10)); //un profil parmis 10
            clients.add(cli);
        }
    }

    public void genmdis(){
        int d;
        double[][] dis = new double[nclient + 1][nclient + 1];
        double[][] dis1= new double[nclient + 1][nclient + 1];
        double[][] dis2 = new double[nclient + 1][nclient + 1];

        for(int i = 0; i < nclient +1; i++){
            dis[i][i] = 0;
        }
        for(int i = 0; i < nclient +1; i++){
            for(int j = i + 1; j < nclient +1; j++){
                d = (int) (Math.random() * 3);
                if(d == 0){
                    dis[i][j] = Math.random() * 50 + 5;
                }
                else if(d == 1){
                    dis[i][j] = 51 + Math.random() * (51);
                }
                else{
                    dis[i][j] = 100 + Math.random() * 100;
                }
                dis[j][i] = dis[i][j];
            }
        }

        for(int i = 0; i < nclient +1; i++){
            for(int j = 0; j < nclient +1; j++){
                dis1[i][j] = dis[i][j] * ((10.39 * 97.82 / 100 + 2.125 * 0.46 / 100 + 6.2 * 1.72 / 100) / 100);
                dis2[i][j] = dis[i][j] * ((0.028 * 97.82 / 100 + 0.012 * 0.46 / 100 + 0.018 * 1.72 / 100) / 100);
            }
        }
        setMdis(dis1);
        setMdisp(dis2);
    }

    //générer les grades
    public void genmatgrade(){
        double[] gr = new double[grade];
        for(int i = 0; i < grade; i++){
            gr[i] = (i+1) * (1./grade);
        }
        setMatgrade(gr);
    }

    //Afficher les clients
    public void display_client(){
        for(int i = 0; i < nclient; i++){
            System.out.println("Client id: " + clients.get(i).id);
            System.out.println("Client profile: " + clients.get(i).profile);
        }
    }

    //générer tri aléatoire
    public int[] genrandomsort(){
        ArrayList<Integer> cd1 = new ArrayList<>();

        for(int i = 0; i < nclient; i++){
            cd1.add(i);
        }

        int[] st = new int[nclient];
        int rand;

        for(int i=0; i < nclient; i++){
            rand = (int) (Math.random() * cd1.size());
            st[i] = cd1.get(rand);
            cd1.remove(rand);
        }

        return st;
    }

    //générer codage 1 et codage 3 simultanément
    public void gencode1_3(){
        int[][] cod1 = new int[nclient][horizon];
        int[][] cod3 = new int[nclient][horizon];
        int[] code1;
        int[] code3;

        for(int i = 0; i < horizon; i++){
            code1 = genrandomsort();
            code3 = genrandomsort();
            for(int j=0; j < nclient; j++){
                cod1[j][i] = code1[j];
                cod3[j][i] = code3[j];
            }
        }
        setCodage1(cod1);
        setCodage3(cod3);
    }

    //générer codage 2 (tableau de nombre aléatoires compris entre 0 et 1)
    public void gencode2(){
        double[][] cod2 = new double[nclient][horizon];
        for(int i = 0; i < nclient; i++){
            for(int j = 0; j < horizon; j++){
                cod2[i][j] = Math.random();
            }
        }
        setCodage2(cod2);
    }

    //générer les fréquences d'utilisation
    public void genfreq(){
        double[][] fr = new double[nclient][horizon];

        for(int i = 0; i < nclient; i++){
            for(int j = 0; j < horizon; j++){
                fr[i][j] = (clients.get(i).profile / 10.) + Math.random() * (0.1) ;
            }
        }
        setFrequencies(fr);
    }


    //Interprétation et construction de la solution
    public void interpretation(){
        int[][] prcl = new int[nclient][horizon +1];
        double[][] perpro = new double[nproduct][horizon +1];
        int[][] st = new int[horizon + 1][grade];
        double[][] rempro = new double[nproduct][horizon+1];

        ArrayList<ArrayList<ArrayList<Double>>> perco = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Double>>> remco = new ArrayList<>();
        ArrayList<ArrayList<Integer>> hamcy = new ArrayList<>();

        //Listes temporaire
        ArrayList<Integer> tempcl;
        ArrayList<Integer> tempco;
        ArrayList<ArrayList<Double>> temp1;
        ArrayList<Double> temp2;

        //Variables
        double var = 0;
        int position;
        int inter;
        boolean available;
        /*                */

        for(int i = 0; i < horizon +1; i++) { //boucle générale du début à la fin des péridoes
            if (i == 0) { //initialisation : affectation du produit 1 au client 1 produit 2 au client 2...
                for (int j = 0; j < nclient; j++) {
                    prcl[j][i] = j; //dans le tableau procli
                    products.get(j).setState(j); //ainsi que dans le statut du produit
                }
                st[i][0] = nproduct - nclient; //initialiser le stock de produits neufs
                for (int j = 1; j < grade; j++) { //initialiser les autres grades à 0
                    st[i][j] = 0;
                }
            }

            //affectation des performances des composants dans la arraylist
            temp1 = new ArrayList<>();
            for (int k = 0; k < nproduct; k++) {
                temp2 = new ArrayList<>();
                for (int j = 0; j < products.get(k).number_components; j++) {
                    temp2.add(products.get(k).com.get(j).performance);
                }
                temp1.add(temp2);
            }
            perco.add(temp1);

            //Affectation des remanufacturabilité des composants
            temp1 = new ArrayList<>();
            for (int k = 0; k < nproduct; k++) {
                temp2 = new ArrayList<>();
                for (int j = 0; j < products.get(k).number_components; j++) {
                    if(products.get(k).com.get(j).rem <=0){
                        products.get(k).setTotal_rem(0.);
                        products.get(k).setState(-3);
                    }
                    temp2.add(products.get(k).com.get(j).rem);
                }
                temp1.add(temp2);
            }
            remco.add(temp1);

            //Affectation des performances et remanufacturabilité des produits
            for (int j = 0; j < nproduct; j++) {
                products.get(j).calculperf();
                if(products.get(j).total_rem > 0){
                    products.get(j).cal_rem();
                }
                perpro[j][i] = products.get(j).total_performance;
                rempro[j][i] = products.get(j).total_rem;
            }

            //display_product();

            if (i >= 1) {
                //Calcul des nouvelles performances des composants des produits qui sont en cours d'utilisation
                //System.out.println("ETAPE 1 : CALCUL DES PERFORMANCES DES PRODUITS EN COURS D'UTILISATION");
                temp1 = new ArrayList<>();
                for (int j = 0; j < products.size(); j++) {
                    temp2 = new ArrayList<>();
                    //System.out.println("State de " + j + " = " + products.get(j).state);
                    if ((products.get(j).state > -1) && (products.get(j).total_performance > 0)) { //si le produit est en cours d'utilisation
                        //System.out.println("if 0");
                        for (int k = 0; k < products.get(j).number_components; k++) { //On parcourt les composants
                            //pour chaque composant on calcule la performance après son utilisation
                            var = products.get(j).com.get(k).performance -  1.5 * Math.sqrt(products.get(j).com.get(k).importance_c * frequencies[products.get(j).state][i - 1]) * ((period[i] - period[i-1]) / period[horizon]);
                            //System.out.println("produit " + j + " composant " + k + " perfomance " +  var);
                            temp2.add(var); //on ajoute cette performance à la liste des performances des composants
                            products.get(j).com.get(k).setPerformance(var); //On met à jour la performance du composant en cours
                            //System.out.println(" composant " + k + " perfomance " + products.get(j).com.get(k).performance);
                        }
                    }
                    else { //Si le produit est stocké ou n'est plus en service
                        //System.out.println("Performance de " + j + " = " + products.get(j).total_performance);
                        //System.out.println("else 0");
                        for (int k = 0; k < products.get(j).number_components; k++) { //On parcourt les composants un par un
                            //pour chaque composant la performance ne change pas car il n'est pas utilisé
                            //System.out.println("produit " + j + " composant " + k + " perfomance " + products.get(j).com.get(k).getPerformance());
                            temp2.add(products.get(j).com.get(k).performance); //On ajoute la performance du composant à la liste
                        }
                    }
                    products.get(j).calculperf(); //On calcule la performance totale du produit et on la met à jour
                    //System.out.println("Performance de " + j + " = " + products.get(j).total_performance);
                    temp1.add(temp2); //On ajoute la liste des performances des composants du produit en cours de traitement à la liste
                }
                perco.add(temp1);

                for (int j = 0; j < grade; j++) { //On copie l'état du stock de la période précédente dans la période en cours
                    st[i][j] = st[i - 1][j];
                }

                //System.out.println("ETAPE 2 : DETERMINATION DE LA LISTE DES CLIENTS DE LA RECUPERATION");
                tempcl = new ArrayList<>();
                //Détermination de la liste des clients qu'on récupère
                position = 0; //On initialise à la position 0
                while(position < nclient){
                    available = dispostock(); //On vérifie si il y a des produits disponibles en stock
                    if (available == true) { //Si il y a des produits disponibles
                        //System.out.println("Position " + position);
                        for (int j = 0; j < nclient; j++) {//On parcourt les clients et par la même occasion la colonne de la période en cours du tableau codage 1
                            //System.out.println("Il y a des produits disponibles "); //Afficher la disponibilité
                            if (codage1[j][i - 1] == position) { //Si l'élément de la colonne en cours de traitement de codage1 correspond à la position actuelle
                                //System.out.println("Le client " + j + " match");
                                //On parcourt les produits un par un
                                for (int k = 0; k < nproduct; k++) {
                                    //Si le produit est en stock et sa performance est supérieure à celle du produit en cours d'utilisation par le client
                                    //System.out.println("Le produit " + k + " est visité");
                                    if ((products.get(k).state == -1) && (products.get(k).total_performance > products.get(prcl[j][i-1]).total_performance)) {
                                        //System.out.println("Le client " + j + " est ajouté à la liste des rcupérations");
                                        tempcl.add(j); //On ajoute ce client à la liste des récupérations
                                        products.get(k).setState(j); //On met à jour le statut du produit en cours en lui attribuant le numéro du client qui va le posséder
                                        products.get(prcl[j][i-1]).setState(-2); //On met à -2 l'état du produit récupéré du client en cours de taitement
                                        prcl[j][i] = k; //On met à jour le produit en possession du client j en lui affectant le produit
                                        //mettre à jour le stock
                                        inter = perfograde(products.get(k).total_performance); //On calcule le grade du produit qu'on remet au client j
                                        st[i][inter] = st[i][inter] - 1; // On le soustrait de la case du stock correspondante
                                        //On va changer la performance du produit récupéré à la nouvelle performance (qu'on utilisera pour savoir quelle performance va-t-il atteindre après une remise à neuf)
                                        inter = perfograde(products.get(prcl[j][i-1]).total_performance);
                                        products.get(prcl[j][i-1]).setTotal_performance((grade - ((int) (inter * codage2[j][i-1]))) * (1. / grade));
                                       // System.out.println("La performance visée du produit " + prcl[j][i-1] + " est : " + products.get(prcl[j][i-1]).total_performance);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    else {
                        //System.out.println("Il n'y a pas des produits disponibles ");
                        break; //Si pas de produits disponible on sort de cette boucle
                    }
                    position = position +1;
                }

                //Pour les clients qui ne sont pas dans la liste de récupérations pour cette période on leur affecte le produit qu'ils possédaient précedemment
                for (int j = 0; j < nclient; j++) {
                    if (!tempcl.contains(j)) {
                        prcl[j][i] = prcl[j][i - 1];
                    }
                }

                //System.out.println("Clients à récupérer à  la période " + i);
                for(int j = 0; j < tempcl.size(); j++){
                    //System.out.print(tempcl.get(j) + "\t");
                }
                System.out.println();

                //Opérations de remise à neuf
                //System.out.println("ETAPE 3 : DETERMINATION DES PERFORMANCES DE REMISE A NEUF");
                temp1 = new ArrayList<>();
                for (int j = 0; j < nproduct; j++) {
                    //System.out.println("Le produit " + j + " est visité");
                    tempco = new ArrayList<>(); //liste temporaire pour enregistrer
                    double[] percom = new double[products.get(j).number_components]; //tableau temporaire pour conserver les performances des composants
                    int[] percoms = new int[products.get(j).number_components]; //tableau temporaire pour enregistrer si un composant a été visité ou non
                    temp2 = new ArrayList<>();
                    if ((products.get(j).getState() == -2) && (products.get(j).getTotal_rem() > 0)) { //Si le produit est toujours remanufacturable
                        //System.out.println("Le produit " + j + " est envoyé en remise à neuf");
                        products.get(j).setState(-1); //mettre à jour l'état du produit
                        for (int k = 0; k < products.get(j).number_components; k++) { //Pour chaque produit
                            //System.out.println("Le composant " + k + " est visité");
                            percoms[k] = 0;
                            if (products.get(j).com.get(k).rem > 0) { //Pour chaque composant
                                //System.out.println("Le composant " + k + " est remanufacturable");
                                if (products.get(j).com.get(k).performance > products.get(j).total_performance) { //Si la performance du composant k du produit j est supérieure à la performance visée
                                    //System.out.println("Le composant " + k + " a une performance plus élelvée");
                                    percom[k] = products.get(j).com.get(k).performance; // On enregistre temporairement cette performance dans le tableau
                                    percoms[k] = 1; // On attribut 1 à la case du deuxième tableau pour ce composant pour préciser qu'il a été visité
                                    tempco.add(k); //On ajoute ce composant à la liste pour dire qu'il a été traité
                                    double p1 = 0; //utilisé pour calcul
                                    for (int l = 0; l < products.get(j).number_features; l++) { //on calcule cette entité
                                        p1 += products.get(j).comfea[l][k] * products.get(j).fea.get(l).importance;
                                    }
                                    double p2 = 0; //utilisé pour calcul
                                    for (int r = 0; r < products.get(j).number_components; r++) { //On parcourt les composants
                                        if (!tempco.contains(r)) { //Si le composant r n'est pas dans la liste tempco
                                            for (int l = 0; l < products.get(j).number_features; l++) { //On calcule cette entité
                                                p2 += products.get(j).comfea[l][r] * products.get(j).fea.get(l).importance;
                                            }
                                            inter = perfograde(products.get(j).total_performance); //on calcule le grade du produit en cours 
                                            if (products.get(j).com.get(r).performance < ((((p1 + p2) * ((1. / grade) * (grade - inter))) - products.get(j).com.get(k).performance * p1) / p2)) {
                                                //Si le grade du composant en cours est inférieur au grade qu'on souhaitrait viser alors on met à jour sa performance
                                                percom[r] = ((((p1 + p2) * ((1. / grade) * (grade - inter))) - products.get(j).com.get(k).performance * p1) / p2);
                                                percoms[r] = 1; //on change son statut à composant visité
                                                tempco.add(r); //on ajoute ce composant à la liste des composants traités
                                                //on sort de la boucle
                                                break;
                                            }
                                            else { //Si la performance du composant est plus élevée que la performance visée alors
                                                percoms[r] = 0; //ce composant n'est pas visité
                                                tempco.add(r);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        for (int k = 0; k < products.get(j).number_components; k++) {
                            if (percoms[k] != 1) { //pour chaque composant
                                //si le composant n'est pas visité alors lui attribuée la performance visée pour le produit
                                percom[k] = products.get(j).total_performance;
                                products.get(j).com.get(k).setPerformance(percom[k]);
                                percoms[k] = 1; //mettre à jour son statut
                            }
                            temp2.add(percom[k]);
                            //calcul de la nouvelle remanufacturabilité
                            double pr1 = products.get(j).com.get(k).rem;
                            //System.out.println("Taille: " + perco.size() + "\t" + perco.get(perco.size() - 1).size() + "\t"); //+ perco.get(perco.size() -1).get(j).size());
                            double pr2 = perco.get(perco.size() - 1).get(j).get(k);
                            products.get(j).com.get(k).setRem(pr1 - (percoms[k] - pr2) * products.get(j).com.get(k).importance_c );
                        }
                        products.get(j).calculperf();

                        if (products.get(j).total_performance > 0.) {
                            inter = perfograde(products.get(j).total_performance);
                            st[i][inter] = st[i][inter] + 1;
                        }
                    }
                    else {
                        for (int k = 0; k < products.get(j).number_components; k++) {
                            temp2.add(products.get(j).com.get(k).performance);
                        }
                    }
                    temp1.add(temp2);
                }
                perco.add(temp1);

                //construction du cycle hamiltonien
                //System.out.println("ETAPE 4 : CREATION DU CYCLE HAMILTONIEN");
                tempco = new ArrayList<>(); //va contenir le cycle hamiltonien de la période en cours
                tempco.add(nclient); //on commence par l'indice du centre de remanufacturing
                position = 0;
                while(position < nclient) {
                    //System.out.println("Poistion : " + position);
                    for (int j = 0; j < tempcl.size(); j++) {
                        //System.out.println("Client : " + tempcl.get(j));
                        if (codage3[tempcl.get(j)][i - 1] == position) {
                            //System.out.println("Il y a un match");
                            tempco.add(tempcl.get(j));
                            break;
                        }
                    }
                    position = position + 1;
                }
                tempco.add(nclient); //on termine avec le centre de remanufacturing
                hamcy.add(tempco);

            }
        }

        setProcli(prcl);
        setPerfopro(perpro);
        setStock(st);
        setPerfocom(perco);
        setRemcom(remco);
        setRemanufacturabilitymat(rempro);
        setHamiltonian_cycle(hamcy);

    }

    //Calcul de la fonction objectif cout
    public void totalcost(){
        double totalc = 0;
        double remcost = 0;
        double transcost = 0;
        double stocost = 0;
        double pencost = 0;
        double reccost = 0;

        //On commence par calculer le coût de remanufacturing: pour cela on parcourt la liste des performances des composants des produits
        for(int i = 0; i < perfocom.size() -1; i++){
            for(int j = 0; j < perfocom.get(i).size(); j++){
                for(int k = 0; k < perfocom.get(i).get(j).size(); k++){
                    if(perfocom.get(i).get(j).get(k) < perfocom.get(i+1).get(j).get(k)){
                        remcost += products.get(j).com.get(k).importance_c * (perfocom.get(i+1).get(j).get(k) - perfocom.get(i).get(j).get(k));
                    }
                }
            }
        }

        //System.out.println("Le coût de remanufacturing : " + remcost);

        //On calcule le cout total de transport
        for(int i = 0; i < hamiltonian_cycle.size(); i++){
            for(int j = 0; j < hamiltonian_cycle.get(i).size() - 1; j++){
                transcost += mdis[hamiltonian_cycle.get(i).get(j)][hamiltonian_cycle.get(i).get(j+1)];
            }
        }

        //System.out.println("Le coût de tranport : " + transcost);

        //On calcule le coût de stockage
        for(int i = 0; i < horizon + 1; i++){
            for(int j = 0; j < grade; j++){
                stocost += stock[i][j] * (1/(j+1)) * 37;
            }
        }

        //System.out.println("Le coût de stock : " + stocost);

        //On calcule le coût de pénalité
        int occ = 0;
        int pro = 0;
        for(int i = 0; i < nclient; i++){
            occ = 0;
            pro = procli[i][0];
            for(int j = 0; j < horizon +1; j++){
               if(procli[i][j] == pro){
                   occ = occ + 1;
                   if(occ == 5){
                       pencost += 50;
                   }
                   else if (occ > 5){
                       pencost += 10;
                   }
               }
               else{
                   pro = procli[i][j];
                   occ = 0;
               }
            }
        }

        //System.out.println("Le coût de pénalité : " + pencost);

        //On calcule le coût de jeter un produit
        for(int i = 0; i < nproduct; i++){
            for(int j = 0; j < horizon +1; j++){
                if(remanufacturabilitymat[i][j] <= 0){
                    reccost = (horizon - j) * 10;
                    break;
                }
            }
        }

        //System.out.println("Le coût de rec: " + reccost);

        totalc = remcost + transcost + stocost + pencost + reccost;

        //System.out.println("Le coût total: " + totalc);

        setTotal_cost(totalc);
    }

    //Calcul de la fonction objectif impact carbon
    public void totalcfp(){
        double cfp = 0;
        double remcfp = 0;
        double transcfp = 0;
        double stocfp = 0;
        double recfp = 0;
        double usecfp = 0;

        //On commence par calculer l'impact carbone de remanufacturing
        for(int i = 0; i < perfocom.size() -1; i++){
            for(int j = 0; j < perfocom.get(i).size(); j++){
                for(int k = 0; k < perfocom.get(i).get(j).size(); k++){
                    if(perfocom.get(i).get(j).get(k) < perfocom.get(i+1).get(j).get(k)){
                        remcfp += 0.1 * products.get(j).com.get(k).importance_c * (perfocom.get(i+1).get(j).get(k) - perfocom.get(i).get(j).get(k));
                    }
                }
            }
        }
        //System.out.println("L'impact carbone de remanufacturing : " + remcfp);

        //On calcule l'impact carbone total de transport
        for(int i = 0; i < hamiltonian_cycle.size(); i++){
            for(int j = 0; j < hamiltonian_cycle.get(i).size() - 1; j++){
                transcfp += mdisp[hamiltonian_cycle.get(i).get(j)][hamiltonian_cycle.get(i).get(j+1)];
            }
        }

        //System.out.println("L'impact carbone de tranport : " + transcfp);

        //On calcule l'impact carbone de stockage
        for(int i = 0; i < horizon + 1; i++){
            for(int j = 0; j < grade; j++){
                stocfp += stock[i][j] * (1/(j+1)) * 0.001;
            }
        }

        //System.out.println("L'impact carbone de stock : " + stocfp);

        //On calcule l'impact carbone de jeter un produit
        for(int i = 0; i < nproduct; i++){
            for(int j = 0; j < horizon +1; j++){
                if(remanufacturabilitymat[i][j] <= 0){
                    recfp = (horizon - j) * 0.007;
                    break;
                }
            }
        }

        //System.out.println("L'impact carbone de recyclage: " + recfp);

        //Calcul de l'impact carbone de l'utilisation
        for(int i = 0; i < nclient; i++) {
            for(int j = 1; j < horizon +1; j++){
                if(products.get(procli[i][j-1]).total_performance > 0){
                    usecfp += frequencies[i][j-1] * (period[j] - period[j-1]) * 0.0005;
                }
            }
        }

        cfp = remcfp + transcfp + stocfp + recfp;

        //System.out.println("L'impact carbone total: " + cfp);

        setTotal_cfp(cfp);
    }

    //vérifier si il y a des produits disponibles en stock
    public boolean dispostock(){
        boolean av = false;
        int count = 0;
        for(int i = 0; i < nproduct; i++){
            if(products.get(i).state == -1){
                count = count +1;
            }
        }
        if(count > 0){
            av = true;
        }
        return av;
    }


    //Fonction pour passer de la performance au grade
    public int perfograde(double p){
        int g;
        if(p >= 1.){
            g = 0;
        }
        else {
            g = grade - (((int) (grade * p)) + 1);
        }
        return g;
    }


    /*                            Displays                             */

    //Afficher les produits pour savoir si quelque chose cloche
    public void display_product(){

            //System.out.println("Produit id :" + products.get(0).id);
            //System.out.println("Produit type :" + products.get(0).type);
            System.out.println("Nombre composants :" + products.get(0).number_components);
            System.out.println("Nombre fonctionnalités :" + products.get(0).number_features);
            //System.out.println("Produit état :" + products.get(0).state);
            products.get(0).display_comfea();
            products.get(0).display_com();
            products.get(0).display_fea();
            //System.out.println("Performance totale :" + products.get(i).total_performance);
            //System.out.println("Rem totale :" + products.get(i).total_rem);

    }

    //affichage des matrices
    public void display_mdis(){
        System.out.println("Matrice des couts: ");
        for(int i = 0; i < nclient + 1; i++){
            for(int j = 0; j < nclient +1; j++){
                System.out.print(mdis[i][j] + "\t");
            }
            System.out.println();
        }

        System.out.println("Matrice carbone: ");
        for(int i = 0; i < nclient + 1; i++){
            for(int j = 0; j < nclient +1; j++){
                System.out.print(mdisp[i][j] + "\t");
            }
            System.out.println();
        }
    }

    //affichage des grades
    public void display_grade(){
        for(int i = 0; i < grade; i++){
            System.out.print(matgrade[i] + "\t");
        }
        System.out.println();
    }

    //Affichage des codages
    public void display_codage(){
        System.out.println("Codage 1: ");
        for(int i = 0; i < nclient; i++){
            for(int j= 0; j < horizon; j++){
                System.out.print(codage1[i][j] + "\t");
            }
            System.out.println();
        }

        System.out.println("Codage 2: ");
        for(int i = 0; i < nclient; i++){
            for(int j= 0; j < horizon; j++){
                System.out.print(codage2[i][j] + "\t");
            }
            System.out.println();
        }

        System.out.println("Codage 3: ");
        for(int i = 0; i < nclient; i++){
            for(int j= 0; j < horizon; j++){
                System.out.print(codage3[i][j] + "\t");
            }
            System.out.println();
        }
    }

    //affichage des fréquences d'utilisation
    public void display_freq(){
        System.out.println("Fréquence d'utilisation: ");
        for(int i = 0; i < nclient; i++){
            for(int j= 0; j < horizon; j++){
                System.out.print(frequencies[i][j] + "\t");
            }
            System.out.println();
        }
    }

    //Affichage des produits affectés aux clients
    public void display_procli(){
        System.out.println("Affectation des produits aux clients: ");
        for(int i = 0; i < nclient; i++){
            for(int j= 0; j < horizon +1 ; j++){
                System.out.print(procli[i][j] + "\t");
            }
            System.out.println();
        }
    }

    //Affichage des performances des composants
    public void display_percom(){

        System.out.println("Performance des composants: ");
        for(int k = 0; k < perfocom.size(); k++){
            for(int i = 0; i < perfocom.get(k).size(); i++){
                for(int j= 0; j < perfocom.get(k).get(i).size() ; j++){
                    System.out.print(perfocom.get(k).get(i).get(j) + "\t");
                }
                System.out.print(" | ");
            }
            System.out.println();
        }
    }

    //Affichage remanufacturabilité des composants
    public void display_remcom(){

        System.out.println("Remanufacturabilité des composants: ");
        for(int i = 0; i < remcom.size(); i++){
            for(int j = 0; j < remcom.get(i).size(); j++){
                for(int k= 0; k < remcom.get(i).get(j).size() ; k++){
                    System.out.print(remcom.get(i).get(j).get(k) + "\t");
                }
                System.out.print(" | ");
            }
            System.out.println();
        }
    }

    //Affichage performances et remanufacturabilité des produits
    public void display_perfopro(){
        System.out.println("Performance des produits: ");
        for(int i = 0; i < nproduct; i++){
            for(int j= 0; j < horizon +1 ; j++){
                System.out.print(perfopro[i][j] + "\t");
            }
            System.out.println();
        }

        System.out.println("Remanufacturabilité des produits: ");
        for(int i = 0; i < nproduct; i++){
            for(int j= 0; j < horizon +1 ; j++){
                System.out.print(remanufacturabilitymat[i][j] + "\t");
            }
            System.out.println();
        }
    }

    //Affichage de l'état du stock
    public void display_stock(){
        System.out.println("Etat du stock: ");
        for(int i = 0; i < horizon +1; i++){
            for(int j= 0; j < grade ; j++){
                System.out.print(stock[i][j] + "\t");
            }
            System.out.println();
        }
    }

    //Affichage des cycles hamiltoniens
    public void display_hamcy(){
        System.out.println("Cycles hamiltonien: ");
        for(int i = 0; i < hamiltonian_cycle.size(); i++){
            for(int j= 0; j < hamiltonian_cycle.get(i).size() ; j++){
                System.out.print(hamiltonian_cycle.get(i).get(j) + "\t");
            }
            System.out.println();
        }
    }

}
