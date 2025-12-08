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
    
    public AnagraficaTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of aggiungiUtente method, of class Anagrafica.
     */
    @Test
    public void testAggiungiUtente() {
        System.out.println("aggiungiUtente");
        Utente u = null;
        Anagrafica instance = new Anagrafica();
        boolean expResult = false;
        boolean result = instance.aggiungiUtente(u);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rimuoviUtente method, of class Anagrafica.
     */
    @Test
    public void testRimuoviUtente() {
        System.out.println("rimuoviUtente");
        String matricola = "";
        Anagrafica instance = new Anagrafica();
        boolean expResult = false;
        boolean result = instance.rimuoviUtente(matricola);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of modificaUtente method, of class Anagrafica.
     */
    @Test
    public void testModificaUtente() {
        System.out.println("modificaUtente");
        String matricola = "";
        Utente u = null;
        Anagrafica instance = new Anagrafica();
        boolean expResult = false;
        boolean result = instance.modificaUtente(matricola, u);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUtente method, of class Anagrafica.
     */
    @Test
    public void testGetUtente() {
        System.out.println("getUtente");
        String matricola = "";
        Anagrafica instance = new Anagrafica();
        Utente expResult = null;
        Utente result = instance.getUtente(matricola);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of cercaUtente method, of class Anagrafica.
     */
    @Test
    public void testCercaUtente() {
        System.out.println("cercaUtente");
        String query = "";
        String campo = "";
        Anagrafica instance = new Anagrafica();
        List<Utente> expResult = null;
        List<Utente> result = instance.cercaUtente(query, campo);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of visualizzaOrdinata method, of class Anagrafica.
     */
    @Test
    public void testVisualizzaOrdinata() {
        System.out.println("visualizzaOrdinata");
        Anagrafica instance = new Anagrafica();
        List<Utente> expResult = null;
        List<Utente> result = instance.visualizzaOrdinata();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
