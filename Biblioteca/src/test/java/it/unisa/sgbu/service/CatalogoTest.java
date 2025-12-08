/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.sgbu.service;

import it.unisa.sgbu.domain.Libro;
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
public class CatalogoTest {
    
    public CatalogoTest() {
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
     * Test of aggiungiLibro method, of class Catalogo.
     */
    @Test
    public void testAggiungiLibro() {
        System.out.println("aggiungiLibro");
        Libro l = null;
        Catalogo instance = new Catalogo();
        boolean expResult = false;
        boolean result = instance.aggiungiLibro(l);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rimuoviLibro method, of class Catalogo.
     */
    @Test
    public void testRimuoviLibro() {
        System.out.println("rimuoviLibro");
        String isbn = "";
        Catalogo instance = new Catalogo();
        boolean expResult = false;
        boolean result = instance.rimuoviLibro(isbn);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of modificaLibro method, of class Catalogo.
     */
    @Test
    public void testModificaLibro() {
        System.out.println("modificaLibro");
        String isbn = "";
        Libro nl = null;
        Catalogo instance = new Catalogo();
        boolean expResult = false;
        boolean result = instance.modificaLibro(isbn, nl);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLibro method, of class Catalogo.
     */
    @Test
    public void testGetLibro() {
        System.out.println("getLibro");
        String isbn = "";
        Catalogo instance = new Catalogo();
        Libro expResult = null;
        Libro result = instance.getLibro(isbn);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ricerca method, of class Catalogo.
     */
    @Test
    public void testRicerca() {
        System.out.println("ricerca");
        String query = "";
        String campo = "";
        Catalogo instance = new Catalogo();
        List<Libro> expResult = null;
        List<Libro> result = instance.ricerca(query, campo);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of visualizzaOrdinata method, of class Catalogo.
     */
    @Test
    public void testVisualizzaOrdinata() {
        System.out.println("visualizzaOrdinata");
        Catalogo instance = new Catalogo();
        List<Libro> expResult = null;
        List<Libro> result = instance.visualizzaOrdinata();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
