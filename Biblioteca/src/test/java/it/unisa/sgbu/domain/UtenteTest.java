/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class UtenteTest {
    
    private Utente utente;
    
    /**
     * @brief Fixture di test: Configurazione iniziale.
     * Questo metodo viene eseguito automaticamente prima di ogni singolo test.
     * Si occupa di istanziare un nuovo oggetto Utente per garantire 
     * che i test siano indipendenti l'uno dall'altro.
     */
    @BeforeEach
    public void setUp() {
        // Creo un utente "pulito" senza prestiti
        utente = new Utente("0123456789", "Mario", "Rossi", "m.rossi@unisa.it");
    }

    /**
     * @brief Test del costruttore e stato iniziale.
     */
    @Test
    public void testCostruttore() {
        assertEquals("0123456789", utente.getMatricola());
        assertEquals("Mario", utente.getNome());
        assertEquals("Rossi", utente.getCognome());
        assertEquals("m.rossi@unisa.it", utente.getEmail());
        
        // Verifica che la lista dei prestiti esista e sia vuota (dimensione 0)
        assertNotNull(utente.getPrestitiAttivi(), "La lista prestiti non deve essere null");
        assertEquals(0, utente.getNumeroPrestitiAttivi(), "La lista prestiti deve essere inizialmente vuota (0)");
    }
    
    /**
     * @brief Test del metodo verificaLimite().
     * @test Verifica il comportamento del controllo limiti in tre scenari:
     * 1. Utente con 0 prestiti (Sotto soglia) -> Deve restituire TRUE.
     * 2. Utente con 2 prestiti (Sotto soglia) -> Deve restituire TRUE.
     * 3. Utente con 3 prestiti (Limite raggiunto) -> Deve restituire FALSE.
     */
    @Test
    public void testVerificaLimite() {
        // Caso 1: 0 prestiti
        assertTrue(utente.verificaLimite(), "Con 0 prestiti l'utente deve poter operare");
        
        // Caso 2: 2 prestiti
        utente.aggiungiPrestito(new Prestito(1, null, utente, null, null));
        utente.aggiungiPrestito(new Prestito(2, null, utente, null, null));
        assertTrue(utente.verificaLimite(), "Con 2 prestiti l'utente deve poter operare");
        
        // Caso 3: 3 prestiti (limite raggiunto)
        utente.aggiungiPrestito(new Prestito(3, null, utente, null, null));
        assertFalse(utente.verificaLimite(), "Con 3 prestiti il limite deve risultare raggiunto");
    }

    /**
     * @brief Test del metodo aggiungiPrestito().
     * @test Verifica la corretta aggiunta di un prestito alla lista:
     * 1. Aggiunta valida: la dimensione della lista deve incrementare.
     * 2. Aggiunta oltre limite: se l'utente ha già 3 prestiti, il quarto non deve essere inserito.
     * 3. Robustezza: l'aggiunta di un oggetto null non deve modificare la lista.
     */
    @Test
    public void testAggiungiPrestito() {
        // 1. Aggiunta valida
        Prestito p1 = new Prestito(1, null, utente, null, null);
        utente.aggiungiPrestito(p1);
        assertEquals(1, utente.getNumeroPrestitiAttivi(), "Il numero di prestiti deve essere 1");
        
        // Riempio fino al limite
        utente.aggiungiPrestito(new Prestito(2, null, utente, null, null));
        utente.aggiungiPrestito(new Prestito(3, null, utente, null, null));
        assertEquals(3, utente.getNumeroPrestitiAttivi());
        
        // 2. Aggiunta oltre limite (FC-2)
        utente.aggiungiPrestito(new Prestito(4, null, utente, null, null));
        assertEquals(3, utente.getNumeroPrestitiAttivi(), "Non deve essere possibile superare il limite di 3");
        
        // 3. Robustezza (null)
        utente.aggiungiPrestito(null);
        assertEquals(3, utente.getNumeroPrestitiAttivi(), "L'aggiunta di null deve essere ignorata");
    }

    /**
     * @brief Test del metodo rimuoviPrestito().
     * @test Verifica la rimozione di un prestito dalla lista:
     * 1. Rimozione valida: un prestito esistente viene rimosso e il contatore decresce.
     * 2. Rimozione non valida: tentare di rimuovere un prestito non presente non deve alterare la lista.
     */
    @Test
    public void testRimuoviPrestito() {
        Prestito p1 = new Prestito(1, null, utente, null, null);
        utente.aggiungiPrestito(p1);
        assertEquals(1, utente.getNumeroPrestitiAttivi());
        
        // 1. Rimozione valida
        utente.rimuoviPrestito(p1);
        assertEquals(0, utente.getNumeroPrestitiAttivi(), "La lista deve tornare vuota dopo la rimozione");
        
        // 2. Rimozione di oggetto non presente (o nuovo oggetto uguale)
        Prestito p2 = new Prestito(2, null, utente, null, null);
        utente.rimuoviPrestito(p2); // Non c'è nella lista
        assertEquals(0, utente.getNumeroPrestitiAttivi(), "Rimuovere un elemento inesistente non deve cambiare lo stato");
    }

    
    /**
     * @brief Test del metodo toString().
     * @test Verifica che il metodo toString restituisca la matricola dell'utente,
     * come atteso per la rappresentazione testuale dell'oggetto (es. nelle tabelle o log).
     */
    @Test
    public void testToString() {
        // Il metodo toString() è stato sovrascritto per restituire la Matricola
        String risultatoAtteso = "0123456789";
        
        assertEquals(risultatoAtteso, utente.toString(), "Il metodo toString() dovrebbe restituire la matricola dell'utente");
    }
}
