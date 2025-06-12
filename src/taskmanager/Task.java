package taskmanager;
import java.time.LocalDate;

public class Task {
    private int id; // optional if using auto-increment in DB
    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority;
    private Status status;

    public enum Priority {
        HIGH, MEDIUM, LOW
    }

    public enum Status {
        PENDING, IN_PROGRESS, COMPLETED
    }

    // Constructor
    public Task(String title, String description, LocalDate dueDate, Priority priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = Status.PENDING;
    }

    // Overloaded constructor with ID
    public Task(int id, String title, String description, LocalDate dueDate, Priority priority, Status status) {
        this(title, description, dueDate, priority);
        this.id = id;
        this.status = status;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDueDate() { return dueDate; }
    public Priority getPriority() { return priority; }
    public Status getStatus() { return status; }

    public void setStatus(Status status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Task[id=%d, title='%s', due=%s, priority=%s, status=%s]",
                id, title, dueDate, priority, status);
    }
}
