/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.nbl.librarymanagementapp;

import com.nbl.utils.Utils;
import com.nbl.pojo.User;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class MenuController implements Initializable {
    private User user;
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void manageAccountsHandler(ActionEvent evt) throws IOException, SQLException {
        if(this.user.getRoleId() == 2) {
            Utils.loadFXML("FXMLManageUsers.fxml", "Manage users");
        }
        else {
            Utils.getBox("You don't have permission for this!", Alert.AlertType.WARNING).show();
        }
    }
}
