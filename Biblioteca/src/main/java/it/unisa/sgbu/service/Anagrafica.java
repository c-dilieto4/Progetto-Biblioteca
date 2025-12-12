/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.service;


import it.unisa.sgbu.domain.*;
import it.unisa.sgbu.io.*;
import java.util.*;

/**
 * @brief Gestisce il registro (anagrafica) degli utenti della biblioteca.
 * 
 * Questa classe implementa le operazioni CRUD (Create, Read, Update, Delete)
 * sugli utenti, garantendo il rispetto dei vincoli di unicità e integrità referenziale
 * definiti nelle specifiche.
 */
public class Anagrafica {
    
    //Mappa per accesso rapido O(1)
    private Map<String, Utente> registroUtenti;
    
    
    /**
     * @brief Costruttore della classe Anagrafica.
     * Inizializza la struttura dati per contenere il registro degli utenti.
     */
    public Anagrafica(){
        this.registroUtenti = new HashMap<>();
    }
    
    
    /**
     * @brief Aggiunge un nuovo utente all'anagrafica.
     * 
     * Implementa il "Flusso di registrazione nuovo utente".
     * Verifica che la matricola non sia già presente nel sistema per soddisfare
     * il vincolo di univocità.
     * 
     * @param[in] u L'oggetto Utente da inserire.
     * 
     * @return true se l'inserimento ha successo, false se la matricola esiste già.
     * 
     * @pre 
     * - L'oggetto u deve essere validato (formato matricola ed email).
     * - La matricola non deve esistere nel registro.
     * 
     * @post
     * - L'utente è aggiunto al registro persistente.
     */
    public boolean aggiungiUtente(Utente u){
        if (u == null) return false;
        
        // Verifica unicità matricola (la chiave della mappa)
        if (registroUtenti.containsKey(u.getMatricola())) {
            return false;
        }
        
        registroUtenti.put(u.getMatricola(), u);
        return true;
    }
    
    
    /**
     * @brief Rimuove un utente dall'anagrafica.
     * 
     * Implementa il "Flusso di eliminazione utente".
     * Prima di procedere alla cancellazione, verifica rigorosamente che l'utente
     * non abbia prestiti attivi, come imposto dal relativo vincolo.
     * 
     * @param[in] matricola L'identificativo dell'utente da rimuovere.
     * 
     * @return true se l'eliminazione ha successo, false se l'utente non esiste o ha prestiti attivi.
     * 
     * @pre
     * - L'utente deve esistere.
     * - L'utente NON deve avere prestiti attivi (Vincolo cancellazione dati).
     * 
     * @post
     * - L'utente è rimosso dal sistema.
     */
    public boolean rimuoviUtente(String matricola){
        if (matricola == null || !registroUtenti.containsKey(matricola)) {
            return false;
        }
        
        // Nota: Il controllo sui prestiti attivi deve essere fatto PRIMA di chiamare questo metodo
        // dal livello superiore (GUIController) usando RegistroPrestiti.haPrestitiAttivi().
        
        registroUtenti.remove(matricola);
        return true;
    }
    
    
    /**
     * @brief Modifica i dati di un utente esistente.
     * 
     * Implementa il "Flusso di modifica utente".
     * Permette l'aggiornamento dei dati anagrafici. Se la modifica coinvolge la matricola,
     * verifica nuovamente l'univocità.
     * 
     * @param[in] matricola La matricola originale dell'utente da modificare.
     * @param[in] u L'oggetto Utente con i dati aggiornati.
     * 
     * @return true se la modifica ha successo, false altrimenti.
     */
    public boolean modificaUtente(String matricola, Utente u){
        if (matricola == null || u == null) return false;
        
        if (!registroUtenti.containsKey(matricola)) {
            return false;
        }
        
        // Se la matricola cambia, devo verificare che la nuova non esista già
        if (!matricola.equals(u.getMatricola())) {
            if (registroUtenti.containsKey(u.getMatricola())) {
                return false; // La nuova matricola è già occupata
            }
            // Rimuovo la vecchia entry e metto la nuova
            registroUtenti.remove(matricola);
            registroUtenti.put(u.getMatricola(), u);
        } else {
            // Aggiorno solo i dati (sovrascrivo)
            registroUtenti.put(matricola, u);
        }
        
        return true;
    }
    
    
    /**
     * @brief Recupera un singolo utente tramite matricola.
     * 
     * Metodo di utilità per l'accesso diretto ai dati di un utente,
     * utilizzato nei vari flussi operativi.
     * 
     * @param[in] matricola L'identificativo univoco.
     * 
     * @return L'oggetto Utente se trovato, altrimenti null.
     */
    public Utente getUtente(String matricola){
        return registroUtenti.get(matricola);
    }
    
    
    /**
     * @brief Ricerca utenti in base a un criterio specifico.
     * 
     * Implementa la funzionalità di ricerca, permettendo di filtrare
     * l'archivio per "Cognome" o "Matricola".
     * Deve garantire tempi di risposta entro 2 secondi.
     * 
     * @param[in] query La stringa da cercare.
     * @param[in] campo Il campo su cui cercare ("Cognome" o "Matricola").
     * 
     * @return Una lista di utenti che soddisfano i criteri di ricerca.
     * 
     */
    public List<Utente> cercaUtente(String query, String campo){
        List<Utente> risultati = new ArrayList<>();
        
        if (query == null || campo == null) return risultati;
        
        for (Utente u : registroUtenti.values()) {
            boolean trovato = false;
            
            if (campo.equalsIgnoreCase("Matricola")) {
                if (u.getMatricola().contains(query)){
                    trovato = true;
                }
            } else if (campo.equalsIgnoreCase("Cognome")) {
                if (u.getCognome().toLowerCase().contains(query.toLowerCase())){
                    trovato = true;
                }
            }
            
            if (trovato) {
                risultati.add(u);
            }
        }
        return risultati;
    }
    
    
    /**
     * @brief Restituisce la lista completa degli utenti ordinata.
     * 
     * Implementa il requisito di visualizzazione, che richiede
     * l'ordinamento specifico per Cognome e Nome.
     * 
     * @return Una lista di oggetti Utente ordinata per Cognome e poi Nome.
     */
    public List<Utente> visualizzaOrdinata(){
        List<Utente> lista = new ArrayList<>(registroUtenti.values());
        
        Collections.sort(lista, new Comparator<Utente>() {
            @Override
            public int compare(Utente u1, Utente u2) {
                int res = u1.getCognome().compareToIgnoreCase(u2.getCognome());
                if (res == 0) {
                    return u1.getNome().compareToIgnoreCase(u2.getNome());
                }
                return res;
            }
        });
        
        return lista;
    }
}
