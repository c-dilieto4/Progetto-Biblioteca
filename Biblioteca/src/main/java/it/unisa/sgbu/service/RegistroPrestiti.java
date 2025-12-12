/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.service;

import it.unisa.sgbu.domain.*;
import it.unisa.sgbu.io.*;
import java.util.*;
import java.time.*;

/**
 * @brief Gestore centrale delle operazioni di prestito e restituzione.
 * 
 * Questa classe implementa la logica per la "Gestione prestiti" e coordina
 * i flussi operativi principali descritti nei relativi Business Flow, verificando i vincoli
 * trasversali prima di autorizzare le transazioni.
 */
public class RegistroPrestiti {
    
    private List<Prestito> prestitiAttivi;
    private final int LimitePrestiti=3;
    private Catalogo catologo;
    private Anagrafica anagrafica;
    
    
    /**
     * @brief Costruttore della classe RegistroPrestiti.
     * 
     * Inizializza il registro e inietta le dipendenze necessarie per operare.
     * 
     * @param[in] catologo Istanza valida del catalogo libri.
     * @param[in] anagrafica Istanza valida dell'anagrafica utenti.
     */
    public RegistroPrestiti(Catalogo catologo, Anagrafica anagrafica){
        this.catologo = catologo;
        this.anagrafica = anagrafica;
        this.prestitiAttivi = new ArrayList<>();
    }
    
    
    /**
     * @brief Esegue la registrazione di un nuovo prestito.
     * 
     * Implementa il "Flusso di registrazione nuovo prestito" e il relativo Caso d'Uso.
     * 
     * Esegue le seguenti verifiche obbligatorie:
     * 1. Verifica disponibilità copie libro tramite Catalogo.
     * 2. Verifica che l'utente non abbia raggiunto il LimitePrestiti.
     * 
     * Se i vincoli sono soddisfatti, crea il prestito, aggiorna le copie disponibili del libro
     * e associa il prestito all'utente.
     * 
     * @param[in] isbn Codice ISBN del libro da prestare.
     * @param[in] matr Matricola dell'utente richiedente.
     * @param[in] dataPrev Data prevista per la restituzione.
     * 
     * @return L'oggetto Prestito creato se l'operazione ha successo, null se sono violati i vincoli.
     * 
     * @pre
     * - isbn e matr devono corrispondere a entità esistenti.
     * - Il libro deve avere `copieDisponibili > 0`.
     * - L'utente deve avere `prestitiAttivi < 3`.
     * - Se la data effettiva è null, il prestito è ancora aperto
     * 
     * @post
     * - Viene creato un nuovo record di prestito attivo.
     * - `Libro.copieDisponibili` viene decrementato di 1.
     */
    public Prestito registraPrestito(String isbn, String matr, LocalDate dataPrev){
        Libro libro = catologo.getLibro(isbn);
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
     * 
     * Implementa il "Flusso di registrazione restituzione" e il relativo Caso d'Uso.
     * Chiude il prestito attivo e incrementa le copie disponibili del libro.
     * Innesca automaticamente il controllo per il "Flusso di segnalazione ritardo"
     * se la data effettiva è successiva a quella prevista.
     * 
     * @param[in] idPrestito Identificativo del prestito da chiudere.
     * @param[in] dataEff Data effettiva di riconsegna.
     * 
     * @return true se la restituzione è registrata con successo, false se il prestito non esiste.
     * 
     * @post
     * - Il record prestito viene chiuso (data effettiva impostata).
     * - `Libro.copieDisponibili` viene incrementato di 1.
     * - Se in ritardo, lo stato del prestito viene aggiornato.
     */
    public boolean registraRestituzione(int idPrestito, LocalDate dataEff){
        
        Prestito prestito = trovaPrestito(idPrestito);
        
        if (prestito == null) {
            return false;
        }
        
        Libro libro = prestito.getLibro();
        Utente utente = prestito.getUtente();
        
        prestito.chiudiPrestito(dataEff); 
        libro.incrementaDisponibilità();  
        utente.rimuoviPrestito(prestito);
        
        return true;
    }
    
    
    /**
     * @brief Restituisce l'elenco di tutti i prestiti attivi.
     * 
     * Implementa il requisito funzionale. La lista restituita
     * deve essere ordinata per data prevista di restituzione per facilitare
     * il monitoraggio delle scadenze.
     * 
     * @return Lista di oggetti Prestito non ancora chiusi.
     */
    public List<Prestito> getPrestitiAttivi(){
        List<Prestito> attivi = new ArrayList<>();
        
        // Scorro tutto l'archivio con un ciclo
        for (Prestito p : this.prestitiAttivi) {
            // Se la data effettiva è null, il prestito è ancora aperto
            if (p.getDataEffettivaRestituzione() == null) {
                attivi.add(p);
            }
        }
        
        // Ordino la lista per data di scadenza (richiesto dalla traccia)
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
     * 
     * Utilizzato per verificare il vincolo sul numero massimo di prestiti [FC-2]
     * e per visualizzare la situazione del singolo utente.
     * 
     * @param[in] u L'utente di cui cercare i prestiti.
     * 
     * @return Lista dei prestiti associati all'utente.
     */
    public List<Prestito> getPrestitiAttivi(Utente u){
        if (u == null) 
            return new ArrayList<>();
        return u.getPrestitiAttivi();
    }
    
    
    /**
     * @brief Identifica e restituisce i prestiti in ritardo.
     * 
     * Filtra i prestiti attivi confrontando la data prevista di restituzione
     * con la data corrente
     * 
     * @return Lista di prestiti scaduti.
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
     * 
     * Controllo critico richiesto per il "Vincolo cancellazione dati".
     * Impedisce l'eliminazione di un utente dall'anagrafica se questi detiene ancora
     * libri della biblioteca.
     * 
     * @param[in] matricola La matricola dell'utente da controllare.
     * 
     * @return true se esistono prestiti attivi, false altrimenti.
     */
    public boolean haPrestitiAttivi(String matricola){
        Utente u = anagrafica.getUtente(matricola);
        if (u == null) return false;
        return !u.getPrestitiAttivi().isEmpty();
    }
    
    
    /**
     * @brief Ricerca un prestito tramite il suo ID.
     * 
     * Metodo di utilità per recuperare l'oggetto prestito necessario per
     * effettuare la restituzione.
     * 
     * @param[in] idPrestito L'identificativo univoco del prestito.
     * 
     * @return L'oggetto Prestito se trovato, null altrimenti.
     */
    public Prestito trovaPrestito(int idPrestito){
        for(Prestito p : prestitiAttivi){
            if(p.getIdPrestito() == idPrestito){
                return p;
            }
        }
        return null;
    }
    
}
