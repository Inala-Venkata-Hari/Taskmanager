# 🗂️ Task Manager GUI App (Java + MySQL)

A full-featured **Task Management Desktop Application** built using **Java Swing** and **MySQL**, designed to help users create, update, sort, and track tasks with real-time status updates and due dates.

> 💡 This project is a showcase for an **Application Developer role** — built from CLI to GUI using core Java and JDBC.

---

## 🚀 Features

- ✅ Add new tasks with title, description, due date, and priority
- ✅ GUI-based form using Swing components
- ✅ View all tasks in a sortable `JTable`
- ✅ Smart sorting by:
  - 🔼/🔽 Due Date
  - 🔼/🔽 Priority
- ✅ Update task status via dropdown or button
- ✅ Automatic overdue validation
- ✅ MySQL integration with DAO pattern
- 🖥️ User-friendly interface using `JFrame`, `JTable`, `JPanel`, and `JDateChooser`

---

## 🧠 Technologies Used

| Layer | Tools |
|-------|-------|
| Language | Java (Java SE 8+) |
| GUI | Swing (JFrame, JTable, JComboBox, JDateChooser) |
| Database | MySQL |
| DB Access | JDBC |
| Architecture | DAO pattern |

---

## 📸 Screenshots

![Screenshot 2025-06-12 124349](https://github.com/user-attachments/assets/007cd71e-10e5-48c5-b99b-894a1f5d188d)



---

## 📂 How to Run

1. Clone the repo:
   ```bash
   git clone https://github.com/yourusername/TaskManager-GUI.git
   cd TaskManager-GUI

2. Import into your IDE (Eclipse/IntelliJ/VS Code)

3. Add MySQL Connector .jar to your classpath:

    Download MySQL Connector/J

4. Create the database:

sql
CREATE DATABASE taskmanager;
Create the table using this structure:

5. 
sql
CREATE TABLE tasks (
  id INT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255),
  description TEXT,
  due_date DATE,
  priority ENUM('HIGH', 'MEDIUM', 'LOW'),
  status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED')
);
6. Set DB credentials in DatabaseUtil.java

7. Run Main.java — the GUI should launch!
