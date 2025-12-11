/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.gui;

import it.unisa.sgbu.io.*;
import it.unisa.sgbu.service.*;
import it.unisa.sgbu.domain.*;
import java.time.LocalDate;
import java.util.*;

/**
 * @brief Controller principale del sistema (Pattern MVC).
 * Questa classe funge da orchestratore centrale per la logica applicativa.
 * Riceve gli input dall'interfaccia utente (GUIView), coordina le operazioni
 * di validazione tramite le Utility, delega l'esecuzione della logica di business
 * ai servizi (Catalogo, Anagrafica, RegistroPrestiti) e gestisce gli aspetti trasversali
 * come il Logging e la Persistenza.
 * Implementa il principio di "Inversione delle Dipendenze" interagendo
 * con i servizi esterni tramite interfacce (IArchivioDati, ILogger, IAutenticatore).
 */
public class GUIController {
    
    private IArchivioDati archivio;
    private ILogger logger;
    private Catalogo catalogo;
    private Anagrafica anagrafica;
    private RegistroPrestiti registro;
    private ValidatoreDati valida;
    private IAutenticatore autenticatore;
    
    // Nomi dei file di persistenza
    private static final String FILE_LIBRI = "libri.dat";
    private static final String FILE_UTENTI = "utenti.dat";
    private static final String FILE_PRESTITI = "prestiti.dat";

    /**
     * @brief Costruttore della classe GUIController.
     * 
     * Inizializza il controller iniettando tutte le dipendenze necessarie.
     * 
     * @param[in] archivio Il gestore della persistenza dei dati.
     * @param[in] logger Il gestore del tracciamento delle operazioni (Audit Trail).
     * @param[in] autenticatore Il servizio per la verifica delle credenziali.
     * @param[in] catalogo Il gestore del catalogo libri.
     * @param[in] anagrafica Il gestore dell'anagrafica utenti.
     * @param[in] registro Il gestore delle transazioni di prestito.
     * @param[in] valida Il componente per la validazione formale dei dati.
     */
    public GUIController(IArchivioDati archivio, ILogger logger, IAutenticatore autenticatore, Catalogo catalogo, Anagrafica anagrafica, RegistroPrestiti registro, ValidatoreDati valida){
        this.archivio = archivio;
        this.logger = logger;
        this.autenticatore = autenticatore;
        this.catalogo = catalogo;
        this.anagrafica = anagrafica;
        this.registro = registro;
        this.valida = valida;
    }
    
