/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.domain;

import java.time.*;
import java.util.List;
        
/**
 * @brief Rappresenta un libro all'interno del catalogo della biblioteca.
 * 
 * Questa classe gestisce i dati anagrafici del libro e il tracciamento
 * delle copie disponibili e totali, in conformità con i requisiti dei dati.
 * L'ISBN funge da identificativo primario e univoco.
 */
public class Libro {
    
    private final String ISBN;
    private String titolo;
    private List<String> autore;
    private int anno;
    private int copieTotali;
    private int copieDisponibili;
    
    
    /**
     * @brief Costruttore della classe Libro.
     * 
     * Inizializza un nuovo libro nel sistema. All'atto della creazione,
     * il numero di copie disponibili viene inizializzato uguale al numero
     * di copie totali.
     * 
     * @param[in] ISBN Codice identificativo univoco (International Standard Book Number).
     * @param[in] titolo Il titolo del libro.
     * @param[in] autore Lista contenente i nomi degli autori.
     * @param[in] anno Anno di edizione del libro.
     * @param[in] copieTotali Numero totale di copie acquisite.
     * 
     * @pre 
     * - ISBN non deve essere nullo e deve essere univoco.
     * - Titolo e Autore non devono essere vuoti.
     * - anno <= anno corrente e  >= 0.
     * - copieTotali > 0.
     * 
     * @post
     * - L'oggetto Libro è creato.
     * - copieDisponibili == copieTotali.
     */
    public Libro(String ISBN, String titolo, List<String> autore, int anno, int copieTotali){      
    }

    
    /**
     * @brief Restituisce il codice ISBN del libro.
     * @return Una stringa rappresentante l'ISBN.
     */
    public String getISBN() {
        
    }

    
    /**
     * @brief Restituisce il titolo del libro.
     * @return Una List<String> contenente i nomi degli autori
     */
    public List<String> getTitolo() {
         
    }

    
    /**
     * @brief Restituisce l'autore o gli autori del libro.
     * @return Una stringa rappresentante l'autore o la lista degli autori.
     */
    public String getAutore() {
         
    }

    
    /**
     * @brief Restituisce il numero di copie attualmente disponibili.
     * @return Un intero rappresentante le copie presenti in biblioteca e non in prestito.
     */
    public int getCopieDisponibili() {
         
    }
    
    
    /**
     * @brief Incrementa il contatore delle copie disponibili.
     * 
     * Da invocare durante il flusso di "Registrazione restituzione".
     * Aumenta di 1 il numero di copie disponibili a seguito del rientro di un libro.
     * 
     * @pre
     * - copieTotali > copieDisponibili
     * 
     * @post
     * - copieDisponibili è incrementato di 1.
     */
    public void incrementaDisponibilità(){
        
    }
    
    
    /**
     * @brief Decrementa il contatore delle copie disponibili.
     * 
     * Da invocare durante il flusso di "Registrazione prestito".
     * Diminuisce di 1 il numero di copie disponibili a seguito dell'uscita di un libro.
     * 
     * @pre
     * - copieDisponibili > 0 (Vincolo di disponibilità).
     * 
     * @post
     * - copieDisponibili è decrementato di 1.
     */
    public void decrementaDisponibilità(){
    }
    
    
    /**
     * @brief Verifica se il libro è disponibile per il prestito.
     * 
     * Controlla se vi è almeno una copia disponibile per soddisfare
     * il Vincolo di disponibilità.
     * 
     * @return true se copieDisponibili > 0, altrimenti false.
     */
    public boolean isDisponibile(){
    }
}
