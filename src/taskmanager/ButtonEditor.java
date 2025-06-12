package taskmanager;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

public class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private int selectedRow;
    private TaskDAO dao;
    private TaskManagerGUI gui;

    public ButtonEditor(JCheckBox checkBox, TaskDAO dao, TaskManagerGUI gui) {
        super(checkBox);
        this.dao = dao;
        this.gui = gui;

        button = new JButton("🗑 Delete");
        button.addActionListener(e -> {
            int taskId = (int) gui.taskTable.getValueAt(selectedRow, 0);
            dao.deleteTask(taskId);
            gui.refreshTaskTable();
        });
    }

    @Override
    public Component getTableCellEditorComponent(
        JTable table, Object value, boolean isSelected, int row, int column
    ) {
        selectedRow = row;
        return button;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return true;
    }
}
