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
 */
public class Prestito implements Serializable{
    
    private final int idPrestito;
    private Libro libro;
    private Utente utente;
    private LocalDate dataInizio;
    private LocalDate dataPrevistaRestituzione;
    private LocalDate dataEffettivaRestituzione;
    private boolean InRitardo; 
    
    
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
    
    public LocalDate getDataInizio() {
        return dataInizio;
    }

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
     * - dataEffettivaRestituzione == null: Il prestito deve essere ancora attivo per poter essere chiuso.
     * - dataEffettiva >= dataInizio: Non è possibile restituire un libro prima di averlo preso in prestito.
     * 
     * @post
     * - Il prestito è considerato chiuso.
     * - Il flag InRitardo è impostato a true se dataEffettiva > dataPrevistaRestituzione, altrimenti resta false.
     */
    public void chiudiPrestito(LocalDate dataEffettiva){
        
        dataEffettivaRestituzione=dataEffettiva;
        
        if (dataEffettiva.isAfter(dataPrevistaRestituzione)) {
            segnaRitardo();
        }
    }


    /**
     * @brief Verifica se il prestito è in ritardo confrontando la data prevista di restituzione 
     * con la data odierna (o la data effettiva se già chiusa)
     * 
     * Confronta la data effettiva (o corrente) con la data prevista di restituzione
     * come descritto nel "Flusso di segnalazione ritardo".
     * 
     * @return true se dataEffettiva > dataPrevistaRestituzione, altrimenti false.
     */
    public boolean verificaRitardo(){
        
        if (dataEffettivaRestituzione != null) {
            return dataEffettivaRestituzione.isAfter(dataPrevistaRestituzione);
        }
        return LocalDate.now().isAfter(dataPrevistaRestituzione);
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
        InRitardo=true;
    }
    
    public boolean isInRitardo() {
        return InRitardo;
    }
    
    
}
