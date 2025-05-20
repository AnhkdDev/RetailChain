<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>RetailPro - Retail Management System</title>
  <link rel="stylesheet" href="css/styles.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
 

</head>

<body>
   <script src="script.js"></script>
  <div class="container">
    <nav class="sidebar">
      <div class="logo">
        <h2>RetailPro</h2>
      </div>
      <ul class="nav-links">
        <li class="active" data-page="dashboard"><i class="fas fa-chart-line"></i> Dashboard</li>
        <li data-page="products"><i class="fas fa-box"></i> Products</li>
        <li data-page="customers"><i class="fas fa-users"></i> Customers</li>
        <li data-page="sales"><i class="fas fa-shopping-cart"></i> Sales</li>
        <li data-page="purchases"><i class="fas fa-truck"></i> Purchases</li>
        <li data-page="expenses"><i class="fas fa-file-invoice-dollar"></i> Expenses</li>
        <li data-page="reports"><i class="fas fa-chart-bar"></i> Reports</li>
        <li data-page="settings"><i class="fas fa-cog"></i> Settings</li>
      </ul>
      <div class="user-info">
        <div class="user-avatar">
          <i class="fas fa-user"></i>
        </div>
        <div class="user-details">
          <p class="user-name">Admin User</p>
          <p class="user-role">Administrator</p>
        </div>
      </div>
    </nav>

    <main class="content">
      <header>
        <div class="search-bar">
          <i class="fas fa-search"></i>
          <input type="text" placeholder="Search...">
        </div>
        <div class="header-actions">
          <div class="language-selector">
            <select id="language">
              <option value="en">English</option>
              <option value="vi">Vietnamese</option>
              <option value="fr">French</option>
              <option value="es">Spanish</option>
            </select>
          </div>
          <div class="notification">
            <i class="fas fa-bell"></i>
            <span class="badge">3</span>
          </div>
          <div class="theme-toggle">
            <i class="fas fa-moon"></i>
          </div>
        </div>
      </header>

      <div class="page-content">
        <!-- Dashboard Page -->
        <div class="page" id="dashboard">
          <h1>Dashboard</h1>
          <div class="stats-container">
            <div class="stat-card">
              <div class="stat-icon sales-icon">
                <i class="fas fa-dollar-sign"></i>
              </div>
              <div class="stat-details">
                <h3>Total Sales</h3>
                <p class="stat-value">$24,500</p>
                <p class="stat-change positive"><i class="fas fa-arrow-up"></i> 12.5%</p>
              </div>
            </div>
            <div class="stat-card">
              <div class="stat-icon customers-icon">
                <i class="fas fa-users"></i>
              </div>
              <div class="stat-details">
                <h3>Customers</h3>
                <p class="stat-value">1,250</p>
                <p class="stat-change positive"><i class="fas fa-arrow-up"></i> 5.3%</p>
              </div>
            </div>
            <div class="stat-card">
              <div class="stat-icon products-icon">
                <i class="fas fa-box"></i>
              </div>
              <div class="stat-details">
                <h3>Products</h3>
                <p class="stat-value">324</p>
                <p class="stat-change positive"><i class="fas fa-arrow-up"></i> 2.1%</p>
              </div>
            </div>
            <div class="stat-card">
              <div class="stat-icon expenses-icon">
                <i class="fas fa-file-invoice-dollar"></i>
              </div>
              <div class="stat-details">
                <h3>Expenses</h3>
                <p class="stat-value">$8,320</p>
                <p class="stat-change negative"><i class="fas fa-arrow-down"></i> 3.2%</p>
              </div>
            </div>
          </div>

          <div class="charts-container">
            <div class="chart-card">
              <div class="chart-header">
                <h3>Sales Overview</h3>
                <div class="chart-actions">
                  <select id="sales-period">
                    <option value="weekly">Weekly</option>
                    <option value="monthly" selected>Monthly</option>
                    <option value="yearly">Yearly</option>
                  </select>
                </div>
              </div>
              <div class="chart" id="sales-chart"></div>
            </div>
            <div class="chart-card">
              <div class="chart-header">
                <h3>Top Products</h3>
                <div class="chart-actions">
                  <select id="products-period">
                    <option value="weekly">Weekly</option>
                    <option value="monthly" selected>Monthly</option>
                    <option value="yearly">Yearly</option>
                  </select>
                </div>
              </div>
              <div class="chart" id="products-chart"></div>
            </div>
          </div>

          <div class="recent-activity">
            <h2>Recent Activity</h2>
            <div class="activity-list">
              <div class="activity-item">
                <div class="activity-icon">
                  <i class="fas fa-shopping-cart"></i>
                </div>
                <div class="activity-details">
                  <p class="activity-title">New Sale</p>
                  <p class="activity-description">John Doe purchased 5 items</p>
                  <p class="activity-time">10 minutes ago</p>
                </div>
                <div class="activity-value">$125.00</div>
              </div>
              <div class="activity-item">
                <div class="activity-icon">
                  <i class="fas fa-box"></i>
                </div>
                <div class="activity-details">
                  <p class="activity-title">New Product</p>
                  <p class="activity-description">Wireless Headphones added to inventory</p>
                  <p class="activity-time">1 hour ago</p>
                </div>
                <div class="activity-value">+15 units</div>
              </div>
              <div class="activity-item">
                <div class="activity-icon">
                  <i class="fas fa-user-plus"></i>
                </div>
                <div class="activity-details">
                  <p class="activity-title">New Customer</p>
                  <p class="activity-description">Sarah Johnson registered</p>
                  <p class="activity-time">3 hours ago</p>
                </div>
                <div class="activity-value"><i class="fas fa-check"></i></div>
              </div>
              <div class="activity-item">
                <div class="activity-icon">
                  <i class="fas fa-truck"></i>
                </div>
                <div class="activity-details">
                  <p class="activity-title">New Purchase</p>
                  <p class="activity-description">Order #12345 from Supplier Inc.</p>
                  <p class="activity-time">Yesterday</p>
                </div>
                <div class="activity-value">$1,250.00</div>
              </div>
            </div>
          </div>
        </div>

        <!-- Products Page -->
        <div class="page hidden" id="products">
          <div class="page-header">
            <h1>Products Management</h1>
            <button class="btn-primary" id="add-product-btn"><i class="fas fa-plus"></i> Add Product</button>
          </div>
          
          <div class="filter-bar">
            <div class="search-filter">
              <input type="text" id="product-search" placeholder="Search products...">
            </div>
            <div class="category-filter">
              <select id="category-filter">
                <option value="">All Categories</option>
                <option value="electronics">Electronics</option>
                <option value="clothing">Clothing</option>
                <option value="food">Food & Beverages</option>
                <option value="home">Home & Kitchen</option>
              </select>
            </div>
            <div class="stock-filter">
              <select id="stock-filter">
                <option value="">All Stock Status</option>
                <option value="in-stock">In Stock</option>
                <option value="low-stock">Low Stock</option>
                <option value="out-of-stock">Out of Stock</option>
              </select>
            </div>
          </div>
          
          <div class="table-container">
            <table class="data-table" id="products-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Image</th>
                  <th>Name</th>
                  <th>Category</th>
                  <th>Price</th>
                  <th>Stock</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                <!-- Product rows will be added dynamically -->
              </tbody>
            </table>
          </div>
          
          <div class="pagination">
            <button class="pagination-btn" id="prev-page"><i class="fas fa-chevron-left"></i></button>
            <div class="pagination-numbers">
              <span class="pagination-number active">1</span>
              <span class="pagination-number">2</span>
              <span class="pagination-number">3</span>
              <span class="pagination-ellipsis">...</span>
              <span class="pagination-number">10</span>
            </div>
            <button class="pagination-btn" id="next-page"><i class="fas fa-chevron-right"></i></button>
          </div>
        </div>

        <!-- Customers Page -->
        <div class="page hidden" id="customers">
          <div class="page-header">
            <h1>Customer Management</h1>
            <button class="btn-primary" id="add-customer-btn"><i class="fas fa-plus"></i> Add Customer</button>
          </div>
          
          <div class="filter-bar">
            <div class="search-filter">
              <input type="text" id="customer-search" placeholder="Search customers...">
            </div>
            <div class="status-filter">
              <select id="customer-status-filter">
                <option value="">All Status</option>
                <option value="active">Active</option>
                <option value="inactive">Inactive</option>
              </select>
            </div>
          </div>
          
          <div class="table-container">
            <table class="data-table" id="customers-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Phone</th>
                  <th>Total Orders</th>
                  <th>Total Spent</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                <!-- Customer rows will be added dynamically -->
              </tbody>
            </table>
          </div>
          
          <div class="pagination">
            <button class="pagination-btn" id="customer-prev-page"><i class="fas fa-chevron-left"></i></button>
            <div class="pagination-numbers">
              <span class="pagination-number active">1</span>
              <span class="pagination-number">2</span>
              <span class="pagination-number">3</span>
            </div>
            <button class="pagination-btn" id="customer-next-page"><i class="fas fa-chevron-right"></i></button>
          </div>
        </div>

        <!-- Other pages will be similar in structure -->
        <div class="page hidden" id="sales">
          <h1>Sales Management</h1>
          <p>Sales management content will be displayed here.</p>
        </div>
        
        <div class="page hidden" id="purchases">
          <h1>Purchases Management</h1>
          <p>Purchases management content will be displayed here.</p>
        </div>
        
        <div class="page hidden" id="expenses">
          <h1>Expenses Management</h1>
          <p>Expenses management content will be displayed here.</p>
        </div>
        
        <div class="page hidden" id="reports">
          <h1>Reports</h1>
          <p>Reports content will be displayed here.</p>
        </div>
        
        <div class="page hidden" id="settings">
          <h1>Settings</h1>
          <p>Settings content will be displayed here.</p>
        </div>
      </div>
    </main>
  </div>

  <!-- Modal for adding/editing products -->
  <div class="modal" id="product-modal">
    <div class="modal-content">
      <div class="modal-header">
        <h2 id="product-modal-title">Add New Product</h2>
        <button class="close-modal">&times;</button>
      </div>
      <div class="modal-body">
        <form id="product-form">
          <div class="form-group">
            <label for="product-name">Product Name</label>
            <input type="text" id="product-name" required>
          </div>
          <div class="form-group">
            <label for="product-category">Category</label>
            <select id="product-category" required>
              <option value="">Select Category</option>
              <option value="electronics">Electronics</option>
              <option value="clothing">Clothing</option>
              <option value="food">Food & Beverages</option>
              <option value="home">Home & Kitchen</option>
            </select>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label for="product-price">Price ($)</label>
              <input type="number" id="product-price" min="0" step="0.01" required>
            </div>
            <div class="form-group">
              <label for="product-stock">Stock Quantity</label>
              <input type="number" id="product-stock" min="0" required>
            </div>
          </div>
          <div class="form-group">
            <label for="product-description">Description</label>
            <textarea id="product-description" rows="4"></textarea>
          </div>
          <div class="form-group">
            <label for="product-image">Product Image</label>
            <input type="file" id="product-image" accept="image/*">
            <div class="image-preview" id="image-preview"></div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button class="btn-secondary" id="cancel-product">Cancel</button>
        <button class="btn-primary" id="save-product">Save Product</button>
      </div>
    </div>
  </div>

  <!-- Modal for adding/editing customers -->
  <div class="modal" id="customer-modal">
    <div class="modal-content">
      <div class="modal-header">
        <h2 id="customer-modal-title">Add New Customer</h2>
        <button class="close-modal">&times;</button>
      </div>
      <div class="modal-body">
        <form id="customer-form">
          <div class="form-row">
            <div class="form-group">
              <label for="customer-first-name">First Name</label>
              <input type="text" id="customer-first-name" required>
            </div>
            <div class="form-group">
              <label for="customer-last-name">Last Name</label>
              <input type="text" id="customer-last-name" required>
            </div>
          </div>
          <div class="form-group">
            <label for="customer-email">Email</label>
            <input type="email" id="customer-email" required>
          </div>
          <div class="form-group">
            <label for="customer-phone">Phone</label>
            <input type="tel" id="customer-phone">
          </div>
          <div class="form-group">
            <label for="customer-address">Address</label>
            <textarea id="customer-address" rows="3"></textarea>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button class="btn-secondary" id="cancel-customer">Cancel</button>
        <button class="btn-primary" id="save-customer">Save Customer</button>
      </div>
    </div>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <script src="css/script.js"></script>
</body>
</html>
