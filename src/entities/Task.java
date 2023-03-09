package entities;

public class Task {

    String name;
    String description;
    String getStatus;
    int id;

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.getStatus = status;

    }

    public Task(String name, String description, String status, int id) {
        this.name = name;
        this.description = description;
        this.getStatus = status;
        this.id = id;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return getStatus;
    }

    public void setStatus(String status) {
        this.getStatus = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    @Override
    public String toString() {
        return "entities.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + getStatus + '\'' +
                ", id=" + id +
                '}';
    }
}
