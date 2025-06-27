<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<style>
    .pagination {
        margin-top: 20px;
    }
    .pagination .page-link {
        color: #007bff;
    }
    .pagination .page-link:hover {
        background-color: #0056b3;
        color: #fff;
    }
    .pagination .page-item.active .page-link {
        background-color: #007bff;
        border-color: #007bff;
        color: #fff;
    }
</style>

<c:if test="${totalPages > 1}">
    <nav aria-label="Page navigation">
        <ul class="pagination justify-content-center">
            <c:if test="${currentPage > 1}">
                <li class="page-item">
                    <c:url var="prevUrl" value="${baseUrl}">
                        <c:param name="page" value="${currentPage - 1}"/>
                        <c:if test="${not empty search}">
                            <c:param name="search" value="${search}"/>
                        </c:if>
                        <c:if test="${not empty statusFilter}">
                            <c:param name="statusFilter" value="${statusFilter}"/>
                        </c:if>
                    </c:url>
                    <a class="page-link" href="${prevUrl}">Trước</a>
                </li>
            </c:if>
            <c:forEach begin="1" end="${totalPages}" var="i">
                <li class="page-item ${i == currentPage ? 'active' : ''}">
                    <c:url var="pageUrl" value="${baseUrl}">
                        <c:param name="page" value="${i}"/>
                        <c:if test="${not empty search}">
                            <c:param name="search" value="${search}"/>
                        </c:if>
                        <c:if test="${not empty statusFilter}">
                            <c:param name="statusFilter" value="${statusFilter}"/>
                        </c:if>
                    </c:url>
                    <a class="page-link" href="${pageUrl}">${i}</a>
                </li>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <li class="page-item">
                    <c:url var="nextUrl" value="${baseUrl}">
                        <c:param name="page" value="${currentPage + 1}"/>
                        <c:if test="${not empty search}">
                            <c:param name="search" value="${search}"/>
                        </c:if>
                        <c:if test="${not empty statusFilter}">
                            <c:param name="statusFilter" value="${statusFilter}"/>
                        </c:if>
                    </c:url>
                    <a class="page-link" href="${nextUrl}">Sau</a>
                </li>
            </c:if>
        </ul>
    </nav> 
</c:if>
