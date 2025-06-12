package taskmanager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import javax.swing.SpinnerDateModel;
import java.text.SimpleDateFormat;
import com.toedter.calendar.JDateChooser;


public class TaskManagerGUI extends JFrame {
    private TaskDAO dao = new TaskDAO();
    JTable taskTable;
    private DefaultTableModel tableModel;
    private boolean sortAscending = false;

    public TaskManagerGUI() {
        setTitle("Task Manager");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("View Tasks", createViewPanel());
        tabs.add("Add Task", createAddPanel());

        add(tabs, BorderLayout.CENTER);
        setVisible(true);
    }

    // 📋 View Tasks Panel
    private JPanel createViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{
            "ID", "Title", "Description", "Due Date", "Priority", "Status", "Action"
        },0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only allow editing in Status and Action columns
                return column == 5 || column == 6 || column == 4;
            }
            public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 0 -> Integer.class;          // ID
                    case 3 -> java.time.LocalDate.class; // Due Date as LocalDate
                    default -> String.class;
                };
            }
        };

        taskTable = new JTable(tableModel);
        taskTable.setAutoCreateRowSorter(false);  // 🚫 disables visual override

        // 👇 Add ComboBox editor for the Status column
        JComboBox<String> statusBox = new JComboBox<>(new String[]{
            "PENDING", "IN_PROGRESS", "COMPLETED"
        });
        JComboBox<String> PriorityBox = new JComboBox<>(new String[]{
        		"HIGH", "MEDIUM", "LOW"
        });
        taskTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(PriorityBox));
        
        taskTable.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(statusBox));

        // 👇 Add Button for Delete in the Action column
        taskTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        taskTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox(), dao, this));

        refreshTaskTable();

        panel.add(new JScrollPane(taskTable), BorderLayout.CENTER);
        // Add both Refresh and Save Status buttons
        JPanel buttonPanel = new JPanel();

        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.addActionListener(e -> refreshTaskTable());

        JButton saveBtn = new JButton("💾 Save Status Changes");
        saveBtn.addActionListener(e -> saveStatusUpdates());
        
        JButton smartSortBtn = new JButton("↕️ Sort by Priority");
        smartSortBtn.addActionListener(e -> {
            if (sortAscending) {
                loadTasksSortedByPriority("ASC");
                smartSortBtn.setText("⬇️ Sort by Priority");
            } else {
                loadTasksSortedByPriority("DESC");
                smartSortBtn.setText("⬆️ Sort by Priority");
            }
            sortAscending = !sortAscending;
        });
  
        JButton sortBtn = new JButton("Sort by dueDate");
  		sortBtn.addActionListener(e -> {
      if (sortAscending) {
          loadTasksSortedByPriority("ASC");
          sortBtn.setText("⬇️ Sort by dueDate");
      } else {
          loadTasksSortedByPriority("DESC");
          sortBtn.setText("⬆️ Sort by dueDate");
      }
      sortAscending = !sortAscending;
  });   
  		//JButton sortBtn2 = new JButton("Sort by taskCompleted");
       // sortBtn.addActionListener(e -> sortByPriority());

        buttonPanel.add(refreshBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(smartSortBtn);
        buttonPanel.add(sortBtn);
        

        panel.add(buttonPanel, BorderLayout.SOUTH);


        return panel;
    }


// 📝 Add Task Panel
    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));

        JTextField titleField = new JTextField();
        JTextField descField = new JTextField();
        JTextField dueDateField = new JTextField("dd/MM/yyyy");
        JComboBox<String> priorityBox = new JComboBox<>(new String[]{"HIGH", "MEDIUM", "LOW"});

        JButton addBtn = new JButton("Add Task");

        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Description:"));
        panel.add(descField);
        JDateChooser dateChooser1 = new JDateChooser();
        dateChooser1.setDateFormatString("dd/MM/yyyy");

        panel.add(new JLabel("Due Date:"));
        panel.add(dateChooser1);

        panel.add(new JLabel("Priority:"));
        panel.add(priorityBox);
        panel.add(new JLabel(""));
        panel.add(addBtn);

        addBtn.addActionListener(e -> {
            try {
                String title = titleField.getText().trim();
                String desc = descField.getText().trim();

                Date selected = dateChooser1.getDate();
                if (selected == null) {
                    JOptionPane.showMessageDialog(this, "❌ Please select a due date.");
                    return;
                }

                LocalDate dueDate = selected.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                if (dueDate.isBefore(LocalDate.now())) {
                    JOptionPane.showMessageDialog(this, "❌ Due date must be today or later.");
                    return;
                }

                Task.Priority priority = Task.Priority.valueOf(priorityBox.getSelectedItem().toString());
                Task newTask = new Task(title, desc, dueDate, priority);
                dao.addTask(newTask);

                JOptionPane.showMessageDialog(this, "✅ Task added.");
                titleField.setText("");
                descField.setText("");
                dateChooser1.setDate(new Date()); // reset to today

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error adding task: " + ex.getMessage());
            }
        });


        return panel;
    }

    void refreshTaskTable() {
        tableModel.setRowCount(0);
        List<Task> tasks = dao.getAllTasks();
        for (Task t : tasks) {
            tableModel.addRow(new Object[]{
                t.getId(), t.getTitle(), t.getDescription(),
                t.getDueDate(), t.getPriority(), t.getStatus()
            });
        }
    }
    private void saveStatusUpdates() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int id = (int) tableModel.getValueAt(i, 0);
         String statusStr = tableModel.getValueAt(i, 5).toString();

            Task.Status status = Task.Status.valueOf(statusStr);
            dao.updateStatus(id, status);
        }
        JOptionPane.showMessageDialog(this, "✅ All statuses updated.");
        refreshTaskTable();
    }
    private void sortByDueDate(String direction) {
		// TODO Auto-generated method stub
    	tableModel.setRowCount(0);
        List<Task> sortedTasks = dao.getTasksByDueDate(direction);
        for (Task t : sortedTasks) {
        	System.out.println(t.getDueDate());  
            tableModel.addRow(new Object[]{
                t.getId(), t.getTitle(), t.getDescription(),
                t.getDueDate(), t.getPriority(), t.getStatus(), "Delete"
            });
        }
        return; 
	}
    private void loadTasksSortedByPriority(String direction) {
        tableModel.setRowCount(0);
        List<Task> sortedTasks = dao.getTasksByPriority(direction);
        for (Task t : sortedTasks) {
            tableModel.addRow(new Object[]{
                t.getId(), t.getTitle(), t.getDescription(),
                t.getDueDate(), t.getPriority(), t.getStatus(), "Delete"
            });
        }
    }


}
