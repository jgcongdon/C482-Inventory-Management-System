package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 * This class holds the inventory for parts and products, as well as methods to add, search (by ID or full/partial name), update, and delete for parts and products
 * */
public class Inventory {

    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * This is the method to add a new part to the inventory. After successfully being added, the part is displayed in the parts table on the main screen.
     * @param newPart the new part to be added
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
    * <p><b>
    * RUNTIME ERROR: While creating the lookup methods, I struggled to have the search results reset when the search field was set to blank. I attempted several different logical arguments to reset the tables when the search field was blank, but no combination of if or try or for loop strategies worked. At best, these failed strategies simply did nothing to refresh the tables or, at worst, simply added even more errors to the problem I was trying to fix. After meeting with course instructors and discussing with classmates, I settled on the currently implemented solution of setting an On Key Typed KeyEvent that takes action to get all parts or products when the search field isEmpty().
    * </b></p>*
    * This is the method to search parts by exact part ID. When this method is called, it parses the current parts in the inventory and returns an exact match if one exists.
    * @param partId the part id to be searched for
    * @return The part matching the search ID criteria (only if a part is found).
    */
    public static Part lookupPart(int partId) {
        Part tempSearchPartID = null;
        for (Part part : getAllParts()) {
            if (part.getId() == partId) {
                tempSearchPartID = part;
            }
        }
        return tempSearchPartID;
    }

    /**
     * This is the method to search parts by exact or partial part name (it is overloaded to do so). When this method is called, it parses the current parts in the inventory and returns an exact or partial match if one or more exist.
     * @param partName the part name to be searched for
     * @return The part or parts matching the search text criteria, either exact matches or if the string is found at any point within part names (only if at least one part name or part of a part name is found).
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> resultParts = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().toLowerCase().contains(partName.toLowerCase())) {
                resultParts.add(part);
            }
        }
        return resultParts;
    }

    /**
     * This is the method to update a part. When this method is called, it takes the existing part being modified and replaces that existing part within the inventory using the part ID.
     * @param id the id of the part to be updated
     * @param selectedPart the selected part to be updated
     */
    public static void updatePart(int id, Part selectedPart) {
        int index = -1;
        for (Part Part : getAllParts()) {
            index++;
            if (Part.getId() == id) {
                getAllParts().set(index, selectedPart);
            }
        }
    }

    /**
     * This is the method to delete a part. When this method is called, the selected part is removed from the inventory.
     * @param selectedPart the selected part to be deleted
     */
    public static void deletePart(Part selectedPart) {
        if (allProducts.size() > 0) {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getAllAssociatedParts().contains(selectedPart)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setContentText("Part is associated with product");
                    alert.showAndWait();
                } else {
                    allParts.remove(selectedPart);
                }
            }
        } else {
            allParts.remove(selectedPart);
        }
    }

    /**
     * This is the method to get all parts. When this method is called, all parts currently in the inventory are returned.
     * @return All parts currently in the inventory.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * This is the method to add a new product to the inventory. After successfully being added, the product is displayed in the products table on the main screen.
     * @param newProduct the new product to be added
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * This is the method to search products by exact product ID. When this method is called, it parses the current products in the inventory and returns an exact match if one exists.
     * @param productId the product id to be searched for
     * @return The product matching the search ID criteria (only if a product is found).
     */
    public static Product lookupProduct(int productId) {
        Product tempSearchProductID = null;
        for (Product product : Inventory.getAllProducts()) {
            if (product.getId() == productId) {
                tempSearchProductID = product;
            }
        }
        return tempSearchProductID;
    }

    /**
     * This is the method to search products by exact or partial product name (it is overloaded to do so). When this method is called, it parses the current products in the inventory and returns an exact or partial match if one or more exist.
     * @param productName the product name to be searched for
     * @return The product or products matching the search text criteria, either exact matches or if the string is found at any point within product names (only if at least one product name or part of a product name is found).
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> resultProducts = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                resultProducts.add(product);
            }
        }
        return resultProducts;
    }

    /**
     * This is the method to update a product. When this method is called, it takes the existing product being modified and replaces that existing product within the inventory using the product ID.
     * @param id the id of the product to be updated
     * @param newProduct the selected product to be updated
     */
    public static void updateProduct(int id, Product newProduct) {
        int index = -1;
        for (Product Product : Inventory.getAllProducts()) {
            index++;
            if (Product.getId() == id) {
                Inventory.getAllProducts().set(index, newProduct);
            }
        }
    }

    /**
     * This is the method to delete a product. When this method is called, the selected product is removed from the inventory.
     * @param selectedProduct the selected product to be deleted
     */
    public static void deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
    }

    /**
     * This is the method to get all product. When this method is called, all products currently in the inventory are returned.
     * @return All products currently in the inventory.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}