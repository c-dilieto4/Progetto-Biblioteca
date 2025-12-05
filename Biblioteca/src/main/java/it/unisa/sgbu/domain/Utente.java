/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.domain;

import java.util.*;

/**
 * @brief Rappresenta un utente della biblioteca e mantiene il tracciamento logico dei 
 * prestiti attivi a suo nome.
 * 
 * Questa classe gestisce i dati anagrafici dell'utente e mantiene la relazione
 * logica con i prestiti attivi a suo nome.
 * 
 */
public class Utente {
    
    private final String matricola;
    private String nome;
    private String cognome;
    private String email;
    
    
    /**
     * @brief Costruttore della classe Utente.
     * 
     * Inizializza un nuovo profilo utente nel sistema. Viene invocato durante
     * il "Flusso di registrazione nuovo utente".
     * 
     * @param[in] matricola Codice identificativo (10 cifre).
     * @param[in] nome Nome dell'utente.
     * @param[in] cognome Cognome dell'utente.
     * @param[in] email Indirizzo di posta elettronica.
     * 
     * @pre
     * - matricola deve essere di 10 cifre numeriche (Assunzione n°3) e univoca.
     * - nome e cognome devono contenere solo caratteri alfabetici.
     * - email deve essere in un formato valido.
     * 
     * @post
     * - L'oggetto Utente è creato con i dati forniti.
     * - La lista dei prestiti associata è inizializzata vuota.
     */
    public Utente(String matricola, String nome, String cognome, String email){   
    }

    
    /**
     * @brief Restituisce la matricola dell'utente.
     * @return Stringa contenente la matricola.
     */
    public String getMatricola() {
    }
    
    
    /**
     * @brief Restituisce il cognome dell'utente.
     * Utilizzato per l'ordinamento nella visualizzazione e per la ricerca.
     * @return Stringa contenente il cognome.
     */
    public String getCognome() {
    }
    
    
    /**
     * @brief Associa un nuovo prestito all'utente.
     * 
     * Questo metodo viene chiamato durante la registrazione di un prestito,
     * dopo aver verificato che il limite massimo non sia stato raggiunto.
     * 
     * @param[in] p L'oggetto Prestito da associare.
     * 
     * @pre
     * - p != null.
     * - Il numero di prestiti attivi deve essere minore di 3.
     * 
     * @post
     * - Il prestito viene aggiunto alla lista dei prestiti attivi dell'utente.
     */
    public void aggiungiPrestito(Prestito p){
    }
    
    
    /**
     * @brief Rimuove un prestito dalla lista dell'utente.
     * 
     * Questo metodo viene chiamato durante la registrazione di una restituzione,
     * per dissociare il prestito attivo dall'utente.
     * 
     * @param[in] p L'oggetto Prestito da rimuovere.
     *  
     * @pre
     * - Il prestito p deve essere presente nella lista dell'utente.
     * 
     * @post
     * - Il prestito viene rimosso dai prestiti attivi.
     */
    public void rimuoviPrestito(Prestito p){
    }
    
    
    /**
     * @brief Verifica se l'utente ha raggiunto il limite di prestiti.
     * 
     * Controlla se l'utente ha già 3 libri in prestito, in accordo con il
     * Vincolo limite prestiti per singolo utente.
     * 
     * @return true se l'utente ha meno di 3 prestiti attivi (e quindi può prendere un 
     * altro libro), false se il limite (3) è stato raggiunto.
     */
    public boolean verificaLimite(){
    }
    
    
}
