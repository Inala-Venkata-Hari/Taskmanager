package taskmanager;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    // ✅ Insert a task into the DB
    public void addTask(Task task) {
        String sql = "INSERT INTO tasks (title, description, due_date, priority, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setDate(3, Date.valueOf(task.getDueDate()));
            stmt.setString(4, task.getPriority().name());
            stmt.setString(5, task.getStatus().name());

            stmt.executeUpdate();
            System.out.println("✅ Task added successfully.");

        } catch (SQLException e) {
            System.out.println("❌ Error adding task: " + e.getMessage());
        }
    }

    // ✅ Get all tasks
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tasks.add(mapRowToTask(rs));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching tasks: " + e.getMessage());
        }

        return tasks;
    }

    // ✅ Update task status
    public void updateStatus(int taskId, Task.Status newStatus) {
        String sql = "UPDATE tasks SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus.name());
            stmt.setInt(2, taskId);

            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("✅ Task status updated.");
            else System.out.println("⚠️ Task not found.");

        } catch (SQLException e) {
            System.out.println("❌ Error updating status: " + e.getMessage());
        }
    }

    // ✅ Delete a task
    public void deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, taskId);
            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("✅ Task deleted.");
            else System.out.println("⚠️ Task not found.");

        } catch (SQLException e) {
            System.out.println("❌ Error deleting task: " + e.getMessage());
        }
    }

    // ✅ Get tasks due today or overdue
    public List<Task> getTasksDueBy(LocalDate date) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE due_date <= ? ORDER BY due_date ASC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tasks.add(mapRowToTask(rs));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching due tasks: " + e.getMessage());
        }

        return tasks;
    }

    // ✅ Get tasks sorted by priority (HIGH → LOW)
    public List<Task> getTasksByPriority() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY FIELD(priority, 'HIGH', 'MEDIUM', 'LOW')";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tasks.add(mapRowToTask(rs));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error sorting tasks: " + e.getMessage());
        }

        return tasks;
    }

    // ✅ Helper method to convert ResultSet → Task object
    private static Task mapRowToTask(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String desc = rs.getString("description");
        LocalDate dueDate = rs.getDate("due_date").toLocalDate();
        Task.Priority priority = Task.Priority.valueOf(rs.getString("priority"));
        Task.Status status = Task.Status.valueOf(rs.getString("status"));

        return new Task(id, title, desc, dueDate, priority, status);
    }

    public List<Task> getTasksByPriority(String direction) {
        List<Task> tasks = new ArrayList<>();
        String order = direction.equalsIgnoreCase("ASC")
            ? "FIELD(priority, 'LOW', 'MEDIUM', 'HIGH')"
            : "FIELD(priority, 'HIGH', 'MEDIUM', 'LOW')";

        String sql = "SELECT * FROM tasks ORDER BY " + order;

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
        	System.out.println("📥 SQL: SELECT * FROM tasks ORDER BY due_date " + direction);

            while (rs.next()) {
            	System.out.println("🗂 Task found: " + rs.getString("title") + " | " + rs.getDate("due_date"));

                tasks.add(mapRowToTask(rs));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error sorting tasks: " + e.getMessage());
        }

        return tasks;
    }

	public static List<Task> getTasksByDueDate(String direction) {
		// TODO Auto-generated method stub
		List<Task> tasks = new ArrayList<>();
        String order = direction.equalsIgnoreCase("ASC")
            ? "ASC"
            : "DESC";

        String sql = "SELECT * FROM tasks ORDER BY due_date " + order;


        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tasks.add(mapRowToTask(rs));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error sorting tasks: " + e.getMessage());
        }

        return tasks;
	}

}
