import { Chart } from "@/components/ui/chart"
document.addEventListener("DOMContentLoaded", () => {
  // Initialize the application
  initApp()

  // Load sample data
  loadSampleData()

  // Initialize charts
  initCharts()
})

// Initialize the application
function initApp() {
  // Navigation
  const navLinks = document.querySelectorAll(".nav-links li")
  navLinks.forEach((link) => {
    link.addEventListener("click", function () {
      const page = this.getAttribute("data-page")
      showPage(page)

      // Update active state
      navLinks.forEach((l) => l.classList.remove("active"))
      this.classList.add("active")
    })
  })

  // Language selector
  const languageSelector = document.getElementById("language")
  languageSelector.addEventListener("change", function () {
    changeLanguage(this.value)
  })

  // Theme toggle
  const themeToggle = document.querySelector(".theme-toggle")
  themeToggle.addEventListener("click", () => {
    toggleDarkMode()
  })

  // Modal functionality
  setupModals()

  // Product form
  setupProductForm()

  // Customer form
  setupCustomerForm()
}

// Show the selected page
function showPage(pageId) {
  const pages = document.querySelectorAll(".page")
  pages.forEach((page) => {
    page.classList.add("hidden")
  })

  const selectedPage = document.getElementById(pageId)
  if (selectedPage) {
    selectedPage.classList.remove("hidden")
  }
}

// Change language
function changeLanguage(language) {
  console.log(`Language changed to: ${language}`)
  // In a real application, this would load language files and update the UI

  // Example of updating some text elements
  const translations = {
    en: {
      dashboard: "Dashboard",
      products: "Products",
      customers: "Customers",
      sales: "Sales",
      purchases: "Purchases",
      expenses: "Expenses",
      reports: "Reports",
      settings: "Settings",
    },
    vi: {
      dashboard: "Bảng điều khiển",
      products: "Sản phẩm",
      customers: "Khách hàng",
      sales: "Bán hàng",
      purchases: "Mua hàng",
      expenses: "Chi phí",
      reports: "Báo cáo",
      settings: "Cài đặt",
    },
    fr: {
      dashboard: "Tableau de bord",
      products: "Produits",
      customers: "Clients",
      sales: "Ventes",
      purchases: "Achats",
      expenses: "Dépenses",
      reports: "Rapports",
      settings: "Paramètres",
    },
    es: {
      dashboard: "Panel de control",
      products: "Productos",
      customers: "Clientes",
      sales: "Ventas",
      purchases: "Compras",
      expenses: "Gastos",
      reports: "Informes",
      settings: "Ajustes",
    },
  }

  // Update navigation items
  const navLinks = document.querySelectorAll(".nav-links li")
  navLinks.forEach((link) => {
    const page = link.getAttribute("data-page")
    if (translations[language] && translations[language][page]) {
      // Find the text node (skip the icon)
      const textNode = Array.from(link.childNodes).find((node) => node.nodeType === 3)
      if (textNode) {
        textNode.nodeValue = ` ${translations[language][page]}`
      }
    }
  })

  // Update page headers
  document.querySelectorAll(".page h1").forEach((header) => {
    const pageId = header.closest(".page").id
    if (translations[language] && translations[language][pageId]) {
      header.textContent = translations[language][pageId]
    }
  })
}

// Toggle dark mode
function toggleDarkMode() {
  document.body.classList.toggle("dark-mode")

  const themeIcon = document.querySelector(".theme-toggle i")
  if (document.body.classList.contains("dark-mode")) {
    themeIcon.classList.remove("fa-moon")
    themeIcon.classList.add("fa-sun")
  } else {
    themeIcon.classList.remove("fa-sun")
    themeIcon.classList.add("fa-moon")
  }
}

