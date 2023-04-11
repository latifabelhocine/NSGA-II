package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Scanner sc = new Scanner(System.in);
        NSGA inst = new NSGA();
        inst.genoneind();
        /*inst.gennumpop();
        inst.firstpopgen();
        inst.NSGALGO();*/
        double p1, p2;


        int c = 1;
        int a;
        int t;
        int sol;
        //Scanner sc = new Scanner(System.in);

        while (c == 1){
            a = 1;
            t = 1;
            inst.gennumpop();
            inst.firstpopgen();
            inst.NSGALGO();
            inst.displaypareto();
            while(t == 1) {
                System.out.println("Paramètres de TOPSIS :");
                p1 = sc.nextDouble();
                p2 = sc.nextDouble();
                int[] top = inst.TOPSIS(p1, p2);
                System.out.println("TOPSIS ORDER: ");
                for (int j = 0; j < top.length; j++) {
                    System.out.print("\t" + top[j]);
                }
                System.out.println("Voulez-vous afficher encore appliquer TOPSIS ? (1/0)");
                t = sc.nextInt();
            }
            System.out.println("Voulez-vous afficher une solution ? (1/0)");
            a = sc.nextInt();
            while(a == 1){
                System.out.println("Quelle solution voulez-vous afficher ? ");
                sol = sc.nextInt();
                inst.population.get(sol).display_codage();
                inst.population.get(sol).display_procli();
                inst.population.get(sol).display_perfopro();
                inst.population.get(sol).display_percom();
                inst.population.get(sol).display_stock();
                inst.population.get(sol).display_hamcy();
                System.out.println("Fitness de " + sol + ": " + inst.fitness.get(sol));
                System.out.println("Voulez-vous afficher une solution ? (1/0)");
                a = sc.nextInt();
            }
            inst.freepop();
            System.out.println("Voulez-vous continuer ? (1/0)");
            c = sc.nextInt();
        }
    }
}
