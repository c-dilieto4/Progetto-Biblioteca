/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.service;

import it.unisa.sgbu.domain.Utente;
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
public class AnagraficaTest {
    
    private Anagrafica anagrafica;
    private Utente u1;
    private Utente u2;
    
    //anagrafica con 2 utenti
    @BeforeEach
    public void setUp() {
        anagrafica = new Anagrafica();
        u1 = new Utente("0123456789", "Mario", "Rossi", "mario@email.it");
        u2 = new Utente("9876543210", "Luigi", "Verdi", "luigi@email.it");
    }
    
    /**
     * @brief Test di aggiuntaUtente.
     * Verifica l'inserimento corretto e il blocco dei duplicati.
     */
    @Test
    public void testAggiungiUtente() {
        // Aggiunta valida
        assertTrue(anagrafica.aggiungiUtente(u1), "L'utente dovrebbe essere aggiunto");
        assertEquals(u1, anagrafica.getUtente("0123456789"), "L'utente recuperato deve coincidere");
        
        // Aggiunta duplicata (stessa matricola)
        assertFalse(anagrafica.aggiungiUtente(u1), "Non si può aggiungere una matricola già esistente");
        
        // Aggiunta null (Robustezza)
        assertFalse(anagrafica.aggiungiUtente(null), "Non si può aggiungere null");
    }
    
    /**
     * @brief Test di rimuoviUtente.
     * Verifica la rimozione corretta e la gestione di utenti inesistenti.
     * Nota: Il controllo sui prestiti attivi è delegato al GUIController o RegistroPrestiti,
     * qui testiamo solo la rimozione dalla mappa.
     */
    @Test
    public void testRimuoviUtente() {
        
        anagrafica.aggiungiUtente(u1);
        
        // Rimozione valida
        assertTrue(anagrafica.rimuoviUtente("0123456789"), "Rimozione dovrebbe avere successo");
        assertNull(anagrafica.getUtente("0123456789"), "L'utente non deve più esistere");
        
        // Rimozione non valida (utente non esiste)
        assertFalse(anagrafica.rimuoviUtente("0000000000"), "Rimozione di utente inesistente deve fallire");
    }
    
    /**
     * @brief Test della modifica
     * @test Verifica tre scenari di modifica:
     * 1. Modifica dati anagrafici (Nome/Cognome) mantenendo la matricola -> Successo.
     * 2. Modifica della matricola in una nuova non esistente -> Successo (la vecchia deve sparire).
     * 3. Modifica della matricola verso una già esistente -> Fallimento (Vincolo unicità).
     */
    @Test
    public void testModificaUtente() {
        
        // Setup: Aggiungo Mario Rossi e Luigi Verdi
        anagrafica.aggiungiUtente(u1); // Matr: 0123456789
        anagrafica.aggiungiUtente(u2); // Matr: 9876543210
        
        // --- CASO 1: Modifica semplice (cambio email) ---
        Utente u1Modificato = new Utente("0123456789", "Mario", "Rossi", "nuova@email.it");
        boolean esito = anagrafica.modificaUtente("0123456789", u1Modificato);
        
        assertTrue(esito, "La modifica dati semplici deve riuscire");
        assertEquals("nuova@email.it", anagrafica.getUtente("0123456789").getEmail());
        
        // --- CASO 2: Modifica Matricola (Valida) ---
        // Cambio la matricola di Mario da "0123456789" a "1111111111"
        Utente u1NuovaMatr = new Utente("1111111111", "Mario", "Rossi", "nuova@email.it");
        esito = anagrafica.modificaUtente("0123456789", u1NuovaMatr);
        
        assertTrue(esito, "Il cambio di matricola (se univoco) deve riuscire");
        assertNull(anagrafica.getUtente("0123456789"), "La vecchia matricola non deve più esistere");
        assertNotNull(anagrafica.getUtente("1111111111"), "La nuova matricola deve essere presente");
        
        // --- CASO 3: Modifica Matricola (Duplicata) ---
        // Provo a cambiare la matricola di Mario (ora "1111111111") in quella di Luigi ("9876543210")
        Utente uConflict = new Utente("9876543210", "Mario", "Ladro", "x@x.it");
        esito = anagrafica.modificaUtente("1111111111", uConflict);
        
        assertFalse(esito, "Non si può modificare la matricola se la nuova esiste già");
        // Verifico che Mario sia rimasto con la matricola "1111111111"
        assertNotNull(anagrafica.getUtente("1111111111"));
    }
    
    /**
     * @brief Test di ricerca.
     * Verifica che la ricerca funzioni per Cognome e Matricola.
     */
    @Test
    public void testCercaUtente() {
        System.out.println("Test Cerca Utente");
        
        anagrafica.aggiungiUtente(u1); // Rossi
        anagrafica.aggiungiUtente(u2); // Verdi
        
        // Cerca per Cognome
        List<Utente> risultati = anagrafica.cercaUtente("Rossi", "Cognome");
        assertEquals(1, risultati.size());
        assertEquals("Mario", risultati.get(0).getNome());
        
        // Cerca per Matricola
        risultati = anagrafica.cercaUtente("9876543210", "Matricola");
        assertEquals(1, risultati.size());
        assertEquals("Luigi", risultati.get(0).getNome());
        
        // Cerca non trovato
        risultati = anagrafica.cercaUtente("Bianchi", "Cognome");
        assertTrue(risultati.isEmpty());
    }
    
    /**
     * @brief Test del metodo visualizzaOrdinata().
     * @test Verifica che la lista restituita sia ordinata alfabeticamente per Cognome.
     * Inserisce utenti in ordine sparso e controlla che l'output sia: 
     * 1. Bianchi
     * 2. Rossi
     * 3. Verdi
     */
    @Test
    public void testVisualizzaOrdinata() {
        
        // Preparo dati disordinati
        Utente uA = new Utente("001", "A", "Verdi", "v@v.it");
        Utente uB = new Utente("002", "B", "Bianchi", "b@b.it");
        Utente uC = new Utente("003", "C", "Rossi", "r@r.it");
        
        // Inserisco in ordine casuale
        anagrafica.aggiungiUtente(uA); // Verdi
        anagrafica.aggiungiUtente(uB); // Bianchi
        anagrafica.aggiungiUtente(uC); // Rossi
        
        // Recupero la lista ordinata
        List<Utente> lista = anagrafica.visualizzaOrdinata();
        
        assertEquals(3, lista.size());
        
        // Verifica l'ordine (Bianchi -> Rossi -> Verdi)
        assertEquals("Bianchi", lista.get(0).getCognome(), "Il primo deve essere Bianchi");
        assertEquals("Rossi", lista.get(1).getCognome(), "Il secondo deve essere Rossi");
        assertEquals("Verdi", lista.get(2).getCognome(), "Il terzo deve essere Verdi");
    }
}