// Setup modals
function setupModals() {
  // Product modal
  const addProductBtn = document.getElementById("add-product-btn")
  const productModal = document.getElementById("product-modal")
  const closeProductModal = productModal.querySelector(".close-modal")
  const cancelProduct = document.getElementById("cancel-product")

  addProductBtn.addEventListener("click", () => {
    productModal.classList.add("active")
    document.getElementById("product-form").reset()
    document.getElementById("product-modal-title").textContent = "Add New Product"
    document.getElementById("image-preview").style.backgroundImage = ""
    document.getElementById("image-preview").textContent = "No image selected"
  })

  closeProductModal.addEventListener("click", () => {
    productModal.classList.remove("active")
  })

  cancelProduct.addEventListener("click", () => {
    productModal.classList.remove("active")
  })

  // Customer modal
  const addCustomerBtn = document.getElementById("add-customer-btn")
  const customerModal = document.getElementById("customer-modal")
  const closeCustomerModal = customerModal.querySelector(".close-modal")
  const cancelCustomer = document.getElementById("cancel-customer")

  addCustomerBtn.addEventListener("click", () => {
    customerModal.classList.add("active")
    document.getElementById("customer-form").reset()
    document.getElementById("customer-modal-title").textContent = "Add New Customer"
  })

  closeCustomerModal.addEventListener("click", () => {
    customerModal.classList.remove("active")
  })

  cancelCustomer.addEventListener("click", () => {
    customerModal.classList.remove("active")
  })

  // Close modals when clicking outside
  window.addEventListener("click", (event) => {
    if (event.target === productModal) {
      productModal.classList.remove("active")
    }
    if (event.target === customerModal) {
      customerModal.classList.remove("active")
    }
  })
}

// Setup product form
function setupProductForm() {
  const productImage = document.getElementById("product-image")
  const imagePreview = document.getElementById("image-preview")
  const saveProduct = document.getElementById("save-product")

  productImage.addEventListener("change", (event) => {
    const file = event.target.files[0]
    if (file) {
      const reader = new FileReader()
      reader.onload = (e) => {
        imagePreview.style.backgroundImage = `url(${e.target.result})`
        imagePreview.textContent = ""
      }
      reader.readAsDataURL(file)
    }
  })

  saveProduct.addEventListener("click", () => {
    const form = document.getElementById("product-form")
    if (form.checkValidity()) {
      // Get form values
      const name = document.getElementById("product-name").value
      const category = document.getElementById("product-category").value
      const price = document.getElementById("product-price").value
      const stock = document.getElementById("product-stock").value

      // In a real application, this would send data to a server
      console.log("Product saved:", { name, category, price, stock })

      // Add to table (for demo purposes)
      addProductToTable({
        id: Math.floor(Math.random() * 1000),
        name,
        category,
        price,
        stock,
        status: stock > 0 ? "In Stock" : "Out of Stock",
      })

      // Close modal
      document.getElementById("product-modal").classList.remove("active")
    } else {
      form.reportValidity()
    }
  })
}

// Setup customer form
function setupCustomerForm() {
  const saveCustomer = document.getElementById("save-customer")

  saveCustomer.addEventListener("click", () => {
    const form = document.getElementById("customer-form")
    if (form.checkValidity()) {
      // Get form values
      const firstName = document.getElementById("customer-first-name").value
      const lastName = document.getElementById("customer-last-name").value
      const email = document.getElementById("customer-email").value
      const phone = document.getElementById("customer-phone").value

      // In a real application, this would send data to a server
      console.log("Customer saved:", { firstName, lastName, email, phone })

      // Add to table (for demo purposes)
      addCustomerToTable({
        id: Math.floor(Math.random() * 1000),
        name: `${firstName} ${lastName}`,
        email,
        phone,
        totalOrders: 0,
        totalSpent: "$0.00",
        status: "Active",
      })

      // Close modal
      document.getElementById("customer-modal").classList.remove("active")
    } else {
      form.reportValidity()
    }
  })
}

