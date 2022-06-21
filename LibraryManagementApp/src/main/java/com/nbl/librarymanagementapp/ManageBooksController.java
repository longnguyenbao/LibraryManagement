/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.nbl.librarymanagementapp;

import com.nbl.pojo.Book;
import com.nbl.pojo.BookDetail;
import com.nbl.pojo.Author;
import com.nbl.pojo.Category;
import com.nbl.services.BookServices;
import com.nbl.services.BookDetailServices;
import com.nbl.services.BookAuthorServices;
import com.nbl.services.CategoryServices;
import com.nbl.services.AuthorServices;
import com.nbl.utils.Utils;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import javafx.scene.control.ButtonType;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class ManageBooksController implements Initializable {
    @FXML
    private TableView<BookDetail> tbBook;
    @FXML
    private TextField txtKeywords;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextArea areaDescription;
    @FXML
    private TextField txtPublicationYear;
    @FXML
    private TextField txtPublicationPlace;
    @FXML
    private DatePicker dpEntryDate;
    @FXML
    private TextField txtLocation;
    @FXML
    private TableView<Author> tbAuthor;
    @FXML
    private ComboBox<Author> cbxAuthor;
    @FXML
    private ComboBox<Category> cbxCategory;
    @FXML
    private ComboBox cbxSearchCondition;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadColumnsBook();
        loadTableBook(null, null);
        
        loadComboBoxAuthor();
        loadComboBoxCategory();
        
        initMouseClick();
        
        this.txtId.setEditable(false);
        
        loadColumnsAuthor();
        loadTableAuthor(null);
        
        this.txtPublicationYear.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtPublicationYear.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }   
    
    public void refreshHandler() {
        this.loadTableBook(null, null);
        this.txtId.clear();
        this.txtName.clear();
        this.areaDescription.clear();
        this.txtPublicationYear.clear();
        this.txtPublicationPlace.clear();
        this.dpEntryDate.setValue(null);
        this.txtLocation.clear();
        this.cbxCategory.getSelectionModel().clearSelection();
        this.cbxCategory.setValue(null);
        
        this.cbxAuthor.getSelectionModel().clearSelection();
        this.cbxAuthor.setValue(null);
        
        this.cbxSearchCondition.getSelectionModel().select(0);
        
        this.tbAuthor.getItems().clear();
    }
    
    public void createIdHandler() {
        this.txtId.setText(UUID.randomUUID().toString());
    }
    
    public void addAuthorHandler() {
        Author author = new Author();
        author.setId(cbxAuthor.getSelectionModel().getSelectedItem().getId());
        author.setName(cbxAuthor.getSelectionModel().getSelectedItem().getName());
        tbAuthor.getItems().add(author);
    }
    
    private void loadTableAuthor(String kw) {
        AuthorServices s = new AuthorServices();
        try {
            this.tbAuthor.setItems(FXCollections.observableList(s.getAuthorListByBookId(kw)));
        } catch (SQLException ex) {
            Logger.getLogger(ManageBooksController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadColumnsAuthor() {
        TableColumn col1 = new TableColumn("Id");
        col1.setCellValueFactory(new PropertyValueFactory("id"));

        TableColumn col2 = new TableColumn("Name");
        col2.setCellValueFactory(new PropertyValueFactory("name"));
        
        TableColumn col3 = new TableColumn("");
        col3.setCellFactory(param -> new TableCell<Author, String>() {
            final Button btnDelete = new Button("Delete");

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    btnDelete.setOnAction(event -> {
                        Author author = getTableView().getItems().get(getIndex());
                        getTableView().getItems().remove(author);
                    });
                    setGraphic(btnDelete);
                    setText(null);
                }
            }
        });
        
        this.tbAuthor.getColumns().addAll(col1, col2, col3);
    }
    
    private void loadComboBoxAuthor() {
        AuthorServices s = new AuthorServices();
        try {
            this.cbxAuthor.setItems(FXCollections.observableArrayList(s.getAuthorList()));
        } catch (SQLException ex) {
            Logger.getLogger(ManageBooksController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadComboBoxCategory() {
        CategoryServices s = new CategoryServices();
        try {
            this.cbxCategory.setItems(FXCollections.observableArrayList(s.getCategoryList()));
        } catch (SQLException ex) {
            Logger.getLogger(ManageBooksController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadTableBook(String kw, Integer  searchCondition) {
        BookDetailServices s = new BookDetailServices();
        try {
            this.tbBook.setItems(FXCollections.observableList(s.getBookDetailList(kw, searchCondition)));
        } catch (SQLException ex) {
            Logger.getLogger(ManageBooksController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addBookHandler(ActionEvent evt) throws SQLException {
        Alert a = Utils.getBox("Are you sure to add?", Alert.AlertType.CONFIRMATION);
        Optional<ButtonType> result = a.showAndWait();
        if(result.get() == ButtonType.OK) {
            if(txtId.getText().isBlank()) {
                Utils.getBox("Id is null!", Alert.AlertType.WARNING).show();
                return;
            }
            
            if(txtName.getText().isBlank()) {
                Utils.getBox("Name is null!", Alert.AlertType.WARNING).show();
                return;
            }

            ArrayList<Integer> authorIdList = new ArrayList();
            for (Author author : tbAuthor.getItems()) {
                authorIdList.add(author.getId());
            }

            Book book = new Book();
            book.setId(txtId.getText());
            book.setName(txtName.getText());
            book.setDescription(areaDescription.getText());
            book.setPublicationYear(0);
            if(!txtPublicationYear.getText().isEmpty())
                book.setPublicationYear(Utils.parseIntOrNull(txtPublicationYear.getText()));
            book.setPublicationPlace(txtPublicationPlace.getText());
            book.setEntryDate(Utils.parseDateNow());
            if(dpEntryDate.getValue() != null)
                book.setEntryDate(Utils.parseDate(dpEntryDate.valueProperty().get()));
            book.setLocation(txtLocation.getText());
            book.setCategoryId(1);
            if(!cbxCategory.getSelectionModel().isEmpty())
                book.setCategoryId(Utils.parseIntOrNull(cbxCategory.getSelectionModel().getSelectedItem().getId().toString()));
            BookServices s = new BookServices();
            Integer check1 = s.addBook(book);

            switch (check1) {
                case 1:
                    BookAuthorServices s1 = new BookAuthorServices();
                    if(s1.addBookAuthor(book.getId(), authorIdList)) {
                        Utils.getBox("Add sucessful!", Alert.AlertType.INFORMATION).show();
                        refreshHandler();
                        break;
                    }
                    else {
                        Utils.getBox("Something wnet wrong!", Alert.AlertType.WARNING).show();
                        break;
                    }
                case 10:
                    Utils.getBox("Id is null!", Alert.AlertType.WARNING).show();
                    break;
                case 11:
                    Utils.getBox("Name is null!", Alert.AlertType.WARNING).show();
                    break;
                case 12:
                    Utils.getBox("Book id is already taken!", Alert.AlertType.WARNING).show();
                    break;
                default:
                    Utils.getBox("Something went wrong!", Alert.AlertType.WARNING).show();
            }
        }
    }
    
    private void loadColumnsBook() {
        TableColumn col1 = new TableColumn("Id");
        col1.setCellValueFactory(new PropertyValueFactory("bookId"));

        TableColumn col2 = new TableColumn("Name");
        col2.setCellValueFactory(new PropertyValueFactory("bookName"));

        TableColumn col3 = new TableColumn("Description");
        col3.setCellValueFactory(new PropertyValueFactory("description"));
        
        TableColumn col4 = new TableColumn("Publication year");
        col4.setCellValueFactory(new PropertyValueFactory("publicationYear"));
        
        TableColumn col5 = new TableColumn("Publication place");
        col5.setCellValueFactory(new PropertyValueFactory("publicationPlace"));
        
        TableColumn col6 = new TableColumn("Entry date");
        col6.setCellValueFactory(new PropertyValueFactory("entryDate"));
        
        TableColumn col7 = new TableColumn("Location");
        col7.setCellValueFactory(new PropertyValueFactory("location"));
        
        TableColumn col8 = new TableColumn("Category name");
        col8.setCellValueFactory(new PropertyValueFactory("categoryName"));
        
        TableColumn col9 = new TableColumn("");
        col9.setCellFactory(param -> new TableCell<BookDetail, String>() {
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
                            BookDetail bookDetail = getTableView().getItems().get(getIndex());
                        
                            BookServices s = new BookServices();
                            try {
                                Integer check = s.deleteBook(bookDetail.getBookId());
                                switch (check) {
                                    case 1:
                                        Utils.getBox("Delete successful!", Alert.AlertType.INFORMATION).show();
                                        break;
                                    case 10:
                                        Utils.getBox("Id is null!", Alert.AlertType.WARNING).show();
                                        break;
                                    case 11:
                                        Utils.getBox("Id does not exist!", Alert.AlertType.WARNING).show();
                                        break;
                                    default:
                                        Utils.getBox("Something went wrong!", Alert.AlertType.WARNING).show();
                                }
                            }
                            catch (SQLException ex) {
                                Utils.getBox("Something went wrong!", Alert.AlertType.WARNING).show();
                            }

                            getTableView().getItems().remove(bookDetail);
                        }
                    });
                    setGraphic(btnDelete);
                    setText(null);
                }
            }
        });
        
        this.tbBook.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7, col8, col9);
    }
    
    private void initMouseClick() {
        this.tbBook.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 0) {
                String id = tbBook.getSelectionModel().getSelectedItem().getBookId();
                this.txtId.setText(id);
                this.txtName.setText(tbBook.getSelectionModel().getSelectedItem().getBookName());
                this.areaDescription.setText(tbBook.getSelectionModel().getSelectedItem().getDescription());
                this.txtPublicationYear.setText(Integer.toString(tbBook.getSelectionModel().getSelectedItem().getPublicationYear()));
                this.txtPublicationPlace.setText(tbBook.getSelectionModel().getSelectedItem().getPublicationPlace());
                this.dpEntryDate.setValue(tbBook.getSelectionModel().getSelectedItem().getEntryDate().toLocalDate());
                this.txtLocation.setText(tbBook.getSelectionModel().getSelectedItem().getLocation());
                
                Integer categoryId = tbBook.getSelectionModel().getSelectedItem().getCategoryId();

                try {
                    Category category = CategoryServices.getCategoryById(categoryId);
                    this.cbxCategory.setValue(category);
                }
                catch (SQLException ex) {
                    Logger.getLogger(ManageBooksController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                this.loadTableAuthor(id);
            }
        });
    }
}
