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
 *
 * @author dilie
 */
public class UtenteTest {
    
    public UtenteTest() {
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
     * Test of getMatricola method, of class Utente.
     */
    @Test
    public void testGetMatricola() {
        System.out.println("getMatricola");
        Utente instance = null;
        String expResult = "";
        String result = instance.getMatricola();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCognome method, of class Utente.
     */
    @Test
    public void testGetCognome() {
        System.out.println("getCognome");
        Utente instance = null;
        String expResult = "";
        String result = instance.getCognome();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of aggiungiPrestito method, of class Utente.
     */
    @Test
    public void testAggiungiPrestito() {
        System.out.println("aggiungiPrestito");
        Prestito p = null;
        Utente instance = null;
        instance.aggiungiPrestito(p);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rimuoviPrestito method, of class Utente.
     */
    @Test
    public void testRimuoviPrestito() {
        System.out.println("rimuoviPrestito");
        Prestito p = null;
        Utente instance = null;
        instance.rimuoviPrestito(p);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of verificaLimite method, of class Utente.
     */
    @Test
    public void testVerificaLimite() {
        System.out.println("verificaLimite");
        Utente instance = null;
        boolean expResult = false;
        boolean result = instance.verificaLimite();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