// Add product to table
function addProductToTable(product) {
  const table = document.getElementById("products-table").getElementsByTagName("tbody")[0]
  const row = table.insertRow()

  row.innerHTML = `
    <td>${product.id}</td>
    <td><div class="product-image" style="width: 40px; height: 40px; background-color: #f1f3f4; border-radius: 4px;"></div></td>
    <td>${product.name}</td>
    <td>${product.category}</td>
    <td>$${product.price}</td>
    <td>${product.stock}</td>
    <td><span class="status-badge ${product.status === "In Stock" ? "in-stock" : "out-of-stock"}">${product.status}</span></td>
    <td>
      <button class="action-btn edit-btn"><i class="fas fa-edit"></i></button>
      <button class="action-btn delete-btn"><i class="fas fa-trash"></i></button>
    </td>
  `

  // Add event listeners to the new buttons
  const editBtn = row.querySelector(".edit-btn")
  const deleteBtn = row.querySelector(".delete-btn")

  editBtn.addEventListener("click", () => {
    // Open edit modal
    const productModal = document.getElementById("product-modal")
    productModal.classList.add("active")
    document.getElementById("product-modal-title").textContent = "Edit Product"

    // Fill form with product data
    document.getElementById("product-name").value = product.name
    document.getElementById("product-category").value = product.category
    document.getElementById("product-price").value = product.price
    document.getElementById("product-stock").value = product.stock
  })

  deleteBtn.addEventListener("click", () => {
    if (confirm("Are you sure you want to delete this product?")) {
      row.remove()
    }
  })
}

// Add customer to table
function addCustomerToTable(customer) {
  const table = document.getElementById("customers-table").getElementsByTagName("tbody")[0]
  const row = table.insertRow()

  row.innerHTML = `
    <td>${customer.id}</td>
    <td>${customer.name}</td>
    <td>${customer.email}</td>
    <td>${customer.phone}</td>
    <td>${customer.totalOrders}</td>
    <td>${customer.totalSpent}</td>
    <td><span class="status-badge ${customer.status === "Active" ? "active" : "inactive"}">${customer.status}</span></td>
    <td>
      <button class="action-btn edit-btn"><i class="fas fa-edit"></i></button>
      <button class="action-btn delete-btn"><i class="fas fa-trash"></i></button>
    </td>
  `

  // Add event listeners to the new buttons
  const editBtn = row.querySelector(".edit-btn")
  const deleteBtn = row.querySelector(".delete-btn")

  editBtn.addEventListener("click", () => {
    // Open edit modal
    const customerModal = document.getElementById("customer-modal")
    customerModal.classList.add("active")
    document.getElementById("customer-modal-title").textContent = "Edit Customer"

    // Fill form with customer data
    const nameParts = customer.name.split(" ")
    document.getElementById("customer-first-name").value = nameParts[0]
    document.getElementById("customer-last-name").value = nameParts.slice(1).join(" ")
    document.getElementById("customer-email").value = customer.email
    document.getElementById("customer-phone").value = customer.phone
  })

  deleteBtn.addEventListener("click", () => {
    if (confirm("Are you sure you want to delete this customer?")) {
      row.remove()
    }
  })
}

