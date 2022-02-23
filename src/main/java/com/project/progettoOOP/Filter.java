package com.project.progettoOOP;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * Intefaccia che consente di implementare la dichiarazione del metodo filterField, che serve per poter filtrare dati
 * @param <E> Tipo dell'oggetto da filtrare
 * @param <T> Tipo del valore da utilizzare per filtrare l'oggetto
 */
public interface Filter<E,T> {

    /**
     * Metodo astratto che viene implementato per filtrare la collezione di elementi
     * rispettivamente ad un campo, un operatore ed un insieme di valori
     * @param fieldName Nome del campo da filtrare
     * @param operator Operatore del filtro
     * @param value Valori su cui viene eseguito il test per il filtro
     * @return Collezione degli elementi fltrata mediante i parametri specificati
     * @throws NoSuchMethodException: Questa eccezione viene lanciata quando un particolare metodo non viene trovato
     * @throws IllegalAccessException: Questa eccezione viene lanciata quando un'applicazione tenta di creare un'istanza in maniera riflessiva
     * @throws InvocationTargetException: Questa eccezione controllata racchiude un'eccezione generata da un metodo o un costruttore richiamato
     */
    abstract Collection<E> filterField(String fieldName, String operator, Object... value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}
