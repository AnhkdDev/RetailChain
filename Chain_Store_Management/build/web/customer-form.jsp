<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${action == 'add' ? 'Add Customer' : 'Edit Customer'}</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <style>
            .modal-content { border-radius: 0.3rem; }
            .modal-header { background-color: #007bff; color: #fff; }
            .modal-title { font-weight: 500; }
            .modal-footer { justify-content: flex-end; }
            .preview-image { max-width: 100px; max-height: 100px; object-fit: cover; border: 1px solid #ddd; border-radius: 4px; padding: 2px; }
            .preview-container { position: relative; margin-top: 10px; display: block !important; visibility: visible !important; }
            .remove-image { position: absolute; top: 0; right: 0; background: red; color: white; border: none; border-radius: 50%; width: 20px; height: 20px; line-height: 20px; text-align: center; cursor: pointer; }
            .detail-label { font-weight: bold; margin-bottom: 5px; }
            .detail-value { margin-bottom: 15px; word-wrap: break-word; }
            .card { margin-bottom: 15px; border: 1px solid #ddd; border-radius: 4px; }
            .card-block { padding: 15px; }
            .form-group { margin-bottom: 15px; }
            .form-control { width: 100%; padding: 6px 12px; border: 1px solid #ccc; border-radius: 4px; }
            .text-muted { color: #6c757d; font-size: 0.875rem; }
            .alert { margin-bottom: 15px; }
            .is-invalid { border-color: #dc3545; }
            .invalid-feedback { color: #dc3545; font-size: 0.875rem; }
        </style>
    </head>
    <body>
        <div class="modal fade" id="customerModal" tabindex="-1" role="dialog" aria-labelledby="customerModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="customerModalLabel">${action == 'add' ? 'Thêm khách hàng mới' : 'Sửa khách hàng'}</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="window.location.href = 'SearchCustomerServlet'"><span aria-hidden="true">×</span></button>
                    </div>
                    <div class="modal-body">
                        <c:if test="${not empty message}">
                            <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                                ${message}
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
                            </div>
                        </c:if>
                        <form action="SearchCustomerServlet" method="POST" id="customerForm" enctype="multipart/form-data">
                            <input type="hidden" name="action" value="save">
                            <input type="hidden" name="customerID" id="customerId" value="${editCustomer.customerID}">
                            <div class="card">
                                <div class="card-block">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="fullName" class="detail-label">Tên đầy đủ:</label>
                                                <input type="text" class="form-control ${errors.fullName != null ? 'is-invalid' : ''}" id="fullName" name="fullName" value="${editCustomer.fullName}" required maxlength="100">
                                                <div class="invalid-feedback">${errors.fullName}</div>
                                            </div>
                                            <div class="form-group">
                                                <label for="phone" class="detail-label">Số điện thoại:</label>
                                                <input type="text" class="form-control ${errors.phone != null ? 'is-invalid' : ''}" id="phone" name="phone" value="${editCustomer.phone}" required pattern="(\+84|0)\d{9}">
                                                <div class="invalid-feedback">${errors.phone}</div>
                                            </div>
                                            <div class="form-group">
                                                <label for="gmail" class="detail-label">Gmail:</label>
                                                <input type="email" class="form-control ${errors.gmail != null ? 'is-invalid' : ''}" id="gmail" name="gmail" value="${editCustomer.gmail}" required>
                                                <div class="invalid-feedback">${errors.gmail}</div>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="gender" class="detail-label">Giới tính:</label>
                                                <select class="form-control ${errors.gender != null ? 'is-invalid' : ''}" id="gender" name="gender" required>
                                                    <option value="Male" ${editCustomer.gender == 'Male' ? 'selected' : ''}>Nam</option>
                                                    <option value="Female" ${editCustomer.gender == 'Female' ? 'selected' : ''}>Nữ</option>
                                                    <option value="Other" ${editCustomer.gender == 'Other' ? 'selected' : ''}>Khác</option>
                                                </select>
                                                <div class="invalid-feedback">${errors.gender}</div>
                                            </div>
                                            <div class="form-group">
                                                <label for="birthDate" class="detail-label">Ngày sinh:</label>
                                                <input type="date" class="form-control ${errors.birthDate != null ? 'is-invalid' : ''}" id="birthDate" name="birthDate" value="${editCustomer.birthDate != null ? editCustomer.birthDate : ''}" required>
                                                <div class="invalid-feedback">${errors.birthDate}</div>
                                            </div>
                                            <div class="form-group">
                                                <label for="address" class="detail-label">Địa chỉ:</label>
                                                <input type="text" class="form-control ${errors.address != null ? 'is-invalid' : ''}" id="address" name="address" value="${editCustomer.address}" required maxlength="255">
                                                <div class="invalid-feedback">${errors.address}</div>
                                            </div>
                                            <c:if test="${action == 'editCustomer' && not empty editCustomer.img}">
                                                <div class="form-group">
                                                    <label for="currentImage" class="detail-label">Hình hiện tại:</label>
                                                    <div class="preview-container" id="currentImageContainer">
                                                        <c:set var="imageUrl" value="${pageContext.request.contextPath}/${editCustomer.img}?t=${applicationScope.now.time}" />
                                                        <img src="${imageUrl}" class="preview-image" id="currentImage" alt="Hình hiện tại" onerror="this.style.display='none'; console.log('Image load failed for: ${imageUrl}')">
                                                        <button type="button" class="remove-image" onclick="removeImage('currentImageContainer', 'imagePath')">X</button>
                                                        <input type="hidden" name="img" id="imagePath" value="${editCustomer.img}">
                                                    </div>
                                                </div>
                                            </c:if>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="imageUpload" class="detail-label">Tải hình lên:</label>
                                        <input type="file" class="form-control ${errors.imageUpload != null ? 'is-invalid' : ''}" id="imageUpload" name="imageUpload" accept=".jpg,.jpeg,.png" onchange="previewImage(this, 'previewContainer', 'previewImage')">
                                        <small class="form-text text-muted">Kích thước tối đa: 2MB. Định dạng: .jpg, .jpeg, .png</small>
                                        <div class="invalid-feedback">${errors.imageUpload}</div>
                                        <div id="previewContainer" class="preview-container" style="display: none;">
                                            <img id="previewImage" class="preview-image" alt="Xem trước hình ảnh">
                                            <button type="button" class="remove-image" onclick="clearPreview('previewContainer', 'previewImage', 'imageUpload')">X</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="window.location.href = 'SearchCustomerServlet'">Đóng</button>
                                <button type="submit" class="btn btn-primary">${action == 'add' ? 'Thêm khách hàng' : 'Lưu thay đổi'}</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <script>
            function previewImage(input, containerId, previewId) {
                console.log('previewImage called at', new Date().toLocaleString());
                var file = input.files[0];
                var previewContainer = document.getElementById(containerId);
                var previewImage = document.getElementById(previewId);

                if (!file) {
                    console.log('No file selected');
                    previewContainer.style.display = 'none';
                    previewImage.src = '';
                    return;
                }

                console.log('Selected file:', file.name, 'Type:', file.type, 'Size:', file.size / 1024, 'KB');

                var reader = new FileReader();
                reader.onload = function(e) {
                    console.log('FileReader loaded:', e.target.result.substring(0, 50));
                    previewContainer.style.display = 'block';
                    previewImage.src = e.target.result;
                };
                reader.onerror = function(e) {
                    console.error('FileReader error:', e);
                    alert('Lỗi khi đọc file ảnh. Kiểm tra console để biết chi tiết.');
                    previewContainer.style.display = 'none';
                    previewImage.src = '';
                };
                reader.readAsDataURL(file);
            }

            function clearPreview(containerId, previewId, inputId) {
                console.log('clearPreview called at', new Date().toLocaleString());
                var previewContainer = document.getElementById(containerId);
                var previewImage = document.getElementById(previewId);
                var input = document.getElementById(inputId);
                previewContainer.style.display = 'none';
                previewImage.src = '';
                input.value = '';
            }

            function removeImage(containerId, inputId) {
                console.log('removeImage called at', new Date().toLocaleString());
                var container = document.getElementById(containerId);
                var input = document.getElementById(inputId);
                if (container) {
                    container.remove();
                }
                if (input) {
                    input.value = '';
                }
            }

            // Auto-open modal if flag is set
            <c:if test="${showCustomerFormModal}">
                $(document).ready(function() {
                    console.log('Modal ready at', new Date().toLocaleString());
                    $('#customerModal').modal({ backdrop: 'static', keyboard: false });
                    $('#customerModal').modal('show');
                });
            </c:if>
        </script>
    </body>
</html>