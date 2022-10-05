package extractData;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public class ExtractData {

    private static HashMap<String, HashMap<String, Integer>> used = new HashMap<>();

    private static List<String> mots(){
        // Le fichier d'entrée
        File file = new File("src/data/data.txt");
        // Créer l'objet File Reader
        FileReader fr = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        // Créer l'objet BufferedReader
        BufferedReader br = new BufferedReader(fr);
        StringBuffer sb = new StringBuffer();
        String line;
        while(true)
        {
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
        return List.of(s.split(" "));
    }
    public static void main(String args[])
    {
        List<String> mots = mots();

        for (int i = 0 ; i < mots.size()-1 ; i++ ){

            if (used.containsKey(mots.get(i))){
                HashMap<String, Integer> current = used.get(mots.get(i));

                if (!current.containsKey(mots.get(i+1))){
                    current.put(mots.get(i+1), 1);
                }

                else {
                    Integer value = current.get(mots.get(i + 1)) + 1;
                    current.put(mots.get(i+1), value);
                }
            }
            else {
                HashMap<String, Integer> a_ajouter =  new HashMap<>();
                a_ajouter.put(mots.get(i+1), 1);
                used.put(mots.get(i), a_ajouter);
            }
        }

        System.out.println(used);
        
    }
}
