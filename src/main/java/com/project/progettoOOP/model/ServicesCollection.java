package com.project.progettoOOP.model;

import com.project.progettoOOP.Filter;
import com.project.progettoOOP.utils.FilterUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe che raggruppa tutti gli oggetti "Service" in una collezione
 */
public class ServicesCollection extends  ArrayList<Service> implements Filter<Service, Object> {
    private ArrayList<Service> servicesList;
    private FilterUtils<Service> utils;

    /**
     * Costruttore dell'oggetto:
     * @param servicesList ArrayList di "Service"
     * @param utils serve per accedere ai metodi di FilterUtils
     */
    public ServicesCollection(ArrayList<Service> servicesList, FilterUtils<Service> utils) {
        super();
        this.servicesList = servicesList;
        this.utils = utils;
    }

    /**
     * Costruttore dell'oggetto:
     * @param servicesList ArrayList di "Servicce"
     */
    public ServicesCollection(ArrayList<Service> servicesList) {
        super();
        this.servicesList = servicesList;
        this.utils = new FilterUtils<Service>();
    }

    /**
     * Metodo che restituisce il contenuto della collezione
     * @return Collezione di oggetti "Service"
     */
    public ArrayList<Service> getServicesList() {
        return servicesList;
    }

    /**
     * Metodo capace di creare e "settare" una nuova collezione di {@link Service}
     * @param servicesList Oggetti "Service" da inserire
     */
    public void setServicesList(ArrayList<Service> servicesList) {
        this.servicesList = servicesList;
    }

    /**
     * Metodo che implementa la logica OR (unione) su di un gruppo di insiemi di oggetti
     * @param collection Contiene il gruppo di insiemi di oggetti "Service"
     * @return Restituisce l'insieme di oggetti ottenuto tramite unione
     */
    public ArrayList<Service> or (ArrayList<ArrayList<Service>> collection) {
        Set<Service> set = new HashSet<>();
        for (ArrayList<Service> item : collection)
            set.addAll(item);
        return new ArrayList<>(set);
    }

    /**
     * Metodo che implementa la logica AND (intersezione) su di un gruppo di insiemi di oggetti
     * @param collection Contiene il gruppo di insiemi di oggetti "Service"
     * @return Restituisce l'insieme di oggetti ottenuto tramite intersezione
     */
    public ArrayList<Service> and (ArrayList<ArrayList<Service>> collection) {
        ArrayList<Service> total = new ArrayList<>();
        for(int i = 0; i < collection.size(); i++) {
            for(Service t : collection.get(i)){
                boolean included = true;
                for(ArrayList<Service> itemToCompare : collection) {
                    if(!itemToCompare.contains(t)) {
                        included = false;
                        break;
                    }
                }
                if(included && !total.contains(t))
                    total.add(t);
            }
        }
        return total;
    }


    /**
     * Metodo che permette di applicare i filtri desiderati sulla collezione di oggetti Service
     * @param fieldName Campo su cui vi vuole applicare la condizione del filtro
     * @param operator Condizione del filtro
     * @param value Valori che caratterizzano la condizione del filtro
     * @return La collezione di oggetti filtrata
     */
    @Override
    public Collection<Service> filterField(String fieldName, String operator, Object... value) {
        return utils.select(this.getServicesList(), fieldName, operator, value);
    }

}


