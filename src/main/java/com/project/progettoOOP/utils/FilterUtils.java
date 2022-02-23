package com.project.progettoOOP.utils;

import com.project.progettoOOP.model.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


/**
 * Classe che permette di filtrare i dati, prestando attenzione ai particolari filtri desiderati
 * @param <T> Tipo generico
 */
public class FilterUtils<T> {

    /**
     * Metodo che determina se ogni oggetto della collezione deve essere o meno considerato
     * nella nuova collezione di dati filtrata, a seconda del particolare filtro scelto
     * @param value Oggetto che viene considerato nel test
     * @param operator Condizione del filtro
     * @param obj Oggetti che caratterizzano il filtro
     * @return Un booleano che determina se l'oggetto deve o non deve essere considerato nella collezione "filtrata" di dati
     */
    public static boolean check(Object value, String operator, Object... obj) {

        if (obj.length == 1 && obj[0] instanceof Number && value instanceof Number) {

            Float objFloat = ((Number)obj[0]).floatValue();
            Float valueFloat = ((Number)value).floatValue();

            if (operator.equals("$eq"))
                return value.equals(objFloat);
            else if (operator.equals("$not"))
                return !value.equals(objFloat);
            else if (operator.equals("$lt"))
                return valueFloat < objFloat;
            else if (operator.equals("$lte"))
                return valueFloat <= objFloat;
            else if (operator.equals("$gt"))
                return valueFloat > objFloat;
            else if (operator.equals("$gte"))
                return valueFloat >= objFloat;

        } else if(obj.length == 1 && obj[0] instanceof String && value instanceof String) {

            if(operator.equals("$eq") || operator.equals("$in"))
                return value.equals(obj[0]);
            else if(operator.equals("$not") || operator.equals("$nin"))
                return !value.equals(obj[0]);

        } else if(obj.length > 1) {

            if (operator.equals("$bt")) {
                if(obj.length == 2 && obj[0] instanceof Number && obj[1] instanceof Number) {
                    Float min = ((Number)obj[0]).floatValue();
                    Float max = ((Number)obj[1]).floatValue();
                    Float valueF = ((Number)value).floatValue();
                    return valueF >= min && valueF <= max;
                }
            }
            else if (operator.equals("$in"))
                return Arrays.asList(obj).contains(value);
            else if (operator.equals("$nin")){
                return !Arrays.asList(obj).contains(value);
            }
        }

        return false;
    }

    /**
     * Metodo che riceve la collezione di oggetti ed il filtro,
     * e restituisce una collezione contenente soltanto gli oggetti selezionati (e dunque filtrati)
     * @param src L'intera collezione di oggetti
     * @param fieldName Campo su cui opera il filtro
     * @param operator Condizione del filtro
     * @param value Oggetti che caratterizzano il filtro
     * @return Collezione dei dati filtrati
     */
   public Collection<Service> select(Collection<Service> src, String fieldName, String operator, Object... value) {
        Collection<Service> out = new ArrayList<>();
        for(Service item : src) {
            try {
                Method m = null;
                if(isInteger(fieldName)) {
                    m = item.getClass().getMethod("getMisureOfTheYear", int.class);
                } else {
                    m = item.getClass().getMethod("get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1),null);
                }
                try {
                    Object tmp = null;
                    if(isInteger(fieldName)) {
                        tmp = m.invoke(item, Integer.parseInt(fieldName));
                    } else {
                        tmp = m.invoke(item);
                    }
                    if(FilterUtils.check(tmp, operator, value))
                        out.add(item);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        return out;
    }

    /**
     * Metodo che stabilisce se l'argomento in ingresso può essere parsato come intero o meno
     * @param fieldName Stringa su cui viene valutata la possibilità di fare il parsing
     * @return Restituisce un booleano per esprimere l'esito del metodo
     */
    private static boolean isInteger(String fieldName) {
        try {
            Integer.parseInt(fieldName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
