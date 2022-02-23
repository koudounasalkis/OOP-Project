package com.project.progettoOOP.utils;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.progettoOOP.model.Service;
import com.project.progettoOOP.model.ServicesCollection;

import java.io.Serializable;
import java.util.List;

/**
 * La classe Statistics crea i metodi con i quali è possibile andare a calcolare le varie statistiche sui dati
 */
public class Statistics implements Serializable {
    /**
     * Statistica calcolante la media
     */
    private float avg;

    /**
     * Statistica calcolante il minimo
     */
    private float min;

    /**
     * Statistica calcolante il massimo
     */
    private float max;

    /**
     * Statistica calcolante la deviazioen standard
     */
    private float dev_std;

    /**
     * Statistica calcolante la somma
     */
    private float sum;

    @JsonIgnore
    private List<Service> servicesList;


    /**
     * Costruttore della classe:
     * @param year Anno del quale vengono valutate le statistiche
     * @param servicesCollection Collezione di dati presa in considerazione nel calcolo delle statistiche
     */
    public Statistics(int year, List<Service> servicesCollection) {
        servicesList = servicesCollection;
        avg = getMeanOfTheYear(year);
        max = getMaxOfTheYear(year);
        min = getMinOfTheYear(year);
        dev_std = (float) getDevStdOfTheYear(year);
        sum = getSumOfTheYear(year);
    }

    /**
         * Metodo che calcola la media su base annua
         * @param year Specifica l'anno sul quale si vuole effettuare la media degli investimenti
         * @return Restituisce la media
         */
    private float getMeanOfTheYear(int year) {
        for (Service s : servicesList) {
            avg += s.getMisureOfTheYear(year);
        }
        avg /= servicesList.size();
        return avg;
    }

    /**
     * Metodo che trova il valore massimo su base annua
     * @param year Specifica l'anno sul quale si vuole calcolare il massimo degli investimenti
     * @return Restituisce il valore massimo
     */
    private float getMaxOfTheYear(int year) {
        for (Service s : servicesList) {
            float temp = s.getMisureOfTheYear(year);
            if (temp > max) {
                max = temp;
            }
        }
        return max;
    }

    /**
     * Metodo che trova il valore minimo su base annua
     * @param year Specifica l'anno sul quale si vuole calcolare il minimo degli investimenti
     * @return Restituisce il valore minimo
     */
    private float getMinOfTheYear(int year) {
        for (Service s : servicesList) {
            float temp = s.getMisureOfTheYear(year);
            if (temp < min) {
                min = temp;
            }
        }
        return min;
    }

    /**
     * Metodo che calcola la deviazione standard su base annua
     * @param year Specifica l'anno sul quale si vuole effettuare la deviazione standard degli investimenti
     * @return Restituisce la deviazione standard
     */
    private double getDevStdOfTheYear(int year) {
        for (Service s : servicesList) {
            double temp = s.getMisureOfTheYear(year) - getMeanOfTheYear(year);
            dev_std += (float) Math.pow(temp, 2);
        }
        return dev_std = (float) Math.sqrt(dev_std) / servicesList.size();
    }

    /**
     * Metodo che calcola la somma di tutti i valori su base annua
     * @param year Specifica l'anno sul quale si vuole effettuare la somma degli investimenti
     * @return Restituisce la somma
     */
    private float getSumOfTheYear(int year) {
        for (Service s : servicesList) {
            sum += s.getMisureOfTheYear(year);
        }
        return sum;
    }

    /**
     * Metodo che ritorna la media degli investimenti
     * @return Media degli investimenti
     */
    public float getAvg() { return avg; }

    /**
     * Metodo che consente di settare la media degli investimenti
     * @param avg Media che si intende settare
     */
    public void setAvg(float avg) { this.avg = avg; }

    /**
     * Metodo che ritorna il minimo degli investimenti
     * @return Minimo degli investimenti
     */
    public float getMin() { return min; }

    /**
     * Metodo che consente di settare il minimo degli investimenti
     * @param min Minimo che si intende settare
     */
    public void setMin(float min) { this.min = min; }

    /**
     * Metodo che ritorna il massimo degli investimenti
     * @return Massimo degli investimenti
     */
    public float getMax() { return max; }

    /**
     * Metodo che consente di settare il massimo degli investimenti
     * @param max Minimo che si intende settare
     */
    public void setMax(float max) { this.max = max; }

    /**
     * Metodo che ritorna la deviazione standard degli investimenti
     * @return Deviazione standard degli investimenti
     */
    public double getDev_std() { return dev_std; }

    /**
     * Metodo che consente di settare la deviazione standard degli investimenti
     * @param dev_std Deviazione standard che si intende settare
     */
    public void setDev_std(float dev_std) { this.dev_std = dev_std; }

    /**
     * Metodo che ritorna la somma degli investimenti
     * @return Somma degli investimenti
     */
    public float getSum() { return sum; }

    /**
     * Metodo che consente di settare la somma degli investimenti
     * @param sum Somma che si intende settare
     */
    public void setSum(float sum) { this.sum = sum; }

}
