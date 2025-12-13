/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.service;

import it.unisa.sgbu.domain.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * @brief Gestore centrale delle operazioni di prestito e restituzione.
 * * Questa classe implementa la logica per la "Gestione prestiti" e coordina
 * i flussi operativi principali descritti nei relativi Business Flow.
 */
public class RegistroPrestiti implements Serializable {
    
    private List<Prestito> prestitiAttivi;
    private final int LimitePrestiti = 3;
    
    // TRANSIENT: Non vengono salvati nel file dei prestiti per evitare duplicati.
    // Verranno reimpostati (iniettati) dal Controller all'avvio.
    private transient Catalogo catalogo;
    private transient Anagrafica anagrafica;
    
    
    /**
     * @brief Costruttore della classe RegistroPrestiti.
     */
    public RegistroPrestiti(Catalogo catalogo, Anagrafica anagrafica){
        this.catalogo = catalogo;
        this.anagrafica = anagrafica;
        this.prestitiAttivi = new ArrayList<>();
    }
    
    
    /**
     * @brief Esegue la registrazione di un nuovo prestito.
     */
    public Prestito registraPrestito(String isbn, String matr, LocalDate dataPrev){
        // Controllo di sicurezza: se catalogo/anagrafica sono null (post-caricamento),
        // significa che i setter non sono stati chiamati.
        if (catalogo == null || anagrafica == null) {
            System.err.println("ERRORE GRAVE: Dipendenze RegistroPrestiti non inizializzate!");
            return null;
        }

        Libro libro = catalogo.getLibro(isbn);
        Utente utente = anagrafica.getUtente(matr);
        
        // Robustezza: se libro o utente non esistono, fallisco
        if (libro == null || utente == null) {
            return null;
        }
        
        // Verifica Vincoli
        if (!libro.isDisponibile()) {
            return null; // Violazione [FC-1]
        }
        
        if (!utente.verificaLimite()) {
            return null; // Violazione [FC-2]
        }
        
        // Creazione Prestito
        int nuovoId = prestitiAttivi.size() + 1; // Generazione ID
        Prestito nuovoPrestito = new Prestito(nuovoId, libro, utente, LocalDate.now(), dataPrev);
        
        // Aggiornamento Stato
        libro.decrementaDisponibilità();        
        utente.aggiungiPrestito(nuovoPrestito);    
        this.prestitiAttivi.add(nuovoPrestito); 
        
        return nuovoPrestito;
    }
    
    
    /**
     * @brief Registra la restituzione di un libro.
     */
    public boolean registraRestituzione(int idPrestito, LocalDate dataEff){
        
        Prestito prestito = trovaPrestito(idPrestito);
        
        if (prestito == null) {
            return false;
        }
        
        // 1. Recupero il libro 'fantasma' memorizzato nel prestito
        Libro libroNelPrestito = prestito.getLibro();
        
        // 2. MODIFICA FONDAMENTALE: 
        // Uso l'ISBN per recuperare il libro 'reale' dal Catalogo attuale
        // Questo assicura che l'aggiornamento sia visibile nella Tabella Libri
        Libro libroRealeNelCatalogo = catalogo.getLibro(libroNelPrestito.getISBN());
        
        // 3. Aggiorno la disponibilità sul libro del catalogo (se esiste)
        if (libroRealeNelCatalogo != null) {
            libroRealeNelCatalogo.incrementaDisponibilità();
        } else {
            // Fallback: se per assurdo non c'è nel catalogo, aggiorno quello del prestito
            libroNelPrestito.incrementaDisponibilità();
        }

        Utente utente = prestito.getUtente();
        
        // Chiudo il prestito e rimuovo il link dall'utente
        prestito.chiudiPrestito(dataEff); 
        utente.rimuoviPrestito(prestito);
        
        return true;
    }
    
    
    /**
     * @brief Restituisce l'elenco di tutti i prestiti attivi.
     */
    public List<Prestito> getPrestitiAttivi(){
        List<Prestito> attivi = new ArrayList<>();
        
        for (Prestito p : this.prestitiAttivi) {
            if (p.getDataEffettivaRestituzione() == null) {
                attivi.add(p);
            }
        }
        
        Collections.sort(attivi, new Comparator<Prestito>() {
            @Override
            public int compare(Prestito p1, Prestito p2) {
                return p1.getDataPrevistaRestituzione().compareTo(p2.getDataPrevistaRestituzione());
            }
        });
        
        return attivi;
    }
    
    
    /**
     * @brief Restituisce i prestiti attivi di uno specifico utente.
     */
    public List<Prestito> getPrestitiAttivi(Utente u){
        if (u == null) 
            return new ArrayList<>();
        return u.getPrestitiAttivi();
    }
    
    
    /**
     * @brief Identifica e restituisce i prestiti in ritardo.
     */
    public List<Prestito> getPrestitiInRitardo(){
        List<Prestito> inRitardo = new ArrayList<>();
        
        for (Prestito p : this.prestitiAttivi) {
            if (p.getDataEffettivaRestituzione() == null && p.verificaRitardo()) {
                inRitardo.add(p);
            }
        }
        
        return inRitardo;
    }
    
    
    /**
     * @brief Verifica se un utente ha prestiti in corso.
     */
    public boolean haPrestitiAttivi(String matricola){
        if (anagrafica == null) return false; // Controllo sicurezza
        
        Utente u = anagrafica.getUtente(matricola);
        if (u == null) return false;
        return !u.getPrestitiAttivi().isEmpty();
    }
    
    
    /**
     * @brief Ricerca un prestito tramite il suo ID.
     */
    public Prestito trovaPrestito(int idPrestito){
        for(Prestito p : prestitiAttivi){
            if(p.getIdPrestito() == idPrestito){
                return p;
            }
        }
        return null;
    }

    // =========================================================================
    // METODI AGGIUNTI PER LA PERSISTENZA (SETTERS)
    // Questi metodi vengono chiamati dal GUIController dopo il caricamento da file
    // per ripristinare i collegamenti persi a causa di 'transient'.
    // =========================================================================

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public void setAnagrafica(Anagrafica anagrafica) {
        this.anagrafica = anagrafica;
    }
    
}