/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.gui;

import it.unisa.sgbu.io.*;
import it.unisa.sgbu.service.*;
import it.unisa.sgbu.domain.*;
import javafx.collections.FXCollections; // NECESSARIO PER LA GUI
import javafx.collections.ObservableList; // NECESSARIO PER LA GUI
import java.time.LocalDate;
import java.util.*;

/**
 * @brief Controller principale del sistema (Pattern MVC).
 * Questa classe funge da orchestratore centrale. Collega la GUI ai Servizi,
 * gestisce la validazione, la persistenza e il logging in tempo reale.
 */
public class GUIController {
    
    private IArchivioDati archivio;
    private ILogger logger;
    private IAutenticatore autenticatore;
    private ValidatoreDati valida;

    // Servizi del Dominio
    private Catalogo catalogo;
    private Anagrafica anagrafica;
    private RegistroPrestiti registro;
    
    // Lista osservabile per aggiornare la tabella dei log in tempo reale
    private ObservableList<String> observableLog; 
    
    // Nomi dei file di persistenza
    private static final String FILE_LIBRI = "libri.dat";
    private static final String FILE_UTENTI = "utenti.dat";
    private static final String FILE_PRESTITI = "prestiti.dat";

    /**
     * @brief Costruttore: Inietta tutte le dipendenze.
     */
    public GUIController(IArchivioDati archivio, ILogger logger, IAutenticatore autenticatore, Catalogo catalogo, Anagrafica anagrafica, RegistroPrestiti registro, ValidatoreDati valida){
        this.archivio = archivio;
        this.logger = logger;
        this.autenticatore = autenticatore;
        this.catalogo = catalogo;
        this.anagrafica = anagrafica;
        this.registro = registro;
        this.valida = valida;
        
        // 1. Recupera i log grezzi dal sistema (che sono in ordine cronologico 1..10)
        List<String> logsDalFile = logger.visualizzaLog();
        
        // 2. Crea una NUOVA lista temporanea copiando i dati (per non toccare l'originale del logger)
        List<String> logsPerGui = new ArrayList<>();
        if (logsDalFile != null) {
            logsPerGui.addAll(logsDalFile);
        }
        
        // 3. Inverti la copia (10..1)
        Collections.reverse(logsPerGui);
        
        // 4. Inizializza la lista osservabile della GUI con la lista invertita
        this.observableLog = FXCollections.observableArrayList(logsPerGui);
    }
    
    /**
     * @brief Metodo helper per aggiornare sia il backend che la grafica.
     * Tutte le operazioni passano di qui.
     */
    private void scriviLog(String messaggio) {
        // 1. Backend: Salva in coda (ordine cronologico corretto per il file)
        logger.registraAzione(messaggio);
        
        // 2. Frontend: Aggiunge in CIMA (indice 0) per visualizzare l'ultimo evento in alto
        if (observableLog != null) {
            observableLog.add(0, messaggio);
        }
    }

