package Model;

public class InvoiceItem {
    private String productID;
    private int invoiceItemID;
    private String invoiceID; // Liên kết với InvoiceID
    private String productName;
    private String productSize;
    private String productColor;
    private int quantity;
    private double unitPrice;
    private double discountPercent;
    private double subtotal;

    public InvoiceItem() {}

    public int getInvoiceItemID() {
        return invoiceItemID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setInvoiceItemID(int invoiceItemID) {
        this.invoiceItemID = invoiceItemID;
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public InvoiceItem(int invoiceItemID, String invoiceID, String productName, String productSize, String productColor, int quantity, double unitPrice, double discountPercent, double subtotal) {
        this.invoiceItemID = invoiceItemID;
        this.invoiceID = invoiceID;
        this.productName = productName;
        this.productSize = productSize;
        this.productColor = productColor;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountPercent = discountPercent;
        this.subtotal = subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
