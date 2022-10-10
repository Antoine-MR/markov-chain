package markov_chain;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import extractData.Data;
import formatter.FlexibleFormatter;

import java.util.*;
import java.util.logging.Logger;

public class MarkovChain {

    public static Logger verbal = Logger.getLogger(MarkovChain.class.getName());

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


        // conditions pour corriger le bug du dernier mot
        if(!relations.keySet().contains(firstWord))
            firstWord = firstWord.substring(0,firstWord.length()-1);

        if(!relations.keySet().contains(firstWord))
            throw new Error("word " + firstWord + " does no exist in the database");


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
        return firstWord +  " " + generate_recursive(language, firstWord, numberOfWords-1);
    }

    private static boolean existingWord(String language, String word){
        return Data.multiLanguagesRelationsJsonObject.get(language).keySet().contains(word);
    }

    private static void verbalSetup(){
        FlexibleFormatter.setLogger(verbal);
        FlexibleFormatter.selectColor(verbal, FlexibleFormatter.Color.BLUE);
    }
    private static void demo(){
        Data.init();
        Scanner sc = new Scanner(System.in);
        String language = "english";
        while (true){
            boolean b = false;
            String response_1;
            do{
                System.out.println("first word : ");
                response_1 = sc.next();
                if(existingWord(language, response_1)){
                    b = true;
                }else {
                    System.out.println("the word " + response_1 + " does not exist in the database, try again : \n");
                }
            }while (!b);


            System.out.println("nombre de mots : ");
            String response_2 = sc.next();
            double t1 = System.currentTimeMillis();
            verbal.info("generating text... \n");
            System.out.println("\n" + generate(language, response_1, Integer.parseInt(response_2)));
            double t2 = System.currentTimeMillis();
            System.out.println("\n\n");
            System.out.println("generated in : " + (t2-t1)/1000 + " seconds");
            System.out.println("\n\n");
        }
    }
    public static void main(String[] args) {
        verbalSetup();
        Data.init();

        // demo();

        JsonObject jo = Data.multiLanguagesRelationsJsonObject.get("english");


        for (int i = 0 ; i < 1000 ; i++){
            System.out.println(generate("english", "I", 2000) + "\n\n\n");
        }


    }
}
