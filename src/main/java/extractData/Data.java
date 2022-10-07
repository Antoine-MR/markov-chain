package extractData;

import com.google.gson.*;

import java.io.*;
import java.util.*;

public class Data {

    public static String txtName = "demo.txt";
    public static String jsonName = "probabilities.json";
    public static String txtPath = "src/main/java/data/txt/english/"+ txtName;
    public static String jsonPath = "src/main/java/data/" + jsonName;

    public static final JsonObject json_relations = new JsonObject();

    public static final HashMap<String, HashMap<String, Integer>> relations = new HashMap<>();

    public static JsonObject getJson(){
        File file = new File(jsonPath);
        FileReader reader;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new Gson();
        JsonObject obj = gson.fromJson(new BufferedReader(reader), JsonObject.class);
        return obj;
    }

    public static void init () {
        File file = new File(jsonPath);
        FileReader reader;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            if (new BufferedReader(reader).readLine() == null){
                init_with_txt();
            }
            else {
                init_with_json();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void init_with_txt() {
        List<String> mots = mots();
        // JsonArray json_mots =

        for (int i = 0; i < mots.size() - 1; i++) {

            if (relations.containsKey(mots.get(i))) {

                HashMap<String, Integer> current = relations.get(mots.get(i));

                if (!current.containsKey(mots.get(i + 1))) {
                    current.put(mots.get(i + 1), 1);
                } else {
                    Integer value = current.get(mots.get(i + 1)) + 1;
                    current.put(mots.get(i + 1), value);
                }
            } else {
                HashMap<String, Integer> a_ajouter = new HashMap<>();
                a_ajouter.put(mots.get(i + 1), 1);
                relations.put(mots.get(i), a_ajouter);
            }
        }
        writeJSON();
    }

    private static void init_with_json(){
        Gson gson = new Gson();
        JsonObject obj = getJson();
        Set<Map.Entry<String, JsonElement>> s = obj.entrySet();
        //System.out.println(s);
    }

    private static List<String> mots() {
        // Le fichier d'entrée
        File file = new File(txtPath);
        // Créer l'objet File Reader
        FileReader fr;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        // Créer l'objet BufferedReader
        BufferedReader br = new BufferedReader(fr);
        StringBuilder sb = new StringBuilder();
        String line;
        while (true) {
            try {
                if ((line = br.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // ajoute la ligne au buffer
            sb.append(line);
            sb.append("\n");
        }
        try {
            fr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String s = sb.toString();
        // TODO changer le regex pour que le split se fasse sur les epscaes et les virgules
        return List.of(s.split("[ ;]"));
    }

    private static void writeJSON(){

        Gson gson = new Gson();
        PrintWriter writer;
        try {
            writer = new PrintWriter(jsonPath, "UTF-8");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String jo = gson.toJson(relations);
        writer.println(jo);
        writer.close();
    }

    public static void main(String[] args) {
        init();
        //writeJSON();
    }
}
