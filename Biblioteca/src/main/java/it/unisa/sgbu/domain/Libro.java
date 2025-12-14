/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.domain;

import java.io.Serializable;
import java.time.*;
import java.util.List;
        
/**
 * @brief Rappresenta un libro all'interno del catalogo della biblioteca.
 * 
 * Questa classe gestisce i dati anagrafici del libro e il tracciamento
 * delle copie disponibili e totali, in conformità con i requisiti dei dati.
 * L'ISBN funge da identificativo primario e univoco.
 * 
 * Implementa l'interfaccia java.io.Serializable per consentire la serializzazione
 * e il salvataggio persistente dell'oggetto su file.
 */
public class Libro implements Serializable{
    
    private final String isbn;
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
    public Libro(String isbn, String titolo, List<String> autore, int anno, int copieTotali){    
        this.isbn = isbn;
        this.titolo = titolo;
        this.autore = autore;
        this.anno = anno;
        this.copieTotali = copieTotali;
        this.copieDisponibili = copieTotali;
    }

    
    /**
     * @brief Restituisce il codice ISBN del libro.
     * @return Una stringa rappresentante l'ISBN.
     */
    public String getISBN() {
        return isbn;
    }

    
    /**
     * @brief Restituisce il titolo del libro.
     * @return Una stringa contenente il titolo.
     */
    public String getTitolo() {
         return titolo;
    }

    
    /**
     * @brief Restituisce l'autore o gli autori del libro.
     * @return Una List<String> rappresentante l'autore o la lista degli autori.
     */
    public List<String> getAutore() {
         return autore;
    }

    
    /**
     * @brief Restituisce il numero di copie totali.
     * @return Un intero rappresentante le copie presenti in biblioteca.
     */
    public int getCopieTotali() {
         return copieTotali;
    }
    
    
    /**
     * @brief Restituisce l'anno di pubblicazione del libro.
     * @return Un intero rappresentante l'anno di pubblicazione.
     */
    public int getAnno() {
         return anno;
    }
    
    /**
     * @brief Restituisce il numero di copie attualmente disponibili per il prestito.
     * @return Un intero rappresentante le copie attualmente presenti (non in prestito) in biblioteca.
     */
    public int getCopieDisponibili() {
        return copieDisponibili;
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
        //PRINCIPIO DI ROBUSTEZZA
        if (copieDisponibili < copieTotali) {
            this.copieDisponibili++;
        }
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
        //PRINCIPIO DI ROBUSTEZZA
        if (copieDisponibili > 0) {
            this.copieDisponibili--;
        }
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
        return copieDisponibili > 0;
    }
    
    @Override
    public String toString() {
        return this.isbn; // O this.titolo, quello che preferisci vedere
    }
}