// Load sample data
function loadSampleData() {
  // Sample products
  const products = [
    { id: 1, name: "Wireless Headphones", category: "Electronics", price: "129.99", stock: 45, status: "In Stock" },
    { id: 2, name: "Smart Watch", category: "Electronics", price: "199.99", stock: 28, status: "In Stock" },
    { id: 3, name: "Bluetooth Speaker", category: "Electronics", price: "79.99", stock: 0, status: "Out of Stock" },
    { id: 4, name: "Laptop Backpack", category: "Accessories", price: "49.99", stock: 12, status: "In Stock" },
    { id: 5, name: "USB-C Cable", category: "Electronics", price: "12.99", stock: 120, status: "In Stock" },
  ]

  // Sample customers
  const customers = [
    {
      id: 1,
      name: "John Doe",
      email: "john.doe@example.com",
      phone: "(555) 123-4567",
      totalOrders: 5,
      totalSpent: "$450.75",
      status: "Active",
    },
    {
      id: 2,
      name: "Jane Smith",
      email: "jane.smith@example.com",
      phone: "(555) 987-6543",
      totalOrders: 3,
      totalSpent: "$290.50",
      status: "Active",
    },
    {
      id: 3,
      name: "Robert Johnson",
      email: "robert.j@example.com",
      phone: "(555) 456-7890",
      totalOrders: 1,
      totalSpent: "$125.00",
      status: "Active",
    },
    {
      id: 4,
      name: "Emily Davis",
      email: "emily.d@example.com",
      phone: "(555) 789-0123",
      totalOrders: 0,
      totalSpent: "$0.00",
      status: "Inactive",
    },
  ]

  // Add products to table
  const productsTable = document.getElementById("products-table").getElementsByTagName("tbody")[0]
  products.forEach((product) => {
    const row = productsTable.insertRow()
    row.innerHTML = `
      <td>${product.id}</td>
      <td><div class="product-image" style="width: 40px; height: 40px; background-color: #f1f3f4; border-radius: 4px;"></div></td>
      <td>${product.name}</td>
      <td>${product.category}</td>
      <td>$${product.price}</td>
      <td>${product.stock}</td>
      <td><span class="status-badge ${product.status === "In Stock" ? "in-stock" : "out-of-stock"}">${product.status}</span></td>
      <td>
        <button class="action-btn edit-btn"><i class="fas fa-edit"></i></button>
        <button class="action-btn delete-btn"><i class="fas fa-trash"></i></button>
      </td>
    `
  })

  // Add customers to table
  const customersTable = document.getElementById("customers-table").getElementsByTagName("tbody")[0]
  customers.forEach((customer) => {
    const row = customersTable.insertRow()
    row.innerHTML = `
      <td>${customer.id}</td>
      <td>${customer.name}</td>
      <td>${customer.email}</td>
      <td>${customer.phone}</td>
      <td>${customer.totalOrders}</td>
      <td>${customer.totalSpent}</td>
      <td><span class="status-badge ${customer.status === "Active" ? "active" : "inactive"}">${customer.status}</span></td>
      <td>
        <button class="action-btn edit-btn"><i class="fas fa-edit"></i></button>
        <button class="action-btn delete-btn"><i class="fas fa-trash"></i></button>
      </td>
    `
  })

  // Add event listeners to all edit and delete buttons
  document.querySelectorAll(".edit-btn").forEach((btn) => {
    btn.addEventListener("click", function () {
      const row = this.closest("tr")
      const table = row.closest("table")

      if (table.id === "products-table") {
        // Open product edit modal
        const productModal = document.getElementById("product-modal")
        productModal.classList.add("active")
        document.getElementById("product-modal-title").textContent = "Edit Product"

        // Fill form with product data
        document.getElementById("product-name").value = row.cells[2].textContent
        document.getElementById("product-category").value = row.cells[3].textContent
        document.getElementById("product-price").value = row.cells[4].textContent.replace("$", "")
        document.getElementById("product-stock").value = row.cells[5].textContent
      } else if (table.id === "customers-table") {
        // Open customer edit modal
        const customerModal = document.getElementById("customer-modal")
        customerModal.classList.add("active")
        document.getElementById("customer-modal-title").textContent = "Edit Customer"

        // Fill form with customer data
        const nameParts = row.cells[1].textContent.split(" ")
        document.getElementById("customer-first-name").value = nameParts[0]
        document.getElementById("customer-last-name").value = nameParts.slice(1).join(" ")
        document.getElementById("customer-email").value = row.cells[2].textContent
        document.getElementById("customer-phone").value = row.cells[3].textContent
      }
    })
  })

  document.querySelectorAll(".delete-btn").forEach((btn) => {
    btn.addEventListener("click", function () {
      const row = this.closest("tr")
      const name = row.cells[1].textContent || row.cells[2].textContent

      if (confirm(`Are you sure you want to delete ${name}?`)) {
        row.remove()
      }
    })
  })
}

