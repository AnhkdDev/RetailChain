package Model;

import java.sql.Timestamp;
import java.util.List;

public class Invoice {
    private String invoiceID; // Dùng String nếu InvoiceID trong CSDL là NVARCHAR/VARCHAR
    private Timestamp invoiceDate;
    private int storeID; // Khóa ngoại
    private String storeName; // Tên cửa hàng (lấy từ JOIN)
    private int employeeID; // Khóa ngoại
    private String employeeName; // Tên nhân viên (lấy từ JOIN)
    private Integer customerID; // Khóa ngoại, có thể null
    private String customerName; // Tên khách hàng (lấy từ JOIN), có thể null
    private double totalAmount;
    private Integer paymentMethodID; // Khóa ngoại, có thể null
    private String status; // Thêm cột Status nếu bạn muốn hiển thị nó
    private String paymentMethod;
    private String note;
    private String paymentMethodName;

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }
    public String getNote() {
        return note;
    }

    public void setNote(String Note) {
        this.note = Note;
    }

   

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    // Danh sách các mặt hàng trong hóa đơn (cho InvoiceDetails)
    private List<InvoiceItem> items;

    public Invoice() {
    }

    // Getters and Setters
    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public Timestamp getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Timestamp invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getPaymentMethodID() {
        return paymentMethodID;
    }

    public void setPaymentMethodID(Integer paymentMethodID) {
        this.paymentMethodID = paymentMethodID;
    }

    public String getStatus() { // Getter cho Status
        return status;
    }

    public void setStatus(String status) { // Setter cho Status
        this.status = status;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    // Ghi đè toString để dễ dàng debug
    @Override
    public String toString() {
        return "Invoice{" +
               "invoiceID='" + invoiceID + '\'' +
               ", invoiceDate=" + invoiceDate +
               ", storeName='" + storeName + '\'' +
               ", employeeName='" + employeeName + '\'' +
               ", customerName='" + customerName + '\'' +
               ", totalAmount=" + totalAmount +
               ", status='" + status + '\'' +
               '}';
    }
}

