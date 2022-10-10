package extractData;

import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Logger;
import formatter.FlexibleFormatter;

public class Data {

    public static HashMap<String, JsonObject> multiLanguagesRelationsJsonObject = new HashMap<>();
    public static HashMap<String, HashMap<String, HashMap<String, Integer>>> multiLanguagesRelationsHashMap = new HashMap<>();

    public static Logger verbal = Logger.getLogger(Data.class.getName());


    /*
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
                init_with_txt(new File(txtPath));
            }
            else {
                init_with_json();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }*/

    /*
    private static void init_with_txt(File file) {
        // liste des mots
        List<String> mots = mots(file);

        // pour tous les mots
        for (int i = 0; i < mots.size() - 1; i++) {

            // si le mot existe dans la hashmap
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
        overWriteJSON();
    }
   */


    private static JsonObject getJsonObjectFromFile(File jsonFile){
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(jsonFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        JsonObject result = new Gson().fromJson(br, JsonObject.class);
        if (result == null)
            return new JsonObject();
        return result;
    }

    private static JsonArray getJsonArrayFromFile(File jsonFile){
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(jsonFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        String test;
        try {
            test = br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }




        JsonArray result = new Gson().fromJson(test, JsonArray.class);
        if (result == null)
            return new JsonArray();
        return result;
    }

    private static void updateJsonFileWithTextFile(File jsonFile, File textFile){
        JsonObject start = getJsonObjectFromFile(jsonFile);
        updateJsonObjectWithTextFile(start, textFile);
        overWriteJSON(jsonFile, start.toString());
    }

    private static void updateJsonObjectWithTextFile(JsonObject jsonObject, File textFile) {
        List<String> mots = mots(textFile);
        Gson gson = new Gson();

        for (int i = 0; i < mots.size() - 1; i++) {
            Set<String> mots_json = jsonObject.keySet();

            // si le json contient la clef
            if (mots_json.contains(mots.get(i))) {
                // on mofifie de valeur
                JsonObject current = (JsonObject) jsonObject.get(mots.get(i));

                // si le sous json ne contient pas le mot suivant, on l'ajoute
                if (!current.keySet().contains(mots.get(i + 1))) {
                    current.addProperty(mots.get(i+1), 1);
                }
                // si il le contient, on augmente sa valeur
                else {
                    int temp = current.get(mots.get(i+1)).getAsInt();
                    temp = temp+1;
                    current.addProperty(mots.get(i+1), temp);
                }
            } // si le json ne contient pas la clef, on l'ajoute
            else {
                JsonObject a_ajouter = new JsonObject();
                a_ajouter.addProperty(mots.get(i+1), 1);
                jsonObject.add(mots.get(i), a_ajouter);
            }
        }

    }

    private static List<String> mots(File file) {

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

    private static void overWriteJSON(File jsonFile, String jsonElement){
        Gson gson = new Gson();
        PrintWriter writer;
        try {
            writer = new PrintWriter(jsonFile, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        writer.println(jsonElement);
        writer.close();
    }

    public static List<File> importantPackages(){
        try{
            File folder = new File("src/main/java/data/txt");
            return Arrays.asList(Objects.requireNonNull(folder.listFiles()));
        }catch(Exception e){
            throw new Error("one of the \"txt\" folder's childs is not a directory");
        }
    }

    private static void createListingJson(){
        String resultPath = "src/main/java/data/json/";

        File temp_1;
        File temp_2;

        for (File f : importantPackages()){
            String folderName = f.getName();

            temp_1 = new File(f + "/" + folderName + "_files_used.json");
            try {
                temp_1.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            temp_2 = new File(resultPath + folderName + ".json");
            try {
                temp_2.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void addTexts(){
        List<File> packages = importantPackages();
        for (File file : packages){

            List<File> childs = List.of(file.listFiles());

            String folderName = file.getName();

            File savedFiles = new File(file + "/" + folderName + "_files_used.json");
            JsonArray ja = getJsonArrayFromFile(savedFiles);
            List<File> settedFiles = jsonArrayToList(ja);

            String jsonProbaPath = "src/main/java/data/json/";
            File json_probas_file = new File(jsonProbaPath + folderName + ".json");

            JsonObject jo = getJsonObjectFromFile(json_probas_file);

            for (File child : childs){
                // on s'interesse aux fichiers textes
                if (child.getName().endsWith(".txt")){
                    // si le fichier n'est pas ajoute, on l'ajoute et on update les donnees

                    if (!contient(settedFiles, child)){
                        ja.add(child.toString());
                        updateJsonFileWithTextFile(json_probas_file, child);
                        overWriteJSON(savedFiles, ja.toString());
                    }
                }
                // on ajoute a cet objet tous les fichiers textes non ajoutes au prealable
            }
            JsonObject varToInitArgument = getJsonObjectFromFile(json_probas_file);
            multiLanguagesRelationsJsonObject.put(folderName, varToInitArgument);
        }
    }

    private static boolean contient(List<File> list, File file){
        String file_value = "\""+ file.toString() + "\"";
        for (File f : list){
            String f_value = f.toString();
            if(f_value.equals(file_value)){
                return true;
            }
        }
        return false;
    }




    public static void init(){
        FlexibleFormatter.setLogger(verbal);
        FlexibleFormatter.selectColor(verbal, FlexibleFormatter.Color.GREEN);
        createListingJson();
        verbal.info("creating json files : done");
        addTexts();
        verbal.info("initialising json files : done");
    }

    private static List<File> jsonArrayToList(JsonArray arr){
        LinkedList<File> result = new LinkedList<>();
        for (JsonElement i : arr){
            result.add(new File(String.valueOf(i)));
        }
        return result;
    }

    public static void main(String[] args) {
        double t1 = System.currentTimeMillis();
        init();
        double t2 = System.currentTimeMillis();
        System.out.println("temps d'execution : " + (t2-t1)/1000 + " s");
    }
}
