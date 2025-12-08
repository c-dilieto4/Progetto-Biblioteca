/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.service;

import it.unisa.sgbu.domain.Prestito;
import it.unisa.sgbu.domain.Utente;
import java.time.LocalDate;
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
public class RegistroPrestitiTest {
    
    public RegistroPrestitiTest() {
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
     * Test of registraPrestito method, of class RegistroPrestiti.
     */
    @Test
    public void testRegistraPrestito() {
        System.out.println("registraPrestito");
        String isbn = "";
        String matr = "";
        LocalDate dataPrev = null;
        RegistroPrestiti instance = null;
        Prestito expResult = null;
        Prestito result = instance.registraPrestito(isbn, matr, dataPrev);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of registraRestituzione method, of class RegistroPrestiti.
     */
    @Test
    public void testRegistraRestituzione() {
        System.out.println("registraRestituzione");
        int idPrestito = 0;
        LocalDate dataEff = null;
        RegistroPrestiti instance = null;
        boolean expResult = false;
        boolean result = instance.registraRestituzione(idPrestito, dataEff);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPrestitiAttivi method, of class RegistroPrestiti.
     */
    @Test
    public void testGetPrestitiAttivi_0args() {
        System.out.println("getPrestitiAttivi");
        RegistroPrestiti instance = null;
        List<Prestito> expResult = null;
        List<Prestito> result = instance.getPrestitiAttivi();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPrestitiAttivi method, of class RegistroPrestiti.
     */
    @Test
    public void testGetPrestitiAttivi_Utente() {
        System.out.println("getPrestitiAttivi");
        Utente u = null;
        RegistroPrestiti instance = null;
        List<Prestito> expResult = null;
        List<Prestito> result = instance.getPrestitiAttivi(u);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPrestitiInRitardo method, of class RegistroPrestiti.
     */
    @Test
    public void testGetPrestitiInRitardo() {
        System.out.println("getPrestitiInRitardo");
        RegistroPrestiti instance = null;
        List<Prestito> expResult = null;
        List<Prestito> result = instance.getPrestitiInRitardo();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of haPrestitiAttivi method, of class RegistroPrestiti.
     */
    @Test
    public void testHaPrestitiAttivi() {
        System.out.println("haPrestitiAttivi");
        String matricola = "";
        RegistroPrestiti instance = null;
        boolean expResult = false;
        boolean result = instance.haPrestitiAttivi(matricola);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of trovaPrestito method, of class RegistroPrestiti.
     */
    @Test
    public void testTrovaPrestito() {
        System.out.println("trovaPrestito");
        int idPrestito = 0;
        RegistroPrestiti instance = null;
        Prestito expResult = null;
        Prestito result = instance.trovaPrestito(idPrestito);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
