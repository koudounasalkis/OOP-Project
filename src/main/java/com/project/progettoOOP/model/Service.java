package com.project.progettoOOP.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Arrays;

/**
 * La classe Services modella un singolo elemento del dataset
 */
public class Service implements Serializable {

    /**
     * Frequenza di campionamento dell'elemento del dataset
     */
    @JsonPropertyDescription("Frequenza del campionamento ('A' = Annuale)")
    private String freq;

    /**
     * Entità geopolitica dell'elemento del dataset
     */
    @JsonPropertyDescription("Entità geopolitica di interesse")
    private String geo;

    /**
     * Unità di misura dell'elemento del dataset (in percentuale del PIL)
     */
    @JsonPropertyDescription("Unità di misura considerata (Percentuale del PIL)")
    private String unit;

    /**
     * Obiettivo dell'elemento del dataset
     */
    @JsonPropertyDescription("Ambito di investimento del capitale")
    private String objective;

    /**
     * Valori dell'elemento del dataset per ogni anno
     */
    @JsonPropertyDescription("Oggetto contenente la percentuale di PIL investita nei vari servizi ogni anno, dal 2000 al 2017, per ogni Paese.")
    private Float[] misure;

    /**
     * Costruttore della classe che inizializza l'oggetto.
     * @param freq Stringa contenente la frequenza di campionamento del dato
     * @param geo Stringa contenente l'area geopolitica di riferimento
     * @param unit Stringa contenente l'unità di misura (percentuale di PIL)
     * @param objective Stringa contenente l'obiettivo relativo al dato, ossia il particolare servizio di interesse
     * @param misure Array contenente la percentuale di PIL investita in un determinato servizio per ogni anno (dal 2000 al 2017)
     */
    public Service(String freq, String geo, String unit, String objective, Float[] misure) {
        this.freq = freq;
        this.geo = geo;
        this.unit = unit;
        this.objective = objective;
        this.misure = misure;
    }

    /**
     * Funzione che restituisce la frequenza di campionamento del dato
     * @return Frequenza di campionamento del dato
     */
    public String getFreq() {
        return freq;
    }

    /**
     * /**
     * Funzione che imposta la frequenza di campionamento del dato
     * @param freq Stringa contenente la frequenza che si vuole settare
     */
    public void setFreq(String freq) {
        this.freq = freq;
    }

    /**
     * Funzione che restituisce l'entità geopolitica
     * @return Entità geopolitica dell'oggetto
     */
    public String getGeo() {
        return geo;
    }

    /**
     * Funzione che imposta l'entità geopolitica
     * @param geo Stringa contenente l'entità geopolitica che si vuole settare
     */
    public void setGeo(String geo) {
        this.geo = geo;
    }

    /**
     * Funzione che restituisce l'unità di misura (in percentuale di PIL)
     * @return Unità di misura dell'oggetto
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Funzione che imposta l'unità di misura
     * @param unit Stringa contenete l'unità di misura che si vuole settare
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Funzione che restituisce il particolare obiettivo considerato
     * @return Obiettivo dell'oggetto
     */
    public String getObjective() {
        return objective;
    }

    /**
     * Funzione che imposta l'obiettivo dell'oggetto
     * @param objective Stringa contenente l'obietti
     */
    public void setObjective(String objective) {
        this.objective = objective;
    }

    /**
     * Funzione che restituisce l'oggetto contenente tutti gli investimenti effettuati (in percentuali di PIL) per ogni anno
     * @return Oggetto contenente gli investimenti relativi al dato, divis per anno
     */
    public Float[] getMisure() {
        return misure;
    }

    /**
     * Funzione che imposta l'oggetto contenente gli investimenti relativi al dato
     * @param misure Oggetto contenente gli investimenti che si vogliono settare
     */
    public void setMisure(Float[] misure) {
        this.misure = misure;
    }

    /**
     * Funzione che restituisce gli investimenti effettuati  in un determinato anno
     * @param year Anno di cui si vogliono conoscere gli investimenti effettuati
     * @return Investimenti (in percentuale di PIL) relativi all'anno in questione
     */
    @JsonIgnore
    public Float getMisureOfTheYear(int year) {
        return this.misure[year - 2000];
    }

    /**
     * Funzione che riporta sotto forma di stringa gli attributi della classe Service
     * @return Stringa contenente gli attributi della classe Service
     */
    @Override
    public String toString() {
        return "Service{" +
                "freq='" + freq + '\'' +
                ", geo='" + geo + '\'' +
                ", unit='" + unit + '\'' +
                ", objective='" + objective + '\'' +
                ", misure=" + Arrays.toString(misure) +
                '}' ;
    }
}