/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.io;

import it.unisa.sgbu.gui.MessaggiInterfaccia;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @brief Implementazione concreta del meccanismo di persistenza su file.
 * 
 * Questa classe gestisce l'interazione diretta con il file system locale per
 * garantire il salvataggio persistente e il ricaricamento dello stato dell'archivio
 * (Libri, Utenti, Prestiti), in conformità con il requisito di Interfaccia Software
 * Soddisfa le esigenze di un'applicazione stand-alone descritte nella sezione "Prospettive del prodotto".
 */
public class FileArchivio implements IArchivioDati{
   
    private String pathDati;
    private ILogger logger;
    
    
    /**
     * @brief Costruttore della classe FileArchivio.
     * 
     * Configura l'accesso all'archivio dati esterno.
     * 
     * @param[in] pathDati Il percorso base nel file system dove salvare/caricare i dati.
     * @param[in] logger L'istanza del logger per registrare eventuali fallimenti critici.
     */
    public FileArchivio(String pathDati, ILogger logger){
        this.pathDati = pathDati;
        this.logger = logger;
        
        // Verifica preliminare: se la cartella base non esiste, prova a crearla
        if (this.pathDati != null) {
            File directory = new File(this.pathDati);
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }
    }
    
    
    /**
     * @brief Salva lo stato di un oggetto su file (Serializzazione).
     * 
     * Implementa la logica di salvataggio persistente richiesta dal caso d'uso
     * "Gestione persistenza dati" alla chiusura del sistema.
     * 
     * Deve rispettare rigorosamente il requisito di Tolleranza agli errori:
     * in caso di eccezione durante la scrittura, il metodo deve fallire in modo sicuro
     * senza corrompere l'ultima versione stabile salvata su disco.
     * 
     * @param[in] dati L'oggetto da serializzare (es. Catalogo, Anagrafica).
     * @param[in] nomeFile Il nome del file di destinazione.
     * 
     * @return true se il salvataggio ha successo, false in caso di errore I/O.
     */
    @Override
    public boolean salvaStato(Object dati, String nomeFile){
        if (dati == null || nomeFile == null || this.pathDati == null) return false;
        
        File fileDestinazione = new File(this.pathDati, nomeFile);
        
        // Catena di Stream: File -> Buffer -> Object 
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(fileDestinazione)))) {
            
            oos.writeObject(dati); // Serializza l'intero oggetto
            return true;
            
        } catch (IOException e) {
            String baseMsg = String.format(MessaggiInterfaccia.ERRORE_GENERICO_SALVATAGGIO, nomeFile);
            String logMessage = baseMsg + " Dettagli: " + e.getMessage();
            
            if (logger != null) {
                logger.registraAzione(logMessage);
            } else {
                System.err.println("[FileArchivio] " + logMessage);
            }
            
            return false;
        }
    }
 
    
    /**
     * @brief Carica lo stato di un oggetto da file (Deserializzazione).
     * 
     * Implementa la logica di caricamento dati all'avvio del sistema.
     * Secondo l'Assunzione n°2, si aspetta una struttura definita (es. file serializzati).
     * L'operazione deve essere ottimizzata per rispettare il requisito prestazionale,
     * che impone un tempo di caricamento massimo di 5 secondi.
     * 
     * @param[in] nomeFile Il nome del file da cui leggere.
     * 
     * @return L'oggetto deserializzato, oppure null se il caricamento fallisce.
     */
    @Override
    public Object caricaStato(String nomeFile){
        if (nomeFile == null || this.pathDati == null) return null;
        
        File fileSorgente = new File(this.pathDati, nomeFile);
        if (!fileSorgente.exists()) return null;
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(fileSorgente)))) {
            
            return ois.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
            String baseMsg = String.format(MessaggiInterfaccia.AVVISO_CARICAMENTO_FALLITO, nomeFile);
            String logMessage = baseMsg + " Dettagli: " + e.getMessage();
            
            if (logger != null) {
                logger.registraAzione(logMessage);
            } else {
                System.err.println("[FileArchivio] " + logMessage);
            }
            
            return null;
        }
    }
    
    
    /**
     * @brief Verifica se un file di dati esiste nel percorso specificato.
     * 
     * Metodo di utilità usato durante l'avvio del sistema per determinare
     * se procedere con il caricamento o avviare l'applicazione con un archivio vuoto.
     * 
     * @param[in] nomeFile Il nome del file da controllare.
     * 
     * @return true se il file esiste, false altrimenti.
     */
    @Override
    public boolean verificaEsistenzaFile(String nomeFile){
        if (nomeFile == null || this.pathDati == null) return false;
        File f = new File(this.pathDati, nomeFile);
        return f.exists() && f.isFile();
    }
}
