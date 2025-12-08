/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.io;

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
public class FileArchivioTest {
    
    public FileArchivioTest() {
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
     * Test of salvaStato method, of class FileArchivio.
     */
    @Test
    public void testSalvaStato() {
        System.out.println("salvaStato");
        Object dati = null;
        String nomeFile = "";
        FileArchivio instance = null;
        boolean expResult = false;
        boolean result = instance.salvaStato(dati, nomeFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of caricaStato method, of class FileArchivio.
     */
    @Test
    public void testCaricaStato() {
        System.out.println("caricaStato");
        String nomeFile = "";
        FileArchivio instance = null;
        Object expResult = null;
        Object result = instance.caricaStato(nomeFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of verificaEsistenzaFile method, of class FileArchivio.
     */
    @Test
    public void testVerificaEsistenzaFile() {
        System.out.println("verificaEsistenzaFile");
        String nomeFile = "";
        FileArchivio instance = null;
        boolean expResult = false;
        boolean result = instance.verificaEsistenzaFile(nomeFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
