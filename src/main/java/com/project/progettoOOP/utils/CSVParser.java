package com.project.progettoOOP.utils;

import com.project.progettoOOP.model.Service;
import com.project.progettoOOP.model.ServicesCollection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Classe che fornisce il metodo per parsare il file CSV (che è stato scaricato)
 */
public class CSVParser {
    private static String COMMA_DELIMITER_1 = ";";
    private static String COMMA_DELIMITER_2 = "\\s?,\\s?";

    /**
     * Metodo per parsare il file CSV già scaricato e salvato e ritorna le informazioni in una collezione di dati
     * @param fileName Contiene le informazioni scaricate
     * @return Restituisce la collezione di oggetti
     */
    public static ServicesCollection parser (String fileName) {
        List<List<String>> record = new ArrayList<>();
        ArrayList<Service> array = new ArrayList();
        ServicesCollection services = new ServicesCollection(array);
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER_1);
                String[] values1 = values[3].split(COMMA_DELIMITER_2);
                Float[] values2 = new Float[values1.length - 1];

                for (int i = 1; i < values1.length; i++) {
                    values2[i - 1] = Float.parseFloat(values1[i]);
                }

                record.add(Arrays.asList(values));
                if (!values1[0].equals("TOTAL")) {
                    services.add(new Service(values[0], values[1], values[2], values1[0], values2));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return services;
        }
    }

}
