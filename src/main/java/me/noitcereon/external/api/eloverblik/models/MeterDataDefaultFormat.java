package me.noitcereon.external.api.eloverblik.models;


/**
 * This class represents the default format that ElOverblikApi sends as CSV.
 * @apiNote Initially made just to have the HEADERS static method, but may be transformed into a record if needed for formatting back to default format.
 */
public class MeterDataDefaultFormat {
    public static final String HEADERS = "Målepunkt id;Fra dato;Til dato;Mængde;Måleenhed;Kvalitet;Type";
}
