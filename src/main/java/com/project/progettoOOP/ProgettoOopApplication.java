package com.project.progettoOOP;


import com.project.progettoOOP.model.ServicesCollection;
import com.project.progettoOOP.utils.CSVDownload;
import com.project.progettoOOP.utils.CSVParser;
import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe da cui parte l'esecuzione del programma, avviando il servizio
 */

@SpringBootApplication
public class ProgettoOopApplication {

	public static ServicesCollection servicesStart;
	private static final String FILE_NAME = "data.CSV";

	/**
	 * Funzione che inizializza l'applicazione e la fa partire. Prima di avviare l'applicazione Spring Boot verrà
	 * scaricato il file CSV dal JSON fornito tramite le specifiche del progetto, nel caso in cui questo non sia presente.
	 * @param args Eventuali argomenti passati
	 * @throws Exception Eccezione generica che può avvenire
	 */

	public static void main(String[] args) throws Exception {
		File file = new File(FILE_NAME);
		if (!file.exists()) {
			CSVDownload.getCSV(FILE_NAME);
		}
		servicesStart = CSVParser.parser(FILE_NAME);
		SpringApplication.run(ProgettoOopApplication.class, args);
	}

}
