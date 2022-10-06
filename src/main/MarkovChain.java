package main;

import extractData.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class MarkovChain {

    // private HashMap<String, Integer> indices_probas = new HashMap<>();


    private static String generate_recursive(String firstWord, int numberOfWords){

        if (numberOfWords <= 0)
            return "";

        HashMap<ArrayList<Integer>, String> indices_probas = new HashMap<>();

        if(!Data.relations.containsKey(firstWord))
            throw new Error("le mot " + firstWord + " n'existe pas dans les données");

        HashMap<String, Integer> start = Data.relations.get(firstWord);

        Set<String> s = Data.relations.get(firstWord).keySet();

        int somme = 0;
        for (String i : s){
            int old_somme = somme+1;
            somme += Data.relations.get(firstWord).get(i)+1;
            ArrayList<Integer> intervalle = new ArrayList<>();
            intervalle.add(old_somme);
            intervalle.add(somme);
            indices_probas.put(intervalle, i);
        }
        int min = 1;
        int max = somme;

        int rand = (int) (min + (Math.random() * (max+1 - min)));

        String nextWord = "";

        for (ArrayList<Integer> i : indices_probas.keySet()){
            if(rand >= i.get(0) && rand <= i.get(1)){
                nextWord = indices_probas.get(i);
            }
        }

        return nextWord + " " + generate_recursive(nextWord, numberOfWords-1);
    }

    private static String generate(String firstWord, int numberOfWords){
        return firstWord + generate_recursive(firstWord, numberOfWords-1);
    }
    public static void main(String[] args) {
        Data.init();
        Scanner sc = new Scanner(System.in);

        while (true){
            System.out.println("premier mot : ");
            String response_1 = sc.next();
            System.out.println("nombre de mots : ");
            String response_2 = sc.next();

            System.out.println("\n" + generate(response_1, Integer.parseInt(response_2)));
            System.out.println("\n\n");
        }
    }
}
