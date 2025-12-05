/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.io;

import java.util.*;

/**
 * @brief Interfaccia per il sistema di tracciamento (Logging).
 * 
 * Definisce il contratto per la registrazione delle operazioni di sistema,
 * supportando il requisito funzionale di Audit Trail.
 * 
 * Permette di disaccoppiare la logica di tracciamento dalla specifica
 * implementazione di persistenza, garantendo che ogni modifica
 * ai dati (inserimento, modifica, eliminazione) venga storicizzata.
 */
public interface ILogger {
   
    
    /**
     * @brief Registra una nuova operazione nel sistema di log.
     * 
     * Metodo fondamentale per il "Flusso di aggiornamento audit trail".
     * Deve essere invocato dai Business Flow principali ogni volta che
     * avviene una modifica ai dati persistenti.
     * 
     * @param[in] azione La descrizione testuale dell'evento da tracciare (es. tipo operazione e dati chiave).
     */
    public void registraAzione(String azione){
    }
    
    
    /**
     * @brief Recupera lo storico delle operazioni registrate.
     * 
     * Permette il caricamento dei log dall'archivio persistente, utile
     * per analisi o verifiche amministrative successive.
     * 
     *@return Una lista di stringhe contenente i record di log recuperati.
     */
    public List<String> caricaLog(){
    }
    
    
    /**
     * @brief Rende persistente lo stato corrente dei log.
     * 
     * Implementa la scrittura su file esterno come richiesto dallo specifico Caso d'Uso e [UC-7] e dal relativo requisito di Interfaccia Esterna.
     * Nota: Secondo i flussi alternativi, eventuali fallimenti in questa operazione
     * (es. file non accessibile) non devono interrompere il flusso principale dell'applicazione,
     * in quanto il logging è considerato a bassa priorità rispetto all'operatività.
     */
    public void salvaLog(){
    }
    
    
    /**
     * @brief Restituisce la lista dei log per la visualizzazione.
     * 
     * Fornisce accesso in lettura ai dati di audit correnti presenti in memoria.
     * 
     * @return La lista dei log.
     */
    public List<String> visualizzaLog(){
    }
    
    
}
