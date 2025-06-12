
 package taskmanager; /*import java.time.LocalDate; import java.util.ArrayList;
 * import java.util.List; import java.util.Scanner;
 * 
 * public class TaskManager { private List<Task> tasks = new ArrayList<>();
 * private int nextId = 1; private Scanner scanner = new Scanner(System.in);
 * 
 * public void showMenu() { while (true) {
 * System.out.println("\n=== TASK MANAGER ===");
 * System.out.println("1. Add Task"); System.out.println("2. View Tasks");
 * System.out.println("3. Update Task Status");
 * System.out.println("4. Delete Task"); System.out.println("5. Exit");
 * System.out.print("Choose option: ");
 * 
 * int choice = scanner.nextInt(); scanner.nextLine(); // clear input
 * 
 * switch (choice) { case 1 -> addTask(); case 2 -> viewTasks(); case 3 ->
 * updateTaskStatus(); case 4 -> deleteTask(); case 5 -> {
 * System.out.println("Goodbye!"); return; } default ->
 * System.out.println("Invalid choice. Try again."); } } }
 * 
 * private void addTask() { System.out.print("Enter title: "); String title =
 * scanner.nextLine(); System.out.print("Enter description: "); String desc =
 * scanner.nextLine(); System.out.print("Enter due date (YYYY-MM-DD): ");
 * LocalDate date = LocalDate.parse(scanner.nextLine());
 * 
 * System.out.println("Choose Priority (1. HIGH, 2. MEDIUM, 3. LOW): "); int p =
 * scanner.nextInt(); scanner.nextLine(); Task.Priority priority = switch (p) {
 * case 1 -> Task.Priority.HIGH; case 2 -> Task.Priority.MEDIUM; default ->
 * Task.Priority.LOW; };
 * 
 * Task task = new Task(nextId++, title, desc, date, priority,
 * Task.Status.PENDING); tasks.add(task); System.out.println("Task added."); }
 * 
 * private void viewTasks() { if (tasks.isEmpty()) {
 * System.out.println("No tasks found."); return; } for (Task task : tasks) {
 * System.out.println(task); } }
 * 
 * private void updateTaskStatus() {
 * System.out.print("Enter Task ID to update: "); int id = scanner.nextInt();
 * scanner.nextLine(); Task found = findById(id); if (found == null) {
 * System.out.println("Task not found."); return; }
 * 
 * System.out.
 * println("Choose new status (1. PENDING, 2. IN_PROGRESS, 3. COMPLETED): ");
 * int statusChoice = scanner.nextInt(); scanner.nextLine();
 * 
 * Task.Status newStatus = switch (statusChoice) { case 2 ->
 * Task.Status.IN_PROGRESS; case 3 -> Task.Status.COMPLETED; default ->
 * Task.Status.PENDING; };
 * 
 * found.setStatus(newStatus); System.out.println("Status updated."); }
 * 
 * private void deleteTask() { System.out.print("Enter Task ID to delete: ");
 * int id = scanner.nextInt(); scanner.nextLine(); Task found = findById(id); if
 * (found != null) { tasks.remove(found); System.out.println("Task deleted."); }
 * else { System.out.println("Task not found."); } }
 * 
 * private Task findById(int id) { for (Task task : tasks) { if (task.getId() ==
 * id) return task; } return null; } }
 */
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    private final TaskDAO dao = new TaskDAO();
    private final Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        while (true) {
            System.out.println("\n=== TASK MANAGER (DB Version) ===");
            System.out.println("1. Add Task");
            System.out.println("2. View All Tasks");
            System.out.println("3. View Tasks Due Today/Overdue");
            System.out.println("4. View Tasks by Priority");
            System.out.println("5. Update Task Status");
            System.out.println("6. Delete Task");
            System.out.println("7. Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            switch (choice) {
                case 1 -> addTask();
                case 2 -> viewAllTasks();
                case 3 -> viewDueTasks();
                case 4 -> viewByPriority();
                case 5 -> updateStatus();
                case 6 -> deleteTask();
                case 7 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void addTask() {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter description: ");
        String desc = scanner.nextLine();
        System.out.print("Enter due date (d/M/yyyy): ");
        String input = scanner.nextLine();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate dueDate = LocalDate.parse(input, fmt);

        System.out.println("Choose Priority (1. HIGH, 2. MEDIUM, 3. LOW): ");
        int p = scanner.nextInt();
        scanner.nextLine();
        Task.Priority priority = switch (p) {
            case 1 -> Task.Priority.HIGH;
            case 2 -> Task.Priority.MEDIUM;
            default -> Task.Priority.LOW;
        };

        Task task = new Task(title, desc, dueDate, priority);
        dao.addTask(task);
    }

    private void viewAllTasks() {
        List<Task> tasks = dao.getAllTasks();
        if (tasks.isEmpty()) System.out.println("No tasks found.");
        else tasks.forEach(System.out::println);
    }

    private void viewDueTasks() {
        LocalDate today = LocalDate.now();
        List<Task> dueTasks = dao.getTasksDueBy(today);
        if (dueTasks.isEmpty()) System.out.println("No tasks due today or overdue.");
        else dueTasks.forEach(System.out::println);
    }

    private void viewByPriority() {
        List<Task> sortedTasks = dao.getTasksByPriority();
        if (sortedTasks.isEmpty()) System.out.println("No tasks found.");
        else sortedTasks.forEach(System.out::println);
    }

    private void updateStatus() {
        System.out.print("Enter Task ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Choose new status: 1. PENDING  2. IN_PROGRESS  3. COMPLETED");
        int choice = scanner.nextInt();
        scanner.nextLine();

        Task.Status status = switch (choice) {
            case 2 -> Task.Status.IN_PROGRESS;
            case 3 -> Task.Status.COMPLETED;
            default -> Task.Status.PENDING;
        };

        dao.updateStatus(id, status);
    }

    private void deleteTask() {
        System.out.print("Enter Task ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        dao.deleteTask(id);
    }
}
