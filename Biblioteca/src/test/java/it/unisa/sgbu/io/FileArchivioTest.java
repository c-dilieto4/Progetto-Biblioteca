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



public class FileArchivioTest {
    
    private FileArchivio archivio;
    private final String TEST_DIR = "./test_data/"; // Cartella separata per i test
    private final String TEST_FILE = "archivio_test.dat";
    
    
    /**
     * @brief Fixture di test: Configurazione iniziale.
     * Questo metodo viene eseguito automaticamente prima di ogni singolo test.
     * Si occupa di istanziare un nuovo oggetto FileArchivio.
     */
    @BeforeEach
    public void setUp() {
        // Passo null come logger perché stiamo testando solo la logica di file
        // La classe FileArchivio gestisce il null stampando su System.err, che va bene per i test.
        archivio = new FileArchivio(TEST_DIR, null);
    }
    
    
    /**
     * @brief Test del salvataggio (Serializzazione).
     * Verifica che il file venga creato fisicamente sul disco.
     */
    @Test
    public void testSalvaStato() {
        
        // Creo un oggetto semplice da salvare (una lista di stringhe è Serializable)
        List<String> datiProva = new ArrayList<>();
        datiProva.add("Dato 1");
        datiProva.add("Dato 2");
        
        boolean esito = archivio.salvaStato(datiProva, TEST_FILE);
        
        assertTrue(esito, "Il metodo salvaStato deve restituire true in caso di successo");
        assertTrue(archivio.verificaEsistenzaFile(TEST_FILE), "Il file deve esistere fisicamente dopo il salvataggio");
    }
    
    /**
     * @brief Test del caricamento (Deserializzazione).
     * Verifica che l'oggetto letto sia uguale (nel contenuto) a quello salvato.
     */
    @Test
    public void testCaricaStato() {
        
        // Preparo e salvo i dati
        List<String> datiOriginali = new ArrayList<>();
        datiOriginali.add("Hello World");
        archivio.salvaStato(datiOriginali, TEST_FILE);
        
        // Ricarico
        List<String> datiCaricati = (List<String>) archivio.caricaStato(TEST_FILE);
        
        // Verifico
        assertNotNull(datiCaricati, "L'oggetto caricato non deve essere null");
        assertEquals(1, datiCaricati.size(), "La dimensione della lista deve coincidere");
        assertEquals("Hello World", datiCaricati.get(0), "Il contenuto deve coincidere");
    }
    
    /**
     * @brief Test caricamento di file inesistente.
     * Verifica la robustezza: il sistema non deve crashare ma restituire null.
     */
    @Test
    public void testCaricaFileInesistente() {
        
        Object risultato = archivio.caricaStato("fantasma.dat");
        assertNull(risultato, "Il caricamento di un file che non c'è deve restituire null");
    }
    
    /**
     * @brief Test verifica esistenza.
     */
    @Test
    public void testVerificaEsistenzaFile() {
        
        // Prima del salvataggio
        assertFalse(archivio.verificaEsistenzaFile(TEST_FILE), "Il file non deve esistere prima del salvataggio");
        
        // Dopo il salvataggio
        archivio.salvaStato("Test", TEST_FILE);
        assertTrue(archivio.verificaEsistenzaFile(TEST_FILE), "Il file deve esistere dopo il salvataggio");
    }
    
    // Pulizia: cancello i file creati dopo ogni test per lasciare l'ambiente pulito
    @AfterEach
    public void tearDown() {
        File f = new File(TEST_DIR + TEST_FILE);
        if (f.exists()) {
            f.delete();
        }
        File d = new File(TEST_DIR);
        if (d.exists()) {
            d.delete();
        }
    }
    
}
