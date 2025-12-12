/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.service;

import it.unisa.sgbu.domain.Libro;
import it.unisa.sgbu.domain.Prestito;
import it.unisa.sgbu.domain.Utente;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class RegistroPrestitiTest {
    
    private RegistroPrestiti registro;
    private Catalogo catalogo;
    private Anagrafica anagrafica;
    
    private Libro libroTest;
    private Utente utenteTest;
    
    
    /**
     * @brief Fixture di test: Configurazione iniziale.
     * Questo metodo viene eseguito automaticamente prima di ogni singolo test.
     * Si occupa di istanziare un nuovo oggetto RegistroPrestiti.
     */
    @BeforeEach
    public void setUp() {
        // Inizializzo i servizi dipendenti
        catalogo = new Catalogo();
        anagrafica = new Anagrafica();
        registro = new RegistroPrestiti(catalogo, anagrafica);
        
        // Creo dati di test
        // Libro con 1 sola copia per testare l'esaurimento
        List<String> autori = new ArrayList<>();
        autori.add("Autore Test");
        libroTest = new Libro("ISBN-123", "Libro Test", autori, 2020, 1);
        
        utenteTest = new Utente("MATR-001", "Mario", "Rossi", "email@test.it");
        
        // Popolo i "database" in memoria
        catalogo.aggiungiLibro(libroTest);
        anagrafica.aggiungiUtente(utenteTest);
    }
    
    
    /**
    * @brief Test della registrazione di un prestito con successo.
    */
    @Test
    public void testRegistraPrestito_Successo() {
        
        LocalDate scadenza = LocalDate.now().plusDays(30);
        Prestito p = registro.registraPrestito("ISBN-123", "MATR-001", scadenza);
        
        assertNotNull(p, "Il prestito dovrebbe essere creato con successo");
        assertEquals(libroTest, p.getLibro());
        assertEquals(utenteTest, p.getUtente());
        
        // Verifica
        assertEquals(0, libroTest.getCopieDisponibili(), "Le copie del libro devono scendere a 0");
        assertEquals(1, utenteTest.getNumeroPrestitiAttivi(), "L'utente deve avere 1 prestito attivo");
        assertEquals(1, registro.getPrestitiAttivi().size(), "Il registro deve contenere 1 prestito");
    }

    
    /**
    * @brief Test della registrazione di un prestito con fallimento 
    * a causa della mancanza di copie disponibili.
    */
    @Test
    public void testRegistraPrestito_Fallimento_CopieEsaurite() {
        
        // Consumo l'unica copia disponibile
        registro.registraPrestito("ISBN-123", "MATR-001", LocalDate.now().plusDays(30));
        
        // Tento un secondo prestito dello stesso libro (con un altro utente per isolare il test)
        Utente utente2 = new Utente("MATR-002", "Luigi", "Verdi", "l.verdi@test.it");
        anagrafica.aggiungiUtente(utente2);
        
        Prestito p2 = registro.registraPrestito("ISBN-123", "MATR-002", LocalDate.now().plusDays(30));
        
        assertNull(p2, "Il prestito non deve essere creato se non ci sono copie");
        assertEquals(0, utente2.getNumeroPrestitiAttivi(), "L'utente 2 non deve avere prestiti");
    }

    
    /**
    * @brief Test della registrazione di un prestito con fallimeto a causa del limite prestiti attivi.
    */
    @Test
    public void testRegistraPrestito_Fallimento_LimiteUtente() {
        
        List<String> autori = new ArrayList<>();
        autori.add("A");
        Libro libroInfinito = new Libro("ISBN-999", "Libro Infinito", autori, 2020, 100);
        catalogo.aggiungiLibro(libroInfinito);
        
        // Assegno 3 prestiti all'utente
        registro.registraPrestito("ISBN-999", "MATR-001", LocalDate.now().plusDays(30));
        registro.registraPrestito("ISBN-999", "MATR-001", LocalDate.now().plusDays(30));
        registro.registraPrestito("ISBN-999", "MATR-001", LocalDate.now().plusDays(30));
        
        assertEquals(3, utenteTest.getNumeroPrestitiAttivi());
        
        // Tento il 4° prestito
        Prestito p4 = registro.registraPrestito("ISBN-999", "MATR-001", LocalDate.now().plusDays(30));
        
        assertNull(p4, "L'utente non può superare i 3 prestiti");
        assertEquals(3, utenteTest.getNumeroPrestitiAttivi(), "I prestiti devono rimanere 3");
    }

    
    /**
    * @brief Test della registrazione di una restituzione
    */
    @Test
    public void testRegistraRestituzione() {
        
        // Creo un prestito
        Prestito p = registro.registraPrestito("ISBN-123", "MATR-001", LocalDate.now().plusDays(30));
        assertNotNull(p);
        int idPrestito = p.getIdPrestito();
        
        // Eseguo restituzione
        boolean esito = registro.registraRestituzione(idPrestito, LocalDate.now());
        
        assertTrue(esito, "La restituzione deve andare a buon fine");
        assertNotNull(p.getDataEffettivaRestituzione(), "Il prestito deve avere una data di chiusura");
        
        // Verifiche stato
        assertEquals(1, libroTest.getCopieDisponibili(), "La copia deve tornare disponibile");
        assertEquals(0, utenteTest.getNumeroPrestitiAttivi(), "L'utente non deve avere più prestiti attivi");
        
        // Il registro non deve mostrare questo prestito tra quelli "Attivi"
        assertEquals(0, registro.getPrestitiAttivi().size());
    }
    
}
