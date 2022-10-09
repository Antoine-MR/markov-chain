package markov_chain;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import extractData.Data;

import java.util.*;

public class MarkovChain {

    

    private static String generate_recursive(String language, String firstWord, int numberOfWords){

        if (numberOfWords <= 0)
            return "";


        JsonObject relations;
        try{
            relations = Data.multiLanguagesRelationsJsonObject.get(language);
        } catch (Exception e) {
            throw new Error("language " + language + " does not exist.");
        }


        HashMap<ArrayList<Integer>, String> indices_probas = new HashMap<>();


        if(!relations.keySet().contains(firstWord))
            throw new Error("word " + firstWord + " does no exist in data files");


        JsonObject start = (JsonObject) relations.get(firstWord);

        Set<String> s = start.keySet();

        int somme = 0;
        for (String i : s){
            int old_somme = somme+1;
            Integer firstStep = Integer.parseInt(start.get(i).toString());

            somme += firstStep;
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

        return nextWord + " " + generate_recursive(language, nextWord, numberOfWords-1);
    }
    private static String generate(String language, String firstWord, int numberOfWords){
        return firstWord + generate_recursive(language, firstWord, numberOfWords-1);
    }
    public static void main(String[] args) {
        Data.init();
        Scanner sc = new Scanner(System.in);
        String language = "english";
        while (true){
            System.out.println("premier mot : ");
            String response_1 = sc.next();
            System.out.println("nombre de mots : ");
            String response_2 = sc.next();

            System.out.println("\n" + generate(language, response_1, Integer.parseInt(response_2)));
            System.out.println("\n\n");
        }
    }
}
