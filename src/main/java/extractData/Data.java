package extractData;

import java.io.*;
import java.util.*;

public class Data {

    public static void init () {
        init_used(false);
    }

    public static final HashMap<String, HashMap<String, Integer>> relations = new HashMap<>();

    private static void init_used(boolean json) {
        List<String> mots = mots();

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
    }

    private static List<String> mots() {
        // Le fichier d'entrée
        File file = new File("src/main/java/data/data.txt");
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
}
