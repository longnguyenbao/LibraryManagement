/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.nbl.librarymanagementapp;

import com.nbl.pojo.User;
import com.nbl.pojo.Role;
import com.nbl.pojo.Department;
import com.nbl.services.RoleServices;
import com.nbl.services.DepartmentServices;
import com.nbl.services.UserServices;
import com.nbl.utils.Utils;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class ManageUsersController implements Initializable {
    @FXML
    private ComboBox<Role> cbxRole;
    @FXML
    private ComboBox<Department> cbxDepartment;
    @FXML
    private DatePicker dpRegistrationDate;
    @FXML
    private DatePicker dpExpirationDate;
    @FXML
    private DatePicker dpDateOfBirth;
    @FXML
    private RadioButton rdoMale;
    @FXML
    private RadioButton rdoFemale;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtKeyword;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private TableView<User> tbUser;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rdoMale.setSelected(true);
        
        this.txtId.setEditable(false);
        
        this.loadComboBoxRole();
        this.loadComboBoxDepartment();
 
        this.txtPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtPhone.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        
        this.initMouseClick();
        
        try {
            this.loadColumnsUser();
            this.loadTableUser(null);
        }
        catch (SQLException ex) {
            Logger.getLogger(ManageUsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void refreshHandler() throws SQLException {
        this.loadTableUser(null);
        this.txtId.clear();
        this.txtUsername.clear();
        this.txtPassword.clear();
        this.txtName.clear();
        this.rdoMale.setSelected(true);
        this.cbxRole.getSelectionModel().clearSelection();
        this.cbxRole.setValue(null);
        this.cbxDepartment.getSelectionModel().clearSelection();
        this.cbxDepartment.setValue(null);
        this.dpDateOfBirth.getEditor().clear();
        this.dpRegistrationDate.getEditor().clear();
        this.dpExpirationDate.getEditor().clear();
        this.txtEmail.clear();
        this.txtAddress.clear();
        this.txtPhone.clear();
    }
    
    public void addHandler(ActionEvent evt) throws SQLException {
        if(this.txtUsername.getText().isBlank()) {
            Utils.getBox("Username is null!", Alert.AlertType.WARNING).show();
            return;
        }
        
        if(this.txtPassword.getText().isEmpty()) {
            Utils.getBox("Password is null!", Alert.AlertType.WARNING).show();
            return;
        }
        
        if (this.cbxRole.getValue() == null) {
            Utils.getBox("Role is null!", Alert.AlertType.WARNING).show();
            return;
        }
        
        if (this.cbxDepartment.getValue() == null) {
            Utils.getBox("Department is null!", Alert.AlertType.WARNING).show();
            return;
        }
        
        User user = new User();
        user.setId(Utils.parseIntOrNull(txtId.getText()));
        user.setUsername(txtUsername.getText());
        user.setName(txtName.getText());
        user.setEmail(txtEmail.getText());
        user.setDateOfBirth(Utils.parseDate(dpDateOfBirth.valueProperty().get()));
        user.setRoleId(cbxRole.getSelectionModel().getSelectedItem().getId());
        user.setDepartmentId(cbxDepartment.getSelectionModel().getSelectedItem().getId());
        user.setGender(pickGender());
        user.setRegistrationDate(Utils.parseDate(dpRegistrationDate.valueProperty().get()));
        user.setExpirationDate(Utils.parseDate(dpExpirationDate.valueProperty().get()));
        user.setAddress(txtAddress.getText());
        user.setPhone(txtPhone.getText());
        user.setPassword(txtPassword.getText());

        UserServices s = new UserServices();
        Integer check = s.addUser(user);
        
        switch(check) {
            case 1:
                refreshHandler();
                Utils.getBox("Add user successful!", Alert.AlertType.INFORMATION).show();
                break;
            case 10:
                Utils.getBox("Username is null!", Alert.AlertType.WARNING).show();
                break;
            case 11:
                Utils.getBox("Password is null!", Alert.AlertType.WARNING).show();
                break;
            case 12:
                Utils.getBox("Role is null!", Alert.AlertType.WARNING).show();
                break;    
            case 13:
                Utils.getBox("Department is null!", Alert.AlertType.WARNING).show();
                break; 
            case 14:
                Utils.getBox("Please clear user id to add new user!", Alert.AlertType.WARNING).show();
                break; 
            case 15:
                Utils.getBox("Username contains special character!", Alert.AlertType.WARNING).show();
                break;
            case 16:
                Utils.getBox("Name contains special character!", Alert.AlertType.WARNING).show();
                break;
            case 17:
                Utils.getBox("This username was taken by another user!", Alert.AlertType.WARNING).show();
                break;
            default:
                Utils.getBox("Something went wrong!", Alert.AlertType.WARNING).show();
        }
    }
    
    public void editHandler(ActionEvent evt) throws SQLException {
        if(this.txtUsername.getText().isBlank()) {
            Utils.getBox("Username is null!", Alert.AlertType.WARNING).show();
            return;
        }
        
        if(this.txtPassword.getText().isEmpty()) {
            Utils.getBox("Password is null!", Alert.AlertType.WARNING).show();
            return;
        }
        
        if (this.cbxRole.getValue() == null) {
            Utils.getBox("Role is null!", Alert.AlertType.WARNING).show();
            return;
        }
        
        if (this.cbxDepartment.getValue() == null) {
            Utils.getBox("Department is null!", Alert.AlertType.WARNING).show();
            return;
        }
        
        User user = new User();
        user.setId(Utils.parseIntOrNull(txtId.getText()));
        user.setUsername(txtUsername.getText());
        user.setName(txtName.getText());
        user.setEmail(txtEmail.getText());
        user.setDateOfBirth(Utils.parseDate(dpDateOfBirth.valueProperty().get()));
        user.setRoleId(cbxRole.getSelectionModel().getSelectedItem().getId());
        user.setDepartmentId(cbxDepartment.getSelectionModel().getSelectedItem().getId());
        user.setGender(pickGender());
        user.setRegistrationDate(Utils.parseDate(dpRegistrationDate.valueProperty().get()));
        user.setExpirationDate(Utils.parseDate(dpExpirationDate.valueProperty().get()));
        user.setAddress(txtAddress.getText());
        user.setPhone(txtPhone.getText());
        user.setPassword(txtPassword.getText());

        UserServices s = new UserServices();
        Integer check = s.editUser(user);
        
        switch (check) {
            case 1:
                refreshHandler();
                Utils.getBox("Edit user successful!", Alert.AlertType.INFORMATION).show();
                break;
            case 10:
                Utils.getBox("Username is null!", Alert.AlertType.WARNING).show();
                break;
            case 11:
                Utils.getBox("Password is null!", Alert.AlertType.WARNING).show();
                break;
            case 12:
                Utils.getBox("Role is null!", Alert.AlertType.WARNING).show();
                break;    
            case 13:
                Utils.getBox("Department is null!", Alert.AlertType.WARNING).show();
                break; 
            case 14:
                Utils.getBox("User id is null!", Alert.AlertType.WARNING).show();
                break; 
            case 15:
                Utils.getBox("Username contains special character!", Alert.AlertType.WARNING).show();
                break;
            case 16:
                Utils.getBox("Name contains special character!", Alert.AlertType.WARNING).show();
                break;
            case 17:
                Utils.getBox("User id does not exist!", Alert.AlertType.WARNING).show();
                break;
            case 18:
                Utils.getBox("This username was taken by another user!", Alert.AlertType.WARNING).show();
                break;
            default:
                Utils.getBox("Something went wrong!", Alert.AlertType.WARNING).show();
        }
    }
    
    private void loadColumnsUser() {
        TableColumn col1 = new TableColumn("id");
        col1.setCellValueFactory(new PropertyValueFactory("id"));
        
        TableColumn col2 = new TableColumn("Username");
        col2.setCellValueFactory(new PropertyValueFactory("username"));

        TableColumn col3 = new TableColumn("Password");
        col3.setCellValueFactory(new PropertyValueFactory("password"));

        TableColumn col4 = new TableColumn("Name");
        col4.setCellValueFactory(new PropertyValueFactory("name"));
        
        TableColumn col5 = new TableColumn("Gender");
        col5.setCellValueFactory(new PropertyValueFactory("gender"));
        
        TableColumn col6 = new TableColumn("Date of Birth");
        col6.setCellValueFactory(new PropertyValueFactory("dateOfBirth"));
        
        TableColumn col7 = new TableColumn("Role id");
        col7.setCellValueFactory(new PropertyValueFactory("roleId"));
        
        TableColumn col8 = new TableColumn("Department id");
        col8.setCellValueFactory(new PropertyValueFactory("departmentId"));
        
        TableColumn col9 = new TableColumn("Registration date");
        col9.setCellValueFactory(new PropertyValueFactory("registrationDate"));
        
        TableColumn col10 = new TableColumn("Expiration date");
        col10.setCellValueFactory(new PropertyValueFactory("expirationDate"));
        
        TableColumn col11 = new TableColumn("Email");
        col11.setCellValueFactory(new PropertyValueFactory("email"));
        
        TableColumn col12 = new TableColumn("Address");
        col12.setCellValueFactory(new PropertyValueFactory("address"));
        
        TableColumn col13 = new TableColumn("Phone");
        col13.setCellValueFactory(new PropertyValueFactory("phone"));
        
        TableColumn col14 = new TableColumn("");
        col14.setCellFactory(param -> new TableCell<User, String>() {
            final Button btnDelete = new Button("Delete");

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    btnDelete.setOnAction(event -> {
                        Alert a = Utils.getBox("Are you sure to delete?", Alert.AlertType.CONFIRMATION);
                        Optional<ButtonType> result = a.showAndWait();
                        if(result.get() == ButtonType.OK) {
                            User user = getTableView().getItems().get(getIndex());
                        
                        try {
                            UserServices s = new UserServices();
        
                            if(s.deleteUser(user.getId()) == true) {
                                Utils.getBox("Delete successful!", Alert.AlertType.INFORMATION).show();
                            }
                            else {
                                Utils.getBox("Something went wrong!", Alert.AlertType.WARNING).show();
                            }
                        } 
                        catch (Exception e) {
                            Utils.getBox("Something went wrong!", Alert.AlertType.WARNING).show();
                        }
                        
                        getTableView().getItems().remove(user);
                        }
                    });
                    setGraphic(btnDelete);
                    setText(null);
                }
            }
        });
        
        this.tbUser.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14);
    }
    
    private void loadTableUser(String kw) throws SQLException {
        UserServices s = new UserServices();
        try {
            this.tbUser.setItems(FXCollections.observableList(s.getUserList(kw)));
        } catch (SQLException ex) {
            Logger.getLogger(ManageUsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadComboBoxRole() {
        RoleServices s = new RoleServices();
        try {
            this.cbxRole.setItems(FXCollections.observableArrayList(s.getRoleList()));
        } catch (SQLException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadComboBoxDepartment() {
        DepartmentServices s = new DepartmentServices();
        try {
            this.cbxDepartment.setItems(FXCollections.observableArrayList(s.getDepartmentList()));
        } catch (SQLException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initMouseClick() {
        this.tbUser.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 0) {
                try {
                    this.txtId.setText(tbUser.getSelectionModel().getSelectedItem().getId().toString());
                    this.txtUsername.setText(tbUser.getSelectionModel().getSelectedItem().getUsername());
                    this.txtName.setText(tbUser.getSelectionModel().getSelectedItem().getName());
                    this.txtEmail.setText(tbUser.getSelectionModel().getSelectedItem().getEmail());
                    
                    this.dpDateOfBirth.setValue(null);
                    if(tbUser.getSelectionModel().getSelectedItem().getDateOfBirth() != null) {
                        this.dpDateOfBirth.setValue(tbUser.getSelectionModel().getSelectedItem().getDateOfBirth().toLocalDate());
                    }
                        
                    this.cbxRole.getSelectionModel().select(RoleServices.getRoleById(tbUser.getSelectionModel().getSelectedItem().getRoleId()));
                    this.cbxDepartment.getSelectionModel().select(DepartmentServices.getDepartmentById(tbUser.getSelectionModel().getSelectedItem().getDepartmentId()));
                    this.txtPhone.setText("");
                    if(tbUser.getSelectionModel().getSelectedItem().getPhone() != null) {
                        this.txtPhone.setText(tbUser.getSelectionModel().getSelectedItem().getPhone());
                    }
                    
                    this.rdoMale.setSelected(true);
                    if(!tbUser.getSelectionModel().getSelectedItem().getGender().contains("Male")) {
                        this.rdoFemale.setSelected(true);
                    }
                    
                    this.dpRegistrationDate.setValue(tbUser.getSelectionModel().getSelectedItem().getRegistrationDate().toLocalDate());
                    this.dpExpirationDate.setValue(tbUser.getSelectionModel().getSelectedItem().getExpirationDate().toLocalDate());
                    this.txtAddress.setText(tbUser.getSelectionModel().getSelectedItem().getAddress());
                    this.txtPassword.setText(tbUser.getSelectionModel().getSelectedItem().getPassword());   
                } 
                catch (Exception ex) {
                    Logger.getLogger(ManageUsersController.class.getName()).log(Level.SEVERE, null, ex);
                }        
            }
        });
    }
    
    public void clearIdHandler(){
        this.txtId.clear();
    }
    
    private String pickGender(){
        if(rdoMale.isSelected()){
            return rdoMale.getText();
        }
        else {
            return rdoFemale.getText();
        }       
    }
}
