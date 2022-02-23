package com.project.progettoOOP.Controllers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.*;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.project.progettoOOP.model.Service;
import com.project.progettoOOP.model.ServicesCollection;
import com.project.progettoOOP.utils.CSVParser;
import com.project.progettoOOP.utils.Statistics;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.expression.ExpressionInvocationTargetException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

import static com.project.progettoOOP.ProgettoOopApplication.servicesStart;


/**
 * Controller che gestisce tutte le richieste GET e POST
 */
@RestController
public class JsonController {
    private ServicesCollection source = servicesStart;

    /**
     * Path che mostra i metadati relativi ad ogni oggetto del dataset
     * @return Restituisce i metadati, sotto forma di JSON
     */
    @RequestMapping(value = "/metadata", method = RequestMethod.GET, produces = "application/json")
    public String getMetadataOfJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator(mapper);
            JsonSchema jsonSchema = jsonSchemaGenerator.generateSchema(Service.class);
            return mapper.writeValueAsString(jsonSchema);
            } catch (Exception e) {
                return makeErrorMex("Unexpected Error");
            }
    }

    /**
     * Path che mostra i dati recuperati dal CSV, sotto forma di JSON
     * @return Restituisce i dati, sotto forma di JSON
     * @throws ExpressionInvocationTargetException: Questa eccezione racchiude (come causa) un'eccezione generata da un metodo invocato da SpEL
     */
    @RequestMapping(value = "/data", method = RequestMethod.GET, produces = "application/json")
    public ArrayList<Service> getDataOfJson()  throws ExpressionInvocationTargetException {

            return servicesStart;
    }

    /**
     * Path che mostra le statistiche (su base annua) su tutti i dati, permettendo di scegliere eventualmente un determinato anno
     * @param year Anno su cui si vogliono calcolare le statistiche
     * @return Restituisce le statistiche
     * @throws JSONException: Questa eccezione viene lanciata per indicare un problema con la JSON API
     * @throws IOException: Questa eccezione viene lanciata quande accade una eccezione I/O di qualche tipo
     */
    @RequestMapping(value = "/statistics", method = RequestMethod.GET, produces = "application/json")
    public String getStatistics
            (@RequestParam(value = "year", required = false) Integer year) throws JSONException, IOException {
        ArrayList<Statistics> allStats = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        for (int i = 2000; i <= 2017; i++) {
            if (year != null) {
                if (year < 2000 || year > 2017)
                    return  makeErrorMex("The year you chose doesn't match. Try again!");
                if (i == year) {
                    allStats.add(new Statistics(i, servicesStart));
                }
            } else {
                allStats.add(new Statistics(i, servicesStart));
            }
        }
        return mapper.writeValueAsString(allStats);
    }

    /**
     * Path che permette di calcolare il numero di occorrenze di una determinata entità geo-politica
     * @param geo Paese di cui si vogliono calcolare le occorrenze
     * @return Restituisce il numero di occorrenze del particolare Paese inserito
     */
    @RequestMapping(value = "/statistics/geo/{geo}", method = RequestMethod.GET, produces="application/json")
    String getStats1 (@PathVariable("geo") String geo) {
        try {
            if (geo != null && (geo.length() == 2 || geo.equals("EU28"))) {
                ServicesCollection obj = CSVParser.parser("data.CSV");
                ObjectMapper mapper = new ObjectMapper();
                List<Service> founded = null;
                for (Service s : obj) {
                    founded = obj.stream().filter(service -> service.getGeo().equals(geo)).collect(Collectors.toList());
                }
                String jsonString = mapper.writeValueAsString(founded);
                return "Number of occurrences of " + geo + ": " + founded.size();
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }catch (RuntimeException e) {
            e.printStackTrace();
        }
        return makeErrorMex("The country you chose doesn't match. Try again!");
    }

    /**
     * Path che permette di calcolare il numero di occorrenze di un determinato obiettivo
     * @param obj Obiettivo (Servizio in cui sono stati investiti determinati fondi) di cui si vogliono calcolare le occorrenze
     * @return Restituisce il numero di occorrenze del determinato Obiettivo inserito
     */
    @RequestMapping(value = "/statistics/objective/{obj}", method = RequestMethod.GET, produces="application/json")
    String getStats2(@PathVariable("obj") String obj) {
        try {
            if (obj != null && (obj.length() == 5 || obj.equals("OTH"))) {
                ServicesCollection object = CSVParser.parser("data.CSV");
                ObjectMapper mapper = new ObjectMapper();
                List<Service> founded = null;
                for (Service s : object) {
                    founded = object.stream().filter(service -> service.getObjective().equals(obj)).collect(Collectors.toList());
                }
                String jsonString = mapper.writeValueAsString(founded);
                return "Number of occurrences of " + obj + ": " + founded.size();
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return makeErrorMex("The objective you chose doesn't match. Try again!");
    }

    /**
     * Path che mostra i dati, eventulmente filtrati, sotto forma di JSON
     * @param filter Filtro in base al quale si vogliono filtrare i dati
     * @return Restituisce un JSON contenente i dati, eventualmente filtrati se sono stati inseriti dei filtri
     * @throws NoSuchMethodException: Questa eccezione viene lanciata quando un particolare metodo non viene trovato
     * @throws IllegalAccessException: Questa eccezione viene lanciata quando un'applicazione tenta di creare un'istanza in maniera riflessiva
     * @throws InvocationTargetException: Questa eccezione controllata racchiude un'eccezione generata da un metodo o un costruttore richiamato
     */
    @RequestMapping(value = "/data/filtered", method = RequestMethod.POST, produces = "application/json")
    public ArrayList<Service> getFilteredData(
            @RequestBody(required = false) String filter)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        ArrayList<Service> filtered = null;

        if (filter != null) {
            try {
                JSONObject obj = new JSONObject(filter);
                String operator = (String) obj.keys().next();
                if((filter.indexOf("$or") > 1) || (filter.indexOf("$and") > 1) || (filter.indexOf("or") + filter.indexOf("$and") >= 2)){
                    ServicesCollection servicesCollection = new ServicesCollection(source);
                    JSONArray jsonArray = obj.getJSONArray(operator);
                    ArrayList<ArrayList<Service>> arrayListArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ArrayList<Service> tmp = new ArrayList<>();
                        tmp = filterControl(source, jsonArray.getJSONObject(i));
                        arrayListArrayList.add(tmp);
                    }if(operator.contains("$or")){
                        filtered = servicesCollection.or(arrayListArrayList);
                    }else{
                        filtered = servicesCollection.and(arrayListArrayList);
                    }
                } else{
                    filtered = filterControl(source, obj);
                }
            } catch (ClassCastException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Values' format is wrong");

            } catch (JSONException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"JSON might not be written correctly");
            }
        }
        if (filtered != null) {
            return filtered;
        } else {
            return source;
        }
    }

    /**
     * Path che mostra le statistiche (su base annua) sui dati (eventualmente filtrati), permettendo di scegliere eventualmente un determinato anno
     * @param year Anno su cui si vogliono (eventualmente) calcolare le statistiche
     * @param filter Filtri che si vogliono (eventualmente) applicare sul dataset prima di calcolare le statistiche
     * @return Restituisce le statistiche, eventualmente filtrate se sono stati inseriti dei filtri
     * @throws IOException: Questa eccezione viene lanciata quande accade una eccezione I/O di qualche tipo
     * @throws InvocationTargetException: Questa eccezione controllata racchiude un'eccezione generata da un metodo o un costruttore richiamato
     * @throws NoSuchMethodException: Questa eccezione viene lanciata quando un particolare metodo non viene trovato
     * @throws ParseException: Questa eccezione segnala che un errore imprevisto si è verificato durante il parsing
     * @throws JSONException: Questa eccezione viene lanciata per indicare un problema con la JSON API
     * @throws IllegalAccessException: Questa eccezione viene lanciata quando un'applicazione tenta di creare un'istanza in maniera riflessiva
     */
    @RequestMapping(value = "/statistics/filtered", method = RequestMethod.POST, produces = "application/json")
    public String getFilteredStatistics
            (@RequestParam(value = "year", required = false) Integer year,
            @RequestBody(required = false) String filter) throws IOException, InvocationTargetException, NoSuchMethodException, ParseException, JSONException, IllegalAccessException {

        ArrayList<Statistics> filteredStats = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        ArrayList<Service> data = getFilteredData(filter);
        for (int i = 2000; i <= 2017; i++) {
            if (year != null) {
                if (year < 2000 || year > 2017)
                    return  makeErrorMex("The year you chose doesn't match. Try again!");
                if (i == year) {
                    filteredStats.add(new Statistics(i, data));
                }
            } else {
                filteredStats.add(new Statistics(i, data));
            }
        }
        return mapper.writeValueAsString(filteredStats);
    }


    /**
     * Metodo che consente di filtrare i contenuti del dataset in base ai filtri specificati
     * @param arrayList Collezione di dati su cui si intendono applicare i filtri
     * @param userRequest Filtri specificati dall'utente all'interno di un JSON object
     * @return Restituisce la collezione di dati filtrati
     * @throws JSONException: Questa eccezione viene lanciata per indicare un problema con la JSON API
     */
    private ArrayList<Service> filterControl(ArrayList<Service> arrayList, JSONObject userRequest) throws JSONException {
        String field = (String) userRequest.keys().next();
        ServicesCollection servicesCollection = new ServicesCollection(arrayList);
        while (userRequest.keys().hasNext()) {
            if (field.contains("$or")) {
                JSONObject newJson = userRequest;
                JSONArray jsonArray = newJson.getJSONArray(field);
                ArrayList<ArrayList<Service>> arrayListArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    String newField = (String) jsonArray.getJSONObject(i).keys().next();
                    String operator = (String) jsonArray.getJSONObject(i).getJSONObject(newField).keys().next();
                    ArrayList<Service> tmp = new ArrayList<>();
                    tmp = switchOperator( servicesCollection, jsonArray.getJSONObject(i).getJSONObject(newField), operator, newField);
                    arrayListArrayList.add(tmp);
                }
                return servicesCollection.or(arrayListArrayList);
            } else if (field.contains("$and")) {
                JSONObject newJson = userRequest;
                JSONArray jsonArray = newJson.getJSONArray(field);
                ArrayList<ArrayList<Service>> arrayListArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    String newField = (String) jsonArray.getJSONObject(i).keys().next();
                    String operator = (String) jsonArray.getJSONObject(i).getJSONObject(newField).keys().next();
                    ArrayList<Service> tmp = new ArrayList<>();
                    tmp = switchOperator(servicesCollection, jsonArray.getJSONObject(i).getJSONObject(newField), operator, newField);
                    arrayListArrayList.add(tmp);
                }
                return servicesCollection.and(arrayListArrayList);
            } else {
                JSONObject newJson = userRequest.getJSONObject(field);
                String operator = (String) newJson.keys().next();
                ArrayList<Service> tmp = new ArrayList<>();
                tmp = switchOperator(servicesCollection, newJson, operator, field);
                return tmp;
            }

        }
        return servicesCollection.getServicesList();
    }

    /**
     * Metodo che consente di gestire gli operatori logici o condizionali attraverso uno switch
     * @param servicesCollection Collezione di dati su cui si intendono valutare gli operatori
     * @param newJson JSON object passato dal filterControl
     * @param operator Operatore in questione
     * @param field Campo su cui si vuole specificare la condizione del filtro
     * @return Restituisce la collezione di dati filtrati
     * @throws JSONException: Questa eccezione viene lanciata per indicare un problema con la JSON API
     */
    private ArrayList<Service> switchOperator(ServicesCollection servicesCollection, JSONObject newJson, String operator, String field)
            throws JSONException {
        switch (operator) {
            case "$bt":
                Object min = newJson.getJSONArray(operator).get(0);
                Object max = newJson.getJSONArray(operator).get(1);
                return (ArrayList<Service>) servicesCollection.filterField(field, operator, min, max);
            case "$in":
            case "$nin":
                Object[] values = new Object[newJson.getJSONArray(operator).length()];
                for (int i = 0; i < newJson.getJSONArray(operator).length(); i++) {
                    values[i] = newJson.getJSONArray(operator).get(i);
                }
                return (ArrayList<Service>) servicesCollection.filterField(field, operator, values);
            case "$eq":
            case "$not":
            case "$lt":
            case "$lte":
            case "$gt":
            case "$gte":
                Object value = newJson.get(operator);
                return (ArrayList<Service>) servicesCollection.filterField(field, operator, value);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The operator you chose might not be written correctly");
        }

    }

    /**
     * Metodo che consente di mostrare un messaggio di errore sotto forma di JSON
     * @param error Testo del messaggio di errore
     * @return Restituisce il JSON contenente l'errore
     */
    private String makeErrorMex(String error) {
        try {
            ObjectMapper map = new ObjectMapper();
            return map.writeValueAsString(error);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
