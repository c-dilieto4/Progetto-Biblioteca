/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.service;

import it.unisa.sgbu.domain.Libro;
import it.unisa.sgbu.io.*;
import java.util.*;

/**
 * @brief Gestisce il catalogo dei libri della biblioteca.
 * 
 * Questa classe implementa le operazioni CRUD (Create, Read, Update, Delete)
 * sui libri e fornisce funzionalità di ricerca e ordinamento, come specificato
 * nel Caso d'Uso "Gestione catalogo libri".
 */
public class Catalogo {
    
    private Map<String, Libro> registroLibri;
    
    /**
     * @brief Costruttore della classe Catalogo.
     * Inizializza la collezione vuota.
     */
    public Catalogo(){
        this.registroLibri = new HashMap<>();
    }
    
    
    /**
     * @brief Aggiunge un nuovo libro al catalogo.
     * 
     * Implementa il "Flusso di registrazione nuovo libro".
     * Verifica che l'ISBN non sia già presente per soddisfare il relativo vincolo.
     * 
     * @param[in] l L'oggetto Libro da inserire.
     * 
     * @return true se l'inserimento ha successo, false se l'ISBN è già presente.
     * 
     * @pre
     * - l != null.
     * - I dati del libro devono essere conformi al relativo requisito di Dati e Formati .
     * - L'ISBN non deve esistere nel catalogo.
     * 
     * @post
     * - Il libro è aggiunto alla collezione persistente.
     */
    public boolean aggiungiLibro(Libro l){
        if (l == null) return false;
        
        // Vincolo unicità ISBN
        if (registroLibri.containsKey(l.getISBN())) {
            return false;
        }
        
        registroLibri.put(l.getISBN(), l);
        return true;
    }
    
    
    /**
     * @brief Rimuove un libro dal catalogo.
     * 
     * Implementa il "Flusso di eliminazione libro".
     * L'operazione è soggetta al vincolo di integrità referenziale:
     * non è possibile rimuovere un libro se esistono prestiti attivi associati.
     * 
     * @param[in] isbn Il codice identificativo del libro da rimuovere.
     * 
     * @return true se l'eliminazione ha successo, false se il libro non esiste o il vincolo è violato.
     * 
     * @pre
     * - Il libro deve esistere nel catalogo.
     * - Il libro NON deve essere attivo in alcun prestito (verificato tramite interazione con il sistema prestiti).
     * 
     * @post
     * - Il libro è rimosso dalla collezione.
     */
    public boolean rimuoviLibro(String isbn){
        if (isbn == null || !registroLibri.containsKey(isbn)) {
            return false;
        }
        // La verifica dei prestiti attivi è demandata al Controller/RegistroPrestiti
        // prima di chiamare questo metodo.
        registroLibri.remove(isbn);
        return true;
    }
    
    
    /**
     * @brief Modifica i dati di un libro esistente.
     * 
     * Implementa il "Flusso di modifica libro".
     * Permette l'aggiornamento dei dati (Titolo, Autore, Copie).
     * Se la modifica coinvolge l'ISBN, verifica nuovamente l'univocità.
     * 
     * @param[in] isbn L'ISBN originale del libro da modificare.
     * @param[in] nl Il nuovo oggetto Libro contenente i dati aggiornati.
     * 
     * @return true se la modifica ha successo, false altrimenti.
     */
    public boolean modificaLibro(String isbn, Libro nl){
        if (isbn == null || nl == null) return false;
        
        if (!registroLibri.containsKey(isbn)) {
            return false;
        }
        
        // Se l'ISBN cambia, devo gestire la chiave nella mappa
        if (!isbn.equals(nl.getISBN())) {
            // Se il nuovo ISBN è già usato da un ALTRO libro -> Errore
            if (registroLibri.containsKey(nl.getISBN())) {
                return false;
            }
            // Rimuovo vecchio, aggiungo nuovo
            registroLibri.remove(isbn);
            registroLibri.put(nl.getISBN(), nl);
        } else {
            // Sostituzione semplice
            registroLibri.put(isbn, nl);
        }
        return true;
    }
    
    
    /**
     * @brief Recupera un libro tramite ISBN.
     * 
     * Metodo di accesso diretto utilizzato nei flussi di prestito e restituzione.
     * 
     * @param[in] isbn Il codice identificativo.
     * 
     * @return L'oggetto Libro se trovato, null altrimenti.
     */
    public Libro getLibro(String isbn){
        return registroLibri.get(isbn);
    }
    
    
    /**
     * @brief Ricerca libri nel catalogo.
     * 
     * Implementa la funzionalità di ricerca.
     * Permette di filtrare per Titolo, Autore o ISBN.
     * Deve garantire tempi di risposta rapidi entro 2 secondi.
     * 
     * @param[in] query La stringa da cercare.
     * @param[in] campo Il criterio di filtro ("Titolo", "Autore", "ISBN").
     * 
     * @return Lista dei libri che corrispondono ai criteri.
     */
    public List<Libro> ricerca(String query, String campo){
        List<Libro> risultati = new ArrayList<>();
        
        if (query == null || campo == null) return risultati;
        String qLower = query.toLowerCase();
        
        for (Libro l : registroLibri.values()) {
            boolean trovato = false;
            
            if (campo.equalsIgnoreCase("Titolo")) {
                if (l.getTitolo().toLowerCase().contains(qLower)){
                    trovato = true;
                }
                
            } else if (campo.equalsIgnoreCase("ISBN")) {
                if (l.getISBN().contains(query)){
                    trovato = true;
                }
                
            } else if (campo.equalsIgnoreCase("Autore")) {
                // Per gli autori (Lista), controllo se ALMENO UNO corrisponde
                for (String autore : l.getAutore()) {
                    if (autore.toLowerCase().contains(qLower)) {
                        trovato = true;
                        break;
                    }
                }
            }
            
            if (trovato) {
                risultati.add(l);
            }
        }
        return risultati;
    }
    
    
    /**
     * @brief Restituisce la lista completa dei libri ordinata.
     * 
     * Implementa il requisito di visualizzazione, che impone specificamente
     * l'ordinamento alfabetico per Titolo.
     * Mostra per ciascun libro il numero di copie disponibili.
     * 
     * @return Lista di libri ordinata per Titolo.
     */
    public List<Libro> visualizzaOrdinata(){
        List<Libro> lista = new ArrayList<>(registroLibri.values());
        
        Collections.sort(lista, new Comparator<Libro>() {
            @Override
            public int compare(Libro l1, Libro l2) {
                return l1.getTitolo().compareToIgnoreCase(l2.getTitolo());
            }
        });
        
        return lista;
    }
    
}
