public class Task {
    String name;
    String description;
    String status;
    int id;

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = status;

    }

    public Task(String name, String description, String status, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;

    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id +
                '}';
    }
}