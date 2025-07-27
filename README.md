# 🚀 Employee Management System (EMS)

A comprehensive **desktop application** built with **Java Swing** and powered by a **MySQL database**. This system offers a user-friendly interface for performing all standard **CRUD** operations (Create, Read, Update, Delete) on employee records.

---

## 📸 Screenshots

### <p align="center">🏠 Home Screen</p>

<img width="955" height="649" alt="Home Screen" src="https://github.com/user-attachments/assets/81f6ccda-0d3a-45a6-a9e3-f9724b61b77c" />

### <p align="center">📋 View All Employees</p>

<img width="1920" height="1008" alt="View All Employees 1" src="https://github.com/user-attachments/assets/2a08f1b5-e07e-4bdd-8eee-53d5c841dbff" />  
<img width="1920" height="1008" alt="View All Employees 2" src="https://github.com/user-attachments/assets/7def4917-3398-47cb-88eb-f92ddb5a2df0" />

### <p align="center">➕ Add Employee</p>

<img width="1920" height="1008" alt="Add Employee 1" src="https://github.com/user-attachments/assets/ad2abdf5-1258-47fd-9ffb-2a1d842894c9" />  
<img width="1920" height="1008" alt="Add Employee 2" src="https://github.com/user-attachments/assets/c37efb4e-6bc3-483c-bb64-2e4a4dfa84c9" />

### <p align="center">❌ Delete Employee</p>

<img width="1920" height="1008" alt="Delete Employee" src="https://github.com/user-attachments/assets/90fa0d75-026b-44db-be79-65ab623257c4" />

### <p align="center">✏️ Update Employee</p>

<img width="955" height="649" alt="Update Employee 1" src="https://github.com/user-attachments/assets/2f97d07c-bc67-4dbc-935d-7cf25944f8a2" />  
<img width="1920" height="1008" alt="Update Employee 2" src="https://github.com/user-attachments/assets/86411278-55fc-47e5-813b-46b77f3899a1" />

---

## ✨ Features

* **View All Employees**
  Display a complete list of all employees in a clean, sortable table.

* **Add New Employee**
  Onboard new employees using a dedicated form that collects name, salary, department, and position.

* **Update Employee Information**
  Search an employee by ID, view their current data, and update the necessary fields.

* **Delete Employee**
  A two-step confirmation process to search and delete an employee from the database.

* **Search by ID**
  Quickly find any employee's details using their unique employee ID.

* **User-Friendly Interface**
  Intuitive GUI built with Java Swing to streamline all interactions.

---

## 🛠️ Tech Stack

* **Frontend:** Java Swing
* **Backend:** Java
* **Database:** MySQL
* **Database Driver:** JDBC (Java Database Connectivity)

---

## 🚀 Getting Started

### ✅ Prerequisites

Ensure the following software is installed on your system:

* **Java Development Kit (JDK)**: Version 21 or newer
* **MySQL Server**: Version 8.0 or newer
* **IDE**: IntelliJ IDEA, Eclipse, or any Java-compatible IDE

---

### 📅 Installation & Setup

1. **Clone the Repository**

```bash
git clone https://github.com/TyrantAbhay/Employee_Management_System.git
cd Employee_Management_System
```

2. **Database Setup**

Start your MySQL server, then run the following SQL script to set up the database:

```sql
-- Create the database
CREATE DATABASE ems;

-- Switch to the new database
USE ems;

-- Create the employee table
CREATE TABLE emsystem (
    id INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(100) NOT NULL,
    Salary INT,
    Department VARCHAR(100),
    Position VARCHAR(100)
);
```

3. **Configure Database Credentials**

In your IDE, open the project. The database credentials are currently hardcoded in the `.java` files:

* **URL:** `jdbc:mysql://localhost:3306/ems`
* **Username:** `root`
* **Password:** `9845`

> ⚠️ Either update the password in your MySQL to match `9845` or change it in all relevant Java files.

4. **Run the Application**

* Locate the `Home.java` file
* Right-click on it → `Run As` → `Java Application`
* The EMS GUI should now launch

---

## 👨‍💻 Author

**Created by:** Abhay
**Last updated:** July 2024
