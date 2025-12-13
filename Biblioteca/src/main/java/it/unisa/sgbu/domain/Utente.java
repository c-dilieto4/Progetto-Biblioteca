/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.domain;

import java.io.Serializable;
import java.util.*;

/**
 * @brief Rappresenta un utente della biblioteca e mantiene il tracciamento logico dei 
 * prestiti attivi a suo nome.
 * 
 * Questa classe gestisce i dati anagrafici dell'utente e mantiene la relazione
 * logica con i prestiti attivi a suo nome.
 * 
 * Implementa l'interfaccia java.io.Serializable per consentire la serializzazione
 * e il salvataggio persistente dell'oggetto su file.
 * 
 */
public class Utente implements Serializable {
    
    private final String matricola;
    private String nome;
    private String cognome;
    private String email;
    private List<Prestito> prestitiAttivi;
    
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
        this.matricola = matricola;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.prestitiAttivi = new ArrayList<>();
    }

    /**
     * @brief Restituisce l'indirizzo email dell'utente.
     * @return Una stringa contenente l'email.
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * @brief Restituisce la matricola dell'utente.
     * Utilizzato per l'ordinamento nella visualizzazione e per la ricerca.
     * @return Stringa contenente la matricola.
     */
    public String getMatricola() {
        return matricola;
    }
    
    
    /**
     * @brief Restituisce il cognome dell'utente.
     * Utilizzato per l'ordinamento nella visualizzazione e per la ricerca.
     * @return Stringa contenente il cognome.
     */
    public String getCognome() {
        return cognome;
    }
    
    /**
     * @brief Restituisce il nome dell'utente.
     * * @return Una stringa contenente il nome.
     */
    public String getNome() {
        return nome;
    }
    
    /**
     * @brief Restituisce il numero attuale di prestiti in carico all'utente.
     * @return Un intero rappresentante la dimensione della lista prestitiAttivi.
     */
    public int getNumeroPrestitiAttivi() {
        return this.prestitiAttivi.size();
    }
    
    /**
     * @brief Restituisce la lista completa dei prestiti attualmente in carico all'utente.
     * * @return La lista (List) di oggetti Prestito associati all'utente.
     */
    public List<Prestito> getPrestitiAttivi() {
        return prestitiAttivi;
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
        // Controllo di robustezza e rispetto delle precondizioni
        if (p != null && this.verificaLimite()) {
            this.prestitiAttivi.add(p);
        }
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
        // Controllo di robustezza e rispetto delle precondizioni
        if (p != null && this.prestitiAttivi.contains(p)) {
            this.prestitiAttivi.remove(p);
        }
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
        return this.prestitiAttivi.size() < 3;
    }
    
    @Override
    public String toString() {
        // Ritorna "Nome Cognome"
        return this.matricola;
        
    }
}
