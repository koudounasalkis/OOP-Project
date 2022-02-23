package com.project.progettoOOP.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Classe che consente di recuperare le infomazioni da un dato CSV parsando un JSON
 */
public class CSVDownload {

    /**
     * Metodo che scarica un JSON e lo inserisce in un JSON Object
     * @param fileName Nome del file
     * @throws Exception dovuta al metodo di getInputStream()
     */
    public static void getCSV (String fileName) throws Exception {
        URLConnection openConnection = new URL("http://data.europa.eu/euodp/data/api/3/action/package_show?id=cLevOZ15edhZnX3V3NzWbQ").openConnection();
        InputStream in = openConnection.getInputStream();

        String data = "";
        String line = "";

        try {
            InputStreamReader inR = new InputStreamReader(in);
            BufferedReader buf = new BufferedReader(inR);

            while ((line = buf.readLine()) != null) {
                data += line;
            }
        } finally {
            in.close();
        }

        JSONObject obj = new JSONObject(data);
        JSONObject objI = (JSONObject) (obj.get("result"));
        JSONArray objA =  (objI.getJSONArray("resources"));

        for (int i = 0; i < objA.length(); i++) {
            if (objA.getJSONObject(i) instanceof JSONObject) {
                JSONObject o1 = objA.getJSONObject(i);
                String format = (String) o1.get("format");
                String urlD = (String) o1.get("url");
                if (format.contains("CSV")){
                    downloadFile (urlD, fileName);
                }
            }
        }
    }

    /**
     * Metodo che si connette all'URL e scarica il JSON nel file
     * @param url Identifica il JSON
     * @param fileName Nome indicante il file dove verrà salvato il JSON
     * @throws Exception dovuta al metodo copy
     */
    private static void downloadFile (String url, String fileName) throws Exception {
        File file = new File(fileName);
        if (!file.exists()) {
            try (InputStream in = URI.create(url).toURL().openStream()) {
                Files.copy(in, Paths.get(fileName));
            }
        }
    }

}
