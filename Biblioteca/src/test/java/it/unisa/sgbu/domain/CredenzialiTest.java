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

/**
 * @brief Test Unitari per la classe Credenziali.
 * Verifica la corretta creazione dell'oggetto e l'accesso ai dati.
 */
public class CredenzialiTest {
    
    /**
     * @brief Test del costruttore e dei metodi getter.
     * Verifica che i dati passati al costruttore siano recuperabili correttamente.
     */
    @Test
    public void testCreazioneCredenziali() {
        
        String user = "admin";
        String pass = "password";
        
        Credenziali c = new Credenziali(user, pass);
        
        // Verifiche
        assertNotNull(c, "L'oggetto non deve essere null");
        assertEquals(user, c.getUser(), "Lo username deve corrispondere");
        assertEquals(pass, c.getPassword(), "La password deve corrispondere");
    }
}
