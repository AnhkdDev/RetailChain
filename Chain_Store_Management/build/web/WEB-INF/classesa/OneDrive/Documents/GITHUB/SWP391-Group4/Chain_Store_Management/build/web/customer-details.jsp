<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!-- Modal for Customer Details -->
<div class="modal fade" id="customerDetailsModal" tabindex="-1" role="dialog" aria-labelledby="customerDetailsModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="customerDetailsModalLabel">Customer Details</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="window.location.href='SearchCustomerServlet'">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">
                <c:if test="${not empty message}">
                    <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                        <c:out value="${message}"/>
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">×</span>
                        </button>
                    </div>
                </c:if>
                <c:if test="${not empty customer}">
                    <div class="card">
                        <div class="card-block">
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="detail-label">Full Name:</p>
                                    <p class="detail-value"><c:out value="${customer.fullName}"/></p>
                                    <p class="detail-label">Phone:</p>
                                    <p class="detail-value"><c:out value="${customer.phone}"/></p>
                                    <p class="detail-label">Gmail:</p>
                                    <p class="detail-value"><c:out value="${customer.gmail}"/></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="detail-label">Gender:</p>
                                    <p class="detail-value"><c:out value="${customer.gender}"/></p>
                                    <p class="detail-label">Birth Date:</p>
                                    <p class="detail-value"><fmt:formatDate value="${customer.birthDate}" pattern="dd-MM-yyyy"/></p>
                                    <p class="detail-label">Address:</p>
                                    <p class="detail-value"><c:out value="${customer.address}"/></p>
                                    <p class="detail-label">Membership Level:</p>
                                    <p class="detail-value"><c:out value="${customer.membershipLevel != null ? customer.membershipLevel : 'None'}"/></p>
                                    <p class="detail-label">Total Spent:</p>
                                    <p class="detail-value"><fmt:formatNumber value="${customer.totalSpent}" type="currency" currencyCode="VND"/></p>
                                    <c:if test="${not empty customer.img}">
                                        <p class="detail-label">Image:</p>
                                        <p class="detail-value">
                                            <img src="<c:out value="${customer.img}"/>" class="card-img-top" alt="Customer Image">
                                        </p>
                                    </c:if>
                                </div>
                            </div>
                            <a href="SearchCustomerServlet?action=editCustomer&customerID=<c:out value="${customer.customerID}"/>" class="btn btn-primary waves-effect waves-light mt-2" data-toggle="modal" data-target="#customerModal">
                                <i class="fa fa-edit"></i> Edit Customer
                            </a>
                        </div>
                    </div>
                    <div class="card">
                        <div class="card-header">
                            <h5>Invoices</h5>
                        </div>
                        <div class="card-block">
                            <div class="dt-responsive table-responsive">
                                <c:choose>
                                    <c:when test="${empty invoices}">
                                        <p>No invoices found for this customer.</p>
                                    </c:when>
                                    <c:otherwise>
                                        <table class="table table-hover">
                                            <thead>
                                                <tr>
                                                    <th>Invoice ID</th>
                                                    <th>Invoice Date</th>
                                                    <th>Product Name</th>
                                                    <th>Quantity</th>
                                                    <th>Unit Price</th>
                                                    <th>Discount (%)</th>
                                                    <th>Line Total</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="invoice" items="${invoices}">
                                                    <tr>
                                                        <td><c:out value="${invoice.invoiceID}"/></td>
                                                        <td><fmt:formatDate value="${invoice.invoiceDate}" pattern="dd-MM-yyyy HH:mm"/></td>
                                                        <td><c:out value="${invoice.productName}"/></td>
                                                        <td><fmt:formatNumber value="${invoice.quantity}" type="number"/></td>
                                                        <td><fmt:formatNumber value="${invoice.unitPrice}" type="currency" currencyCode="VND"/></td>
                                                        <td><fmt:formatNumber value="${invoice.discountPercent}" type="percent"/></td>
                                                        <td><fmt:formatNumber value="${invoice.lineTotal}" type="currency" currencyCode="VND"/></td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="window.location.href='SearchCustomerServlet'">Close</button>
            </div>
        </div>
    </div>
</div>