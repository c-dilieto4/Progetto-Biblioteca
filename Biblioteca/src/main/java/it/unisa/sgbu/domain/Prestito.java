/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.domain;

import java.io.Serializable;
import java.time.*;

/**
 * @brief Rappresenta una transazione di prestito attiva o conclusa.
 * 
 * Questa classe associa un utente a un libro e gestisce le date temporali
 * del prestito, conformemente alla specifica dei dati.
 * Gestisce inoltre lo stato di ritardo nella restituzione.
 * 
 * Implementa l'interfaccia java.io.Serializable per consentire la serializzazione
 * e il salvataggio persistente dell'oggetto su file.
 */
public class Prestito implements Serializable{
    
    private final int idPrestito;
    private Libro libro;
    private Utente utente;
    private LocalDate dataInizio;
    private LocalDate dataPrevistaRestituzione;
    private LocalDate dataEffettivaRestituzione;
    private boolean inRitardo; 
    
    
    /**
     * @brief Costruttore della classe Prestito.
     * 
     * Inizializza un nuovo prestito attivo. La creazione di questa istanza
     * avviene tipicamente all'interno del flusso di "Registrazione nuovo prestito",
     * dopo aver superato i controlli di disponibilità e limite prestiti.
     * 
     * @param[in] idPrestito Identificativo numerico del prestito.
     * @param[in] libro L'oggetto Libro da prestare (deve avere copie disponibili).
     * @param[in] utente L'oggetto Utente che richiede il prestito (non deve aver superato il limite).
     * @param[in] dataInizio La data odierna di avvio prestito.
     * @param[in] dataPrevistaRestituzione La data di scadenza calcolata.
     * 
     * @pre
     * - libro != null e il libro deve avere copie disponibili (copie > 0), rispettando il relativo Vincolo.
     * - utente != null e l'utente deve avere meno di 3 prestiti attivi, rispettando il relativo Vincolo.
     * - dataInizio != null e dataPrevistaRestituzione != null.
     * - dataPrevistaRestituzione >= dataInizio.
     * 
     * @post
     * - Il prestito è creato ed è attivo (dataEffettivaRestituzione è null).
     * - InRitardo è inizializzato a false.
     * - Coerenza.
     */
    public Prestito(int idPrestito, Libro libro, Utente utente, LocalDate dataInizio, LocalDate dataPrevistaRestituzione){
        this.idPrestito = idPrestito;
        this.libro = libro;
        this.utente = utente;
        this.dataInizio = dataInizio;
        this.dataPrevistaRestituzione = dataPrevistaRestituzione;
        this.inRitardo = false;
    }

    
    /**
     * @brief Restituisce l'ID del prestito.
     * @return L'intero identificativo.
     */
    public int getIdPrestito() {
        return idPrestito;
    }

    
    /**
     * @brief Restituisce il libro associato al prestito.
     * @return L'oggetto Libro.
     */
    public Libro getLibro() {
        return libro;
    }

    
    /**
     * @brief Restituisce l'utente associato al prestito.
     * @return L'oggetto Utente.
     */
    public Utente getUtente() {
        return utente;
    }
    
    /**
     * @brief Restituisce la data di inizio del prestito.
     * @return Un oggetto LocalDate rappresentante la data di avvio.
     */
    public LocalDate getDataInizio() {
        return dataInizio;
    }

    /**
     * @brief Restituisce la data prevista per la restituzione.
     * Questa data rappresenta la scadenza entro cui l'utente deve restituire il libro
     * per evitare di incorrere in ritardi. È calcolata al momento della creazione del prestito.
     * @return Un oggetto LocalDate rappresentante la data di scadenza.
     */
    public LocalDate getDataPrevistaRestituzione() {
        return dataPrevistaRestituzione;
    }
    
    public LocalDate getDataEffettivaRestituzione() {
        return dataEffettivaRestituzione;
    }
    

    /**
     * @brief Registra la restituzione del libro chiudendo il prestito.
     * 
     * Implementa parte del "Flusso di registrazione restituzione".
     * Imposta la data effettiva di restituzione. Se questa data è successiva
     * alla data prevista, deve essere attivato il flusso di segnalazione ritardo.
     * 
     * @param[in] dataEffettiva La data in cui il libro viene fisicamente restituito.
     * 
     * @pre
     * - dataEffettiva != null.
     * - dataEffettiva <= dataInizio: Non è possibile restituire un libro prima di averlo preso in prestito.
     * 
     * @post
     * - Il prestito è considerato chiuso.
     * - Il flag InRitardo è impostato a true se dataEffettiva > dataPrevistaRestituzione, altrimenti resta false.
     */
    public void chiudiPrestito(LocalDate dataEffettiva){
        if (dataEffettiva != null) {
            this.dataEffettivaRestituzione = dataEffettiva;
            // Controlla se la data effettiva è DOPO la data prevista
            if (dataEffettiva.isAfter(dataPrevistaRestituzione)) {
                segnaRitardo();
            }
        }
    }


    /**
     * @brief Verifica se il prestito è in ritardo.
     * Controlla lo stato temporale del prestito.
     * - Se il prestito è CHIUSO: confronta la data effettiva con la scadenza.
     * - Se il prestito è ATTIVO: confronta la data di oggi con la scadenza.
     * * @return true se la data di confronto è successiva alla scadenza, false altrimenti.
     */
    public boolean verificaRitardo(){
        LocalDate dataConfronto;
        
        if (this.dataEffettivaRestituzione != null) {
            // Caso 1: Il libro è stato restituito.
            // Uso la data in cui è avvenuta la restituzione.
            dataConfronto = this.dataEffettivaRestituzione;
        } else {
            // Caso 2: Il prestito è ancora in corso.
            // Uso la data di oggi per vedere se sono fuori tempo massimo.
            dataConfronto = LocalDate.now();
        }
        
        // Restituisce true se la data di confronto è DOPO la data prevista
        return dataConfronto.isAfter(this.dataPrevistaRestituzione);
    }


    /**
     * @brief Contrassegna il prestito come "In Ritardo".
     * 
     * Aggiorna lo stato interno del prestito per riflettere il mancato rispetto
     * della scadenza. Questo metodo supporta la funzionalità di gestione dei ritardi
     * e l'estensione del caso d'uso.
     * 
     * @pre
     * - La data corrente o la data di restituzione effettiva deve essere successiva alla data prevista.
     * 
     * @post
     * - Il flag InRitardo è impostato a true.
     */
    public void segnaRitardo(){
        this.inRitardo = true;
    }
    
    
}
