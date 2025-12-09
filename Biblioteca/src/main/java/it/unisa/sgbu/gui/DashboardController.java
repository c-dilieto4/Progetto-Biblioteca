/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import it.unisa.sgbu.domain.*;
import it.unisa.sgbu.gui.GUIController;
import it.unisa.sgbu.gui.GUIView;

public class DashboardController {

    @FXML private TableView<Libro> tableLibri;
    @FXML private TableView<Utente> tableUtenti;
    @FXML private TableView<Prestito> tablePrestiti;

    private GUIController sistema;
    private GUIView mainView;

    public void setSistema(GUIController sistema, GUIView view) {
        this.sistema = sistema;
        this.mainView = view;
    }

    @FXML
    private void onAggiungiLibro() {
        mainView.gestisciAggiuntaLibro(); 
    }

    @FXML
    private void onAggiungiUtente() {
        mainView.gestisciAggiuntaUtente();
    }
    
    @FXML
    private void onNuovoPrestito() {
        mainView.gestisciRegistrazionePrestito();
    }
}
