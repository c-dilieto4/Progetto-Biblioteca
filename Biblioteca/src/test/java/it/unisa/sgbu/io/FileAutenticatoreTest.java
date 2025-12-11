/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.io;

import it.unisa.sgbu.domain.Credenziali;
import java.io.File;
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
public class FileAutenticatoreTest {
    
    private FileAutenticatore autenticatore;
    private IArchivioDati archivio;
    private final String TEST_DIR = "./test_auth/";
    private final String FILE_CREDENZIALI = "credenziali_test.dat";
    
    @BeforeEach
    public void setUp() {
        // Preparo l'archivio (dipendenza)
        archivio = new FileArchivio(TEST_DIR, null);
        
        // Preparo l'autenticatore (SUT)
        autenticatore = new FileAutenticatore(FILE_CREDENZIALI, archivio);
        
        // CREO UN FILE DI CREDENZIALI VALIDE PER IL TEST
        // Username: admin, Password: password
        Credenziali c = new Credenziali("admin", "password");
        archivio.salvaStato(c, FILE_CREDENZIALI);
    }

    /**
     * @brief Test Login Successo.
     */
    @Test
    public void testVerificaCredenziali_Successo() {
        
        boolean esito = autenticatore.verificaCredenziali("admin", "password");
        assertTrue(esito, "Il login dovrebbe riuscire con le credenziali corrette");
    }

    /**
     * @brief Test Login Fallito (Password Errata).
     */
    @Test
    public void testVerificaCredenziali_PassErrata() {
  
        boolean esito = autenticatore.verificaCredenziali("admin", "sbagliata");
        assertFalse(esito, "Il login deve fallire se la password è errata");
    }
    
    /**
     * @brief Test Login Fallito (Username Errato).
     */
    @Test
    public void testVerificaCredenziali_UserErrato() {
        
        boolean esito = autenticatore.verificaCredenziali("pippo", "password");
        assertFalse(esito, "Il login deve fallire se l'username è errato");
    }
    
    /**
     * @brief Test Login con File Mancante.
     * Simula il caso in cui il file delle credenziali non esista ancora.
     */
    @Test
    public void testVerificaCredenziali_FileMancante() {
        
        // Cancello il file creato nel setUp
        File f = new File(TEST_DIR + FILE_CREDENZIALI);
        f.delete();
        
        // Provo a loggarmi
        boolean esito = autenticatore.verificaCredenziali("admin", "password");
        assertFalse(esito, "Se il file non esiste, il login deve fallire per sicurezza");
    }
    
    @AfterEach
    public void tearDown() {
        // Pulizia file
        File f = new File(TEST_DIR + FILE_CREDENZIALI);
        if (f.exists()) f.delete();
        File d = new File(TEST_DIR);
        if (d.exists()) d.delete();
    }
}
