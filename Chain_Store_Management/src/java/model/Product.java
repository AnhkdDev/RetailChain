package model;

import java.math.BigDecimal;
import java.sql.Date;

public class Product {

    private int productID;
    private String productName;
    private int categoryID;
    private String categoryName;
    private Integer sizeID; // Thay size bằng SizeID, nullable
    private Integer colorID; // Thay color bằng ColorID, nullable
    private BigDecimal sellingPrice;
    private BigDecimal costPrice;
    private String unit;
    private String description;
    private String images;
    private boolean isActive;
    private Date releaseDate;
    private String barcode;
    private String productCode;
    private int stockQuantity;

    // Default constructor
    public Product() {
    }

    // Constructor matching the parameters used in UpdateProductServlet
   public Product(String productName, int categoryID, Integer sizeID, Integer colorID, BigDecimal sellingPrice,
               String description, String images, boolean isActive, String barcode, String productCode,
               int stockQuantity, String unit) {
    this.productName = productName;
    this.categoryID = categoryID;
    this.sizeID = sizeID;
    this.colorID = colorID;
    this.sellingPrice = sellingPrice;
    this.description = description;
    this.images = images;
    this.isActive = isActive;
    this.barcode = barcode;
    this.productCode = productCode;
    this.stockQuantity = stockQuantity;
    this.unit = unit;
}

    // Getters and setters
    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getSizeID() {
        return sizeID;
    }

    public void setSizeID(Integer sizeID) {
        this.sizeID = sizeID;
    }

    public Integer getColorID() {
        return colorID;
    }

    public void setColorID(Integer colorID) {
        this.colorID = colorID;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}