package Controllers;
import java.sql.Timestamp;
import DAO.InvoiceDAO;
import Model.BankTransaction;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Model.Invoice;
import Model.InvoiceItem;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/invoices") 
public class InvoiceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private InvoiceDAO invoiceDAO;

    @Override
    public void init() throws ServletException {
        invoiceDAO = new InvoiceDAO();
        System.out.println("InvoiceServlet initialized. InvoiceDAO created.");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String invoiceId = request.getParameter("id");

        System.out.println("Received GET request. Action: " + action + ", Invoice ID: " + invoiceId);

        if (action == null || action.isEmpty() || "list".equals(action)) {
            listInvoices(request, response);
        } else if ("viewDetails".equals(action)) {
            viewInvoiceDetails(request, response, invoiceId);
        } else if ("create".equals(action)) {
            try {
                request.setAttribute("stores", invoiceDAO.getAllStores());
                request.setAttribute("employees", invoiceDAO.getAllEmployees());
                request.setAttribute("customers", invoiceDAO.getAllCustomers());
                request.setAttribute("paymentMethods", invoiceDAO.getAllPaymentMethods());
                request.setAttribute("products", invoiceDAO.getAllProducts());
                request.getRequestDispatcher("/create-invoice.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Error loading form data: " + e.getMessage());
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } else if ("listBankTransactions".equals(action)) {
            listBankTransactions(request, response);
        } else if ("createBankTransaction".equals(action)) {
            try {
                request.setAttribute("invoices", invoiceDAO.getAllInvoices());
                request.setAttribute("paymentMethods", invoiceDAO.getAllPaymentMethods());
                request.getRequestDispatcher("/create-bank-transaction.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Error loading bank transaction form: " + e.getMessage());
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } else {
            System.out.println("Unknown action: " + action + ". Redirecting to list invoices.");
            listInvoices(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        System.out.println("Received POST request. Action: " + action);

        if ("createInvoice".equals(action)) {
            try {
                Invoice invoice = new Invoice();
                invoice.setInvoiceDate(new Timestamp(System.currentTimeMillis()));
                invoice.setNote(request.getParameter("notes"));
                invoice.setStatus("Pending");
                invoice.setStoreID(Integer.parseInt(request.getParameter("storeID")));
                invoice.setEmployeeID(Integer.parseInt(request.getParameter("employeeID")));
                String customerID = request.getParameter("customerID");
                if (customerID != null && !customerID.isEmpty()) {
                    invoice.setCustomerID(Integer.parseInt(customerID));
                }
                String paymentMethodID = request.getParameter("paymentMethodID");
                if (paymentMethodID != null && !paymentMethodID.isEmpty()) {
                    invoice.setPaymentMethodID(Integer.parseInt(paymentMethodID));
                }

                List<InvoiceItem> items = new ArrayList<>();
                double totalAmount = 0.0;
                int detailCount = 1;
                while (request.getParameter("productID_" + detailCount) != null) {
                    InvoiceItem item = new InvoiceItem();
                    item.setProductID(request.getParameter("productID_" + detailCount));
                    item.setQuantity((int) Double.parseDouble(request.getParameter("quantity_" + detailCount)));
                    item.setUnitPrice(Double.parseDouble(request.getParameter("unitPrice_" + detailCount)));
                    String discountPercent = request.getParameter("discountPercent_" + detailCount);
                    double discount = (discountPercent != null && !discountPercent.isEmpty()) ?
                                     Double.parseDouble(discountPercent) : 0.0;
                    item.setDiscountPercent(discount);
                    double subtotal = item.getQuantity() * item.getUnitPrice() * (1 - item.getDiscountPercent() / 100);
                    item.setSubtotal(subtotal);
                    items.add(item);
                    totalAmount += subtotal;
                    detailCount++;
                }
                invoice.setTotalAmount(totalAmount);

                String newInvoiceID = invoiceDAO.createInvoice(invoice, items);
                response.sendRedirect(request.getContextPath() + "/invoices?action=list&success=Invoice+created+successfully+with+ID+" + newInvoiceID);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Error creating invoice: " + e.getMessage());
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } else if ("createBankTransaction".equals(action)) {
            try {
                BankTransaction transaction = new BankTransaction();
                String invoiceID = request.getParameter("invoiceID");
                if (invoiceID != null && !invoiceID.isEmpty()) {
                    transaction.setInvoiceID(Integer.parseInt(invoiceID));
                }
                transaction.setPaymentMethodID(Integer.parseInt(request.getParameter("paymentMethodID")));
                transaction.setBankName(request.getParameter("bankName"));
                transaction.setAccountNumber(request.getParameter("accountNumber"));
                transaction.setTransactionCode(request.getParameter("transactionCode"));
                transaction.setAmount(Double.parseDouble(request.getParameter("amount")));
                transaction.setStatus(request.getParameter("status"));

                int transactionID = invoiceDAO.createBankTransaction(transaction);
                response.sendRedirect(request.getContextPath() + "/invoices?action=listBankTransactions&success=Bank+transaction+created+successfully+with+ID+" + transactionID);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Error creating bank transaction: " + e.getMessage());
                try {
                    request.setAttribute("invoices", invoiceDAO.getAllInvoices());
                } catch (SQLException ex) {
                    Logger.getLogger(InvoiceServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    request.setAttribute("paymentMethods", invoiceDAO.getAllPaymentMethods());
                } catch (SQLException ex) {
                    Logger.getLogger(InvoiceServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                request.getRequestDispatcher("/create-bank-transaction.jsp").forward(request, response);
            }
        } else {
            doGet(request, response);
        }
    }

    private void listInvoices(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Invoice> allInvoices = invoiceDAO.getAllInvoices();
            request.setAttribute("invoices", allInvoices);
            System.out.println("Forwarding to /invoices.jsp with " + (allInvoices != null ? allInvoices.size() : 0) + " invoices.");
            request.getRequestDispatcher("/invoices.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading invoices: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void viewInvoiceDetails(HttpServletRequest request, HttpServletResponse response, String invoiceId)
            throws ServletException, IOException {
        if (invoiceId == null || invoiceId.isEmpty()) {
            request.setAttribute("errorMessage", "Invoice ID is missing for details view.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        try {
            Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);
            if (invoice == null) {
                request.setAttribute("errorMessage", "Invoice with ID " + invoiceId + " not found.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            } else {
                request.setAttribute("invoiceDetails", invoice);
                request.getRequestDispatcher("/invoice-details.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading invoice details: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void listBankTransactions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<BankTransaction> bankTransactions = invoiceDAO.getAllBankTransactions();
            request.setAttribute("bankTransactions", bankTransactions);
            System.out.println("Forwarding to /bank-transactions.jsp with " + (bankTransactions != null ? bankTransactions.size() : 0) + " transactions.");
            request.getRequestDispatcher("/bank-transactions.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading bank transactions: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}