// Initialize charts
function initCharts() {
  // Sales chart
  const salesChartCtx = document.getElementById("sales-chart")
  if (salesChartCtx) {
    const salesChart = new Chart(salesChartCtx, {
      type: "line",
      data: {
        labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
        datasets: [
          {
            label: "Sales",
            data: [1500, 1800, 2100, 1900, 2400, 2800, 3200, 3500, 3100, 3400, 3800, 4200],
            borderColor: "#1a73e8",
            backgroundColor: "rgba(26, 115, 232, 0.1)",
            borderWidth: 2,
            tension: 0.3,
            fill: true,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false,
          },
        },
        scales: {
          y: {
            beginAtZero: true,
            grid: {
              color: "rgba(0, 0, 0, 0.05)",
            },
          },
          x: {
            grid: {
              display: false,
            },
          },
        },
      },
    })

    // Update chart when period changes
    document.getElementById("sales-period").addEventListener("change", function () {
      const period = this.value
      let labels, data

      if (period === "weekly") {
        labels = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]
        data = [500, 700, 600, 800, 950, 1200, 1000]
      } else if (period === "monthly") {
        labels = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
        data = [1500, 1800, 2100, 1900, 2400, 2800, 3200, 3500, 3100, 3400, 3800, 4200]
      } else if (period === "yearly") {
        labels = ["2018", "2019", "2020", "2021", "2022", "2023"]
        data = [15000, 18000, 21000, 24000, 28000, 32000]
      }

      salesChart.data.labels = labels
      salesChart.data.datasets[0].data = data
      salesChart.update()
    })
  }

  // Products chart
  const productsChartCtx = document.getElementById("products-chart")
  if (productsChartCtx) {
    const productsChart = new Chart(productsChartCtx, {
      type: "bar",
      data: {
        labels: ["Wireless Headphones", "Smart Watch", "Bluetooth Speaker", "Laptop Backpack", "USB-C Cable"],
        datasets: [
          {
            label: "Units Sold",
            data: [120, 95, 80, 60, 150],
            backgroundColor: [
              "rgba(26, 115, 232, 0.8)",
              "rgba(52, 168, 83, 0.8)",
              "rgba(251, 188, 5, 0.8)",
              "rgba(234, 67, 53, 0.8)",
              "rgba(103, 58, 183, 0.8)",
            ],
            borderRadius: 4,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false,
          },
        },
        scales: {
          y: {
            beginAtZero: true,
            grid: {
              color: "rgba(0, 0, 0, 0.05)",
            },
          },
          x: {
            grid: {
              display: false,
            },
          },
        },
      },
    })

    // Update chart when period changes
    document.getElementById("products-period").addEventListener("change", function () {
      const period = this.value
      let labels, data

      if (period === "weekly") {
        labels = ["Wireless Headphones", "Smart Watch", "Bluetooth Speaker", "Laptop Backpack", "USB-C Cable"]
        data = [25, 18, 15, 12, 30]
      } else if (period === "monthly") {
        labels = ["Wireless Headphones", "Smart Watch", "Bluetooth Speaker", "Laptop Backpack", "USB-C Cable"]
        data = [120, 95, 80, 60, 150]
      } else if (period === "yearly") {
        labels = ["Wireless Headphones", "Smart Watch", "Bluetooth Speaker", "Laptop Backpack", "USB-C Cable"]
        data = [1450, 1200, 950, 750, 1800]
      }

      productsChart.data.labels = labels
      productsChart.data.datasets[0].data = data
      productsChart.update()
    })
  }
}
