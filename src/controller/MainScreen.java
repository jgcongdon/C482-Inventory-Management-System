package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.lang.*;

/**
 * This class creates the Main Screen controller.
 * @author Jackson Congdon
 */
public class MainScreen implements Initializable {

    Stage stage;
    Parent scene;

    @FXML private TextField mainmenupartsSearchtxt;
    @FXML private TableView<Part> mainmenuPartsTableView;
    @FXML private TableColumn<Part, Integer> mainmenupartsPartIDCol;
    @FXML private TableColumn<Part, String> mainmenupartsPartNameCol;
    @FXML private TableColumn<Part, Integer> mainmenupartsInventoryCol;
    @FXML private TableColumn<Part, Double> mainmenupartsPriceCol;
    @FXML private TextField mainmenuproductsSearchtxt;
    @FXML private TableView<Product> mainmenuproductsTableView;
    @FXML private TableColumn<Product, Integer> mainmenuproductsProductIDCol;
    @FXML private TableColumn<Product, String> mainmenuproductsProductCol;
    @FXML private TableColumn<Product, Integer> mainmenuproductsInventoryCol;
    @FXML private TableColumn<Product, Double> mainmenuproductsPriceCol;

    /**
     * This method loads the Add Part screen when the user clicks the Add button in the Parts section.
     * @param event user clicks the Add button in the Parts section
     * @throws IOException
     */
    @FXML
    void onActionAddParts(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method searches the parts inventory when the user clicks the Search button in the Parts section. It searches part by exact part ID or by exact or partial part name. It parses the current parts in the inventory and returns an exact id match if one exists or it returns an exact or partial name match if one or more exist.
     * @param event user clicks the Search button in the Parts section
     */
    @FXML
    void onActonSearchParts(ActionEvent event) {
        String searchField = mainmenupartsSearchtxt.getText();
        try {
            //First, try searching by name
            ObservableList<Part> tempParts = Inventory.lookupPart(searchField);
            //If no results by name, then search by ID
            if (tempParts.size() == 0) {
                int searchID = Integer.parseInt(searchField);
                Part searchP = Inventory.lookupPart(searchID);
                tempParts.add(searchP);
                //If no results by ID, then alert no parts found by ID
                if (searchP == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setContentText("No parts found by ID");
                    alert.showAndWait();
                }
            }
            mainmenuPartsTableView.setItems(tempParts);
        }
        //Catch NumberFormatException and alert no parts found by name
        catch (NumberFormatException e){
            mainmenuPartsTableView.setItems(null);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("No parts found by name");
            alert.showAndWait();
        }
    }

    /**
     * This is the method to refresh the parts table with all parts in the inventory when the user deletes all text from the parts search field.
     * @param event user deletes all text from the parts search field
     */
    @FXML
    void mainScreenPartSearchOnKeyTyped(KeyEvent event) {
        if (mainmenupartsSearchtxt.getText().isEmpty()) {
            mainmenuPartsTableView.setItems(Inventory.getAllParts());
        }
    }

    /**
     * This method loads the Modify Part screen when the user clicks the Modify button in the Parts section.
     * @param event user clicks the Modify button in the Parts section
     * @throws IOException
     */
    @FXML
    void onActionModifyParts(ActionEvent event) throws IOException {
        //If no parts selected, alert and return to MainScreen
        if (mainmenuPartsTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("ERROR: No part selected");
            alert.showAndWait();
        }
        else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyPart.fxml"));
            loader.load();

            ModifyPart MPController = loader.getController();
            MPController.sendPart(mainmenuPartsTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This is the method to delete a part from the inventory when the user clicks the Delete button in the Parts section.
     * @param event user clicks the Delete button in the Parts section
     * @throws IOException
     */
    @FXML
    void onActionDeleteParts(ActionEvent event) throws IOException {
        Part part = mainmenuPartsTableView.getSelectionModel().getSelectedItem();
        if (mainmenuPartsTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("ERROR: No part selected");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
                Inventory.deletePart(part);
            } else {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
    }

    /**
     * This method loads the Add Product screen when the user clicks the Add button in the Products section.
     * @param event user clicks the Add button in the Products section
     * @throws IOException
     */
    @FXML
    void onActionAddProducts(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method searches the products inventory when the user clicks the Search button in the Products section. It searches products by exact product ID or by exact or partial product name. It parses the current products in the inventory and returns an exact id match if one exists or it returns an exact or partial name match if one or more exist.
     * @param event user clicks the Search button in the Products section
     */
    @FXML
    void onActionSearchProducts(ActionEvent event) {
        String searchProductField = mainmenuproductsSearchtxt.getText();
        try {
            //First, try searching by name
            ObservableList<Product> tempProducts = Inventory.lookupProduct(searchProductField);
            //If no results by name, then search by ID
            if (tempProducts.size() == 0) {
                int searchProductID = Integer.parseInt(searchProductField);
                Product searchProd = Inventory.lookupProduct(searchProductID);
                tempProducts.add(searchProd);
                //If no results by ID, then alert no products found by ID
                if (searchProd == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setContentText("No products found by ID");
                    alert.showAndWait();
                }
            }
            mainmenuproductsTableView.setItems(tempProducts);
        }
        //Catch NumberFormatException and alert no products found by name
        catch (NumberFormatException e) {
            mainmenuproductsTableView.setItems(null);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("No products found by name");
            alert.showAndWait();
        }
    }

    /**
     * This is the method to refresh the products table with all products in the inventory when the user deletes all text from the products search field.
     * @param event user deletes all text from the products search field
     */
    @FXML
    void mainScreenProductSearchOnKeyTyped(KeyEvent event) {
        if (mainmenuproductsSearchtxt.getText().isEmpty()) {
            mainmenuproductsTableView.setItems(Inventory.getAllProducts());
        }
    }

    /**
     * This method loads the Modify Product screen when the user clicks the Modify button in the Product section.
     * @param event user clicks the Modify button in the Product section
     * @throws IOException
     */
    @FXML
    void onActionModifyProducts(ActionEvent event) throws IOException {
        //If no products selected, alert and return to MainScreen
        if (mainmenuproductsTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("ERROR: No product selected");
            alert.showAndWait();
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyProduct.fxml"));
            loader.load();

            ModifyProduct MProdController = loader.getController();
            MProdController.sendProduct(mainmenuproductsTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This is the method to delete a product from the inventory when the user clicks the Delete button in the Product section.
     * @param event user clicks the Delete button in the Product section
     * @throws IOException
     */
    @FXML
    void onActionDeleteProducts(ActionEvent event) throws IOException {
        Product product = mainmenuproductsTableView.getSelectionModel().getSelectedItem();
        if (mainmenuproductsTableView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("ERROR: No product selected");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
                if (!product.getAllAssociatedParts().isEmpty()) {
                    Alert assocAlert = new Alert(Alert.AlertType.WARNING);
                    assocAlert.setTitle("Warning Dialog");
                    assocAlert.setContentText("Product has at least 1 associated part");
                    assocAlert.showAndWait();
                } else {
                    Inventory.deleteProduct(product);
                }
            }
        }
    }

    /**
     * This method exits the program when the user clicks the Exit button
     * @param event user clicks the Exit button
     */
    @FXML void onActionExitMainScreen(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * This is the method to set the all parts table view and the all products table view.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    //Parts table section
    //Loads table with default view of parts
        mainmenuPartsTableView.setItems(Inventory.getAllParts());

    //Loads columns with parts info
        mainmenupartsPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainmenupartsPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainmenupartsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainmenupartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    //Product table section
    //Loads table with default view of products
        mainmenuproductsTableView.setItems(Inventory.getAllProducts());

    //Loads columns with products info
        mainmenuproductsProductIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainmenuproductsProductCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainmenuproductsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainmenuproductsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
