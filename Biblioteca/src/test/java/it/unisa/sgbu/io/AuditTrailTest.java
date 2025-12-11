/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author dilie
 */
public class AuditTrailTest {
    
    private AuditTrail auditTrail;
    private IArchivioDati archivio;
    private final String TEST_DIR = "./test_logs/";
    private final String FILE_LOG = "audit_log.dat";
    
    @BeforeEach
    public void setUp() {
        // Uso FileArchivio reale per testare l'integrazione I/O
        archivio = new FileArchivio(TEST_DIR, null);
        auditTrail = new AuditTrail(new ArrayList<>(), archivio);
    }
    
    /**
     * @brief Test registrazione azione.
     * Verifica che la lista cresca e che il messaggio contenga il timestamp.
     */
    @Test
    public void testRegistraAzione() {
        
        auditTrail.registraAzione("Inserimento Utente Mario");
        
        List<String> logs = auditTrail.visualizzaLog();
        assertEquals(1, logs.size());
        
        // Verifica formato: Deve iniziare con [Data...
        String record = logs.get(0);
        assertTrue(record.startsWith("["), "Il log deve avere un timestamp");
        assertTrue(record.contains("Inserimento Utente Mario"), "Il messaggio deve essere conservato");
    }

    /**
     * @brief Test persistenza (Salvataggio e Caricamento).
     */
    @Test
    public void testPersistenzaLog() {
        
        // Registro azioni
        auditTrail.registraAzione("Azione 1");
        auditTrail.registraAzione("Azione 2");
        
        // salvaLog() viene chiamato automaticamente dentro registraAzione
        
        // Simulo riavvio sistema: creo un NUOVO audit trail
        AuditTrail nuovoAudit = new AuditTrail(new ArrayList<>(), archivio);
        
        // Carico i log
        List<String> logsCaricati = nuovoAudit.caricaLog();
        
        // Verifico
        assertNotNull(logsCaricati);
        assertEquals(2, logsCaricati.size(), "Devo ritrovare 2 azioni salvate");
        assertTrue(logsCaricati.get(1).contains("Azione 2"));
    }
    
    /**
     * @brief Test Robustezza (Input null o vuoti).
     */
    @Test
    public void testInputNonValidi() {
        
        auditTrail.registraAzione(null);
        auditTrail.registraAzione("");
        
        assertEquals(0, auditTrail.visualizzaLog().size(), "Input null o vuoti non devono generare log");
    }
    
    @AfterEach
    public void tearDown() {
        // PULIZIA
        File f = new File(TEST_DIR + FILE_LOG);
        if (f.exists()) f.delete();
        File d = new File(TEST_DIR);
        if (d.exists()) d.delete();
    }
    
}