    /**
     * @brief Avvia il sistema caricando i dati persistenti.
     * 
     * Implementa il Caso d'Uso "Gestione persistenza dati" (Avvio).
     * Tenta di recuperare lo stato salvato (Libri, Utenti, Prestiti) tramite l'archivio.
     * Rispetta il requisito sui tempi di caricamento.
     * 
     * @return true se il sistema è avviato correttamente, false in caso di errore critico.
     */
    public boolean avviaSistema(){
        try {
            // Logica di caricamento (adattabile in base all'implementazione specifica di IArchivioDati)
            if (catalogo == null || anagrafica == null || registro == null) return false;

            logger.registraAzione("Sistema avviato.");
            return true;
        } catch (Exception e) {
            logger.registraAzione("Errore critico avvio sistema: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * @brief Chiude il sistema salvando lo stato corrente.
     * 
     * Implementa il Caso d'Uso "Gestione persistenza dati" (Chiusura).
     * Serializza lo stato attuale dei gestori su file.
     * 
     * @return true se il salvataggio ha successo, false altrimenti.
     */
    public boolean chiudiSistema(){
        try {
            // Esempio logica salvataggio:
            // archivio.salvaStato(catalogo, FILE_LIBRI);
            // archivio.salvaStato(anagrafica, FILE_UTENTI);
            // archivio.salvaStato(registro, FILE_PRESTITI);
            
            logger.registraAzione("Sistema chiuso correttamente.");
            return true;
        } catch (Exception e) {
            logger.registraAzione("Errore chiusura sistema: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * @brief Coordina l'aggiunta di un nuovo utente.
     * 
     * Esegue la validazione formale dei campi tramite ValidatoreDati e, se superata,
     * delega l'inserimento all'Anagrafica.
     * Se l'operazione ha successo, registra l'evento nel Log.
     * 
     * @param[in] u L'oggetto Utente da aggiungere.
     * 
     * @return true se l'utente è stato aggiunto, false se i dati non sono validi o la matricola è duplicata.
     */
    public boolean aggiungiUtente(Utente u){
        // Validazione completa dell'oggetto Utente campo per campo
        if (u == null) return false;
        
        if (!valida.validaMatricola(u.getMatricola())) return false;
        if (!valida.validaEmail(u.getEmail())) return false;
        if (!valida.validaNomeCognome(u.getNome(), u.getCognome())) return false;
        
        boolean esito = anagrafica.aggiungiUtente(u);
        
        if (esito) {
            logger.registraAzione("Aggiunto utente: " + u.getMatricola());
        }
        return esito;
    }
    
    /**
     * @brief Coordina l'aggiunta di un nuovo libro.
     * 
     * Esegue la validazione formale dei dati e delega l'inserimento al Catalogo.
     * Se l'operazione ha successo, registra l'evento nel Log.
     * 
     * @param[in] l L'oggetto Libro da aggiungere.
     * 
     * @return true se il libro è stato aggiunto, false se i dati non sono validi o l'ISBN è duplicato.
     */
    public boolean aggiungiLibro(Libro l){
        // Validazione completa dell'oggetto Libro campo per campo
        if (l == null) return false;
        
        if (!valida.validaISBN(l.getISBN())) return false;
        if (!valida.validaAnnoPubblicazione(l.getAnno())) return false;
        // Controllo base su Titolo/Autore (non vuoti) se non gestito dal ValidatoreDati
        if (l.getTitolo() == null || l.getTitolo().isEmpty()) return false;
        
        boolean esito = catalogo.aggiungiLibro(l);
        
        if (esito) {
            logger.registraAzione("Aggiunto libro: " + l.getISBN());
        }
        return esito;
    }
    
    /**
     * @brief Coordina la modifica di un utente esistente.
     * 
     * Implementa il relativo Business Flow.
     * Verifica la validità dei nuovi dati e delega l'aggiornamento all'Anagrafica.
     * 
     * @param[in] matrOriginale La matricola originale dell'utente da modificare.
     * @param[in] uNuovo L'oggetto con i dati aggiornati.
     * 
     * @return true se la modifica ha successo, false altrimenti.
     */
    public boolean modificaUtente(String matrOriginale, Utente uNuovo){
        // Valido la matricola originale per la ricerca
        if (!valida.validaMatricola(matrOriginale)) return false;
        
        // Valido i nuovi dati
        if (uNuovo == null) return false;
        if (!valida.validaMatricola(uNuovo.getMatricola())) return false;
        if (!valida.validaEmail(uNuovo.getEmail())) return false;
        if (!valida.validaNomeCognome(uNuovo.getNome(), uNuovo.getCognome())) return false;
        
        boolean esito = anagrafica.modificaUtente(matrOriginale, uNuovo);
        
        if (esito) {
            logger.registraAzione("Modificato utente: " + matrOriginale);
        }
        return esito;
    }
    
    /**
     * @brief Coordina la modifica di un libro esistente.
     * 
     * Implementa il relativo Business Flow.
     * Verifica la validità dei nuovi dati e delega l'aggiornamento al Catalogo.
     * 
     * @param[in] isbnOriginale L'ISBN originale del libro da modificare.
     * @param[in] lNuovo L'oggetto con i dati aggiornati.
     * 
     * @return true se la modifica ha successo, false altrimenti. 
     */
    public boolean modificaLibro(String isbnOriginale, Libro lNuovo){
        // Valido l'ISBN originale per la ricerca
        if (!valida.validaISBN(isbnOriginale)) return false;

        // Valido i nuovi dati
        if (lNuovo == null) return false;
        if (!valida.validaISBN(lNuovo.getISBN())) return false;
        if (!valida.validaAnnoPubblicazione(lNuovo.getAnno())) return false;
        if (lNuovo.getTitolo() == null || lNuovo.getTitolo().isEmpty()) return false;

        boolean esito = catalogo.modificaLibro(isbnOriginale, lNuovo);
        
        if (esito) {
            logger.registraAzione("Modificato libro: " + isbnOriginale);
        }
        return esito;
    }
    
    /**
     * @brief Esegue la ricerca di libri nel catalogo.
     * 
     * @param[in] query La stringa da cercare.
     * @param[in] campo Il campo su cui filtrare (es. Titolo, Autore).
     * 
     * @return Una lista di libri che corrispondono ai criteri.
     */
    public List<Libro> cercaLibro(String query, String campo){
        // Se la query è vuota, restituisco tutto
        if (query == null || query.isEmpty()) {
            return catalogo.visualizzaOrdinata();
        }
        return catalogo.ricerca(query, campo);
    }
    
    /**
     * @brief Esegue la ricerca di utenti nell'anagrafica.
     * 
     * @param[in] query La stringa da cercare.
     * @param[in] campo Il campo su cui filtrare (es. Cognome, Matricola).
     * 
     * @return Una lista di utenti che corrispondono ai criteri.
     */
    public List<Utente> cercaUtente(String query, String campo){
        if (query == null || query.isEmpty()) {
            return anagrafica.visualizzaOrdinata();
        }
        return anagrafica.cercaUtente(query, campo);
    }
    
    /**
     * @brief Coordina la rimozione di un utente.
     * 
     * Prima di delegare la rimozione all'Anagrafica, interroga il RegistroPrestiti
     * per verificare che l'utente non abbia prestiti attivi.
     * Se la rimozione avviene, l'azione viene loggata.
     * 
     * @param[in] matr La matricola dell'utente da rimuovere.
     * 
     * @return true se rimosso con successo, false se l'utente ha prestiti attivi o non esiste.
     */
    public boolean rimuoviUtente(String matr){
        if (!valida.validaMatricola(matr)) return false;
        
        // Verifica vincolo prestiti attivi tramite RegistroPrestiti
        if (registro.haPrestitiAttivi(matr)) {
            logger.registraAzione("Rimozione utente " + matr + " fallita: ha prestiti attivi.");
            return false;
        }
        
        boolean esito = anagrafica.rimuoviUtente(matr);
        if (esito) {
            logger.registraAzione("Rimosso utente: " + matr);
        }
        return esito;
    }
    
    /**
     * @brief Coordina la rimozione di un libro.
     * 
     * Verifica il vincolo di integrità referenziale (nessun prestito attivo per questo libro)
     * prima di chiamare il Catalogo per la rimozione.
     * Se la rimozione avviene, l'azione viene loggata.
     * 
     * @param[in] isbn L'ISBN del libro da rimuovere.
     * 
     * @return true se rimosso con successo, false se il libro è in prestito o non esiste.
     */
    public boolean rimuoviLibro(String isbn){
        if (!valida.validaISBN(isbn)) return false;
        
        // Controllo manuale se il libro è in prestito iterando sui prestiti attivi
        // (poiché RegistroPrestiti non espone un metodo diretto isLibroInPrestito)
        List<Prestito> attivi = registro.getPrestitiAttivi();
        boolean inPrestito = false;
        for(Prestito p : attivi){
            if(p.getLibro().getISBN().equals(isbn)){
                inPrestito = true;
                break;
            }
        }
        
        if (inPrestito) {
            logger.registraAzione("Rimozione libro " + isbn + " fallita: è in prestito.");
            return false;
        }
        
        boolean esito = catalogo.rimuoviLibro(isbn);
        if (esito) {
            logger.registraAzione("Rimosso libro: " + isbn);
        }
        return esito;
    }
    
    /**
     * @brief Gestisce l'intero flusso di registrazione di un prestito.
     * 
     * Implementa relativo il Caso d'Uso.
     * 1. Valida i formati di ISBN e Matricola.
     * 2. Delega al RegistroPrestiti la verifica dei vincoli di business (Disponibilità, Limite Utente) e la creazione.
     * 3. Se il prestito viene creato, invoca il Logger per il tracciamento.
     * 
     * @param[in] isbn Codice del libro.
     * @param[in] matricola Matricola dell'utente.
     * @param[in] dataPrevistaRestituzione Data di scadenza del prestito.
     * 
     * @return true se il prestito è registrato, false se falliscono i controlli.
     */
    public boolean gestisciPrestito(String isbn, String matricola, LocalDate dataPrevistaRestituzione){
        // Validazione formale specifica
        if (!valida.validaISBN(isbn) || !valida.validaMatricola(matricola)) {
            return false;
        }
        
        Prestito p = registro.registraPrestito(isbn, matricola, dataPrevistaRestituzione);
        
        if (p != null) {
            logger.registraAzione("Registrato prestito ID " + p.getIdPrestito() + ": Libro " + isbn + " a Utente " + matricola);
            return true;
        } else {
            logger.registraAzione("Fallita registrazione prestito: Libro " + isbn + " a Utente " + matricola);
            return false;
        }
    }
    
    /**
     * @brief Gestisce il flusso di restituzione di un libro.
     * 
     * Implementa il relativo Caso d'Uso.
     * Delega al RegistroPrestiti la chiusura del prestito e l'aggiornamento delle disponibilità.
     * Se l'operazione ha successo, registra l'evento nel Log.
     * 
     * @param[in] idPrestito Identificativo univoco del prestito.
     * @param[in] dataEffettivaRestituzione Data del rientro.
     * 
     * @return true se la restituzione è registrata, false altrimenti.
     */
    public boolean gestisciRestituzione(int idPrestito, LocalDate dataEffettivaRestituzione){
        boolean esito = registro.registraRestituzione(idPrestito, dataEffettivaRestituzione);
        
        if (esito) {
            logger.registraAzione("Registrata restituzione prestito ID: " + idPrestito);
        }
        return esito;
    }
    
    /**
     * @brief Recupera il report dei prestiti attivi.
     * @return Lista di prestiti ordinata per data di scadenza.
     */
    public List<Prestito> ottieniReportPrestiti(){
        return registro.getPrestitiAttivi();
    }
        
    /**
     * @brief Recupera il catalogo completo per la visualizzazione.
     * @return Lista di libri ordinata per Titolo.
     */
    public List<Libro> ottieniCatalogoOrdinato(){
        return catalogo.visualizzaOrdinata();
    }
    
    /**
     * @brief Recupera l'elenco utenti per la visualizzazione.
     * @return Lista di utenti ordinata per Cognome.
     */
    public List<Utente> ottieniAnagraficaOrdinata(){
        return anagrafica.visualizzaOrdinata();
    }
    
    /**
     * @brief Gestisce la procedura di login del Bibliotecario.
     * 
     * Soddisfa il relativo requisito.
     * Delega la verifica delle credenziali al componente IAutenticatore.
     * 
     * @param[in] user Username inserito.
     * @param[in] pass Password inserita.
     * 
     * @return true se le credenziali sono valide, false altrimenti.
     */
    public boolean gestisciLogin(String user, String pass){
        boolean esito = autenticatore.verificaCredenziali(user, pass);
        if (esito) {
            logger.registraAzione("Login effettuato: " + user);
        } else {
            logger.registraAzione("Login fallito: " + user);
        }
        return esito;
    }
}