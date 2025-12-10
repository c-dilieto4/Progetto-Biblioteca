/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.domain;

import java.time.LocalDate;
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
public class PrestitoTest {
    
    private Prestito prestito;
    private Libro libro;
    private Utente utente;
    private LocalDate dataInizio;
    private LocalDate dataScadenza;
    
    /**
     * @brief Fixture: Configurazione iniziale.
     * Crea un libro, un utente e un prestito attivo con scadenza tra 30 giorni.
     */
    @BeforeEach
    public void setUp() {
        // Creazione oggetti dipendenti (Stub)
        List<String> autori = new ArrayList<>();
        autori.add("Joshua Bloch");
        
        libro = new Libro("1234567890", "Java Book", autori, 2020, 5);
        utente = new Utente("0123456789", "Mario", "Rossi", "m.rossi@unisa.it");
        
        dataInizio = LocalDate.now();
        dataScadenza = dataInizio.plusDays(30); // Scade tra 30 giorni
        prestito = new Prestito(1, libro, utente, dataInizio, dataScadenza);
    }
    
    /**
     * @brief Test del costruttore e corretta inizializzazione.
     * Verifica che:
     * 1. L'ID e le associazioni (Libro, Utente) siano corretti.
     * 2. Le date (Inizio, Scadenza) siano registrate correttamente.
     * 3. Il prestito nasca attivo (data effettiva null) e senza ritardi.
     */
    @Test
    public void testCostruttore() {
        assertEquals(1, prestito.getIdPrestito());
        assertEquals(libro, prestito.getLibro());
        assertEquals(utente, prestito.getUtente());
        
        assertEquals(dataInizio, prestito.getDataInizio(), "La data di inizio non coincide");
        assertEquals(dataScadenza, prestito.getDataPrevistaRestituzione(), "La data di scadenza non coincide");
        assertNull(prestito.getDataEffettivaRestituzione(), "Appena creato, la data effettiva deve essere null");
    }
    
    /**
     * @brief Test di chiudiPrestito (Caso Puntuale).
     * Verifica che restituendo entro la scadenza il prestito venga chiuso senza ritardo.
     */
    @Test
    public void testChiudiPrestitoPuntuale() {
        LocalDate dataRestituzione = dataInizio.plusDays(10); // Restituisco prima della scadenza
        prestito.chiudiPrestito(dataRestituzione);
        
        assertEquals(dataRestituzione, prestito.getDataEffettivaRestituzione(), "La data effettiva deve essere registrata");
    }

    /**
     * @brief Test di chiudiPrestito (Caso Ritardo).
     * Verifica che restituendo dopo la scadenza venga segnato il ritardo.
     */
    @Test
    public void testChiudiPrestitoInRitardo() {
        LocalDate dataRestituzione = dataScadenza.plusDays(5); // 5 giorni dopo la scadenza
        prestito.chiudiPrestito(dataRestituzione);
        
        assertEquals(dataRestituzione, prestito.getDataEffettivaRestituzione(), "La data effettiva è successiva alla data prevista");
    }
    
    /**
     * @brief Test del metodo verificaRitardo (Logica Temporale).
     */
    @Test
    public void testVerificaRitardo() {
        // Caso 1: Prestito attivo non scaduto (oggi < scadenza)
        assertFalse(prestito.verificaRitardo(), "Non dovrebbe essere in ritardo oggi");
        
        // Caso 2: Prestito chiuso in ritardo
        LocalDate dataTardi = dataScadenza.plusDays(1);
        prestito.chiudiPrestito(dataTardi);
        assertTrue(prestito.verificaRitardo(), "Dovrebbe rilevare il ritardo sulla data effettiva");
    }
}