    public boolean avviaSistema(){
        try {
            scriviLog("--- AVVIO SISTEMA ---");

            // 1. Carica CATALOGO
            Object catObj = archivio.caricaStato(FILE_LIBRI);
            if (catObj instanceof Catalogo) {
                this.catalogo = (Catalogo) catObj;
            }

            // 2. Carica ANAGRAFICA
            Object anagObj = archivio.caricaStato(FILE_UTENTI);
            if (anagObj instanceof Anagrafica) {
                this.anagrafica = (Anagrafica) anagObj;
            }

            // 3. Carica REGISTRO PRESTITI
            Object regObj = archivio.caricaStato(FILE_PRESTITI);
            if (regObj instanceof RegistroPrestiti) {
                this.registro = (RegistroPrestiti) regObj;
            }

            // 4. RICOLLEGAMENTO DIPENDENZE
            this.registro.setCatalogo(this.catalogo);
            this.registro.setAnagrafica(this.anagrafica);

            scriviLog("Dati caricati correttamente.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            scriviLog("Errore durante il caricamento dati: " + e.getMessage());
            return false;
        }
    }
    
    public boolean chiudiSistema(){
        try {
            scriviLog("--- CHIUSURA SISTEMA ---");

            archivio.salvaStato(catalogo, FILE_LIBRI);
            archivio.salvaStato(anagrafica, FILE_UTENTI);
            archivio.salvaStato(registro, FILE_PRESTITI);
            
            // Salva il Log completo su file
            archivio.salvaStato(logger.visualizzaLog(), AuditTrail.NOME_FILE_LOG);

            return true;
        } catch (Exception e) {
            // Qui usiamo logger diretto perché la GUI si sta chiudendo
            logger.registraAzione("Errore salvataggio chiusura: " + e.getMessage());
            return false;
        }
    }

    public boolean aggiungiUtente(Utente u){
        if (u == null) return false;
        
        if (!valida.validaMatricola(u.getMatricola())) return false;
        if (!valida.validaEmail(u.getEmail())) return false;
        if (!valida.validaNomeCognome(u.getNome(), u.getCognome())) return false;
        
        boolean esito = anagrafica.aggiungiUtente(u);
        
        if (esito){
            scriviLog("Aggiunto nuovo utente: " + u.getMatricola());
        }
        else{
            scriviLog("Tentativo aggiunta utente fallito (Duplicato): " + u.getMatricola());
        }
        
        return esito;
    }
    
    public boolean modificaUtente(String matrOriginale, Utente uNuovo){
        if (matrOriginale == null || uNuovo == null) return false;
        
        if (!valida.validaMatricola(uNuovo.getMatricola())) return false;
        if (!valida.validaEmail(uNuovo.getEmail())) return false;
        if (!valida.validaNomeCognome(uNuovo.getNome(), uNuovo.getCognome())) return false;
        
        // Se la matricola è cambiata, verifico che non ci siano prestiti sulla vecchia
        if (!matrOriginale.equals(uNuovo.getMatricola())) {
            if (registro.haPrestitiAttivi(matrOriginale)) {
                scriviLog("Modifica UTENTE fallita: " + matrOriginale + " ha prestiti attivi. Impossibile cambiare Matricola.");
                return false;
            }
        }
        
        boolean esito = anagrafica.modificaUtente(matrOriginale, uNuovo);
        
        if (esito){
            scriviLog("Modificato utente: " + matrOriginale);
        }
        return esito;
    }
    
    public boolean rimuoviUtente(String matr){
        if (!valida.validaMatricola(matr)) return false;
        
        if (registro.haPrestitiAttivi(matr)) {
            scriviLog("Rimozione utente " + matr + " BLOCCATA: ha prestiti attivi.");
            return false;
        }
        
        boolean esito = anagrafica.rimuoviUtente(matr);
        
        if (esito){
            scriviLog("Rimosso utente: " + matr);
        }
        return esito;
    }

    public List<Utente> cercaUtente(String query, String campo){
        return anagrafica.cercaUtente(query, campo);
    }

    public List<Utente> ottieniAnagraficaOrdinata(){
        return anagrafica.visualizzaOrdinata();
    }
    
    public boolean aggiungiLibro(Libro l){
        if (l == null) return false;
        
        if (!valida.validaISBN(l.getISBN())) return false;
        if (!valida.validaAnnoPubblicazione(l.getAnno())) return false;
        if (l.getTitolo() == null || l.getTitolo().isEmpty()) return false;
        
        boolean esito = catalogo.aggiungiLibro(l);
        
        if (esito){
            scriviLog("Aggiunto libro: " + l.getISBN());
        }
        else{
            scriviLog("Tentativo aggiunta libro fallito (Duplicato): " + l.getISBN());
        }
        
        return esito;
    }
    
    public boolean modificaLibro(String isbnOriginale, Libro lNuovo){
        if (isbnOriginale == null || lNuovo == null) return false;
        
        if (!valida.validaISBN(lNuovo.getISBN())) return false;
        if (!valida.validaAnnoPubblicazione(lNuovo.getAnno())) return false;
        
        // Se l'ISBN è cambiato, verifico che il vecchio libro non sia in prestito
        if (!isbnOriginale.equals(lNuovo.getISBN())) {
            List<Prestito> attivi = registro.getPrestitiAttivi();
            for(Prestito p : attivi){
                if(p.getLibro().getISBN().equals(isbnOriginale)){
                    scriviLog("Modifica LIBRO fallita: " + isbnOriginale + " è in prestito. Impossibile cambiare ISBN.");
                    return false;
                }
            }
        }
        
        boolean esito = catalogo.modificaLibro(isbnOriginale, lNuovo);
        
        if (esito){
            scriviLog("Modificato libro: " + isbnOriginale);
        }
        return esito;
    }
    
    public boolean rimuoviLibro(String isbn){
        if (!valida.validaISBN(isbn)) return false;
        
        List<Prestito> attivi = registro.getPrestitiAttivi();
        for(Prestito p : attivi){
            if(p.getLibro().getISBN().equals(isbn)){
                scriviLog("Rimozione libro " + isbn + " BLOCCATA: è attualmente in prestito.");
                return false;
            }
        }
        
        boolean esito = catalogo.rimuoviLibro(isbn);
        
        if (esito){
            scriviLog("Rimosso libro: " + isbn);
        }
        return esito;
    }

    public List<Libro> cercaLibro(String query, String campo){
        return catalogo.ricerca(query, campo);
    }
    
    public List<Libro> ottieniCatalogoOrdinato(){
        return catalogo.visualizzaOrdinata();
    }

    public boolean gestisciPrestito(String isbn, String matricola, LocalDate dataPrevistaRestituzione){
        if (!valida.validaISBN(isbn) || !valida.validaMatricola(matricola)) {
            return false;
        }
        
        Prestito p = registro.registraPrestito(isbn, matricola, dataPrevistaRestituzione);
        archivio.salvaStato(catalogo, FILE_LIBRI);
        
        if (p != null) {
            scriviLog("NUOVO PRESTITO: ID " + p.getIdPrestito() + " | Libro: " + isbn + " | Utente: " + matricola);
            return true;
        } else {
            scriviLog("Prestito FALLITO (Limiti superati o Libro non disponibile): " + isbn + " -> " + matricola);
            return false;
        }
    }
    
    public boolean gestisciRestituzione(int idPrestito, LocalDate dataEffettivaRestituzione){
        boolean esito = registro.registraRestituzione(idPrestito, dataEffettivaRestituzione);
        
        if (esito) {
            scriviLog("RESTITUZIONE REGISTRATA: ID " + idPrestito);
        } else {
            scriviLog("Errore Restituzione: Prestito ID " + idPrestito + " non trovato.");
        }
        
        archivio.salvaStato(catalogo, FILE_LIBRI);
        return esito;
    }
    
    public List<Prestito> ottieniReportPrestiti(){
        return registro.getPrestitiAttivi();
    }

    /**
     * @return La lista osservabile per la TableView della GUI
     */
    public ObservableList<String> ottieniAuditTrail(){
        return (ObservableList<String>)observableLog; 
    }
    
    public boolean gestisciLogin(String user, String pass){
        boolean esito = autenticatore.verificaCredenziali(user, pass);
        if (esito) scriviLog("Login effettuato: " + user);
        else scriviLog("Login fallito: " + user);
        return esito;
    }
}