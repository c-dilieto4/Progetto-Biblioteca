/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.domain;

import java.time.LocalDate;
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
    
    public PrestitoTest() {
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
     * Test of getIdPrestito method, of class Prestito.
     */
    @Test
    public void testGetIdPrestito() {
        System.out.println("getIdPrestito");
        Prestito instance = null;
        int expResult = 0;
        int result = instance.getIdPrestito();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLibro method, of class Prestito.
     */
    @Test
    public void testGetLibro() {
        System.out.println("getLibro");
        Prestito instance = null;
        Libro expResult = null;
        Libro result = instance.getLibro();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUtente method, of class Prestito.
     */
    @Test
    public void testGetUtente() {
        System.out.println("getUtente");
        Prestito instance = null;
        Utente expResult = null;
        Utente result = instance.getUtente();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of chiudiPrestito method, of class Prestito.
     */
    @Test
    public void testChiudiPrestito() {
        System.out.println("chiudiPrestito");
        LocalDate dataEffettiva = null;
        Prestito instance = null;
        instance.chiudiPrestito(dataEffettiva);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of verificaRitardo method, of class Prestito.
     */
    @Test
    public void testVerificaRitardo() {
        System.out.println("verificaRitardo");
        Prestito instance = null;
        boolean expResult = false;
        boolean result = instance.verificaRitardo();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of segnaRitardo method, of class Prestito.
     */
    @Test
    public void testSegnaRitardo() {
        System.out.println("segnaRitardo");
        Prestito instance = null;
        instance.segnaRitardo();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
