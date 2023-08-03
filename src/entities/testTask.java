package entities;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class testTask {

    protected Integer id;
    protected String name;

    protected String description;
    protected Status status;
    protected String type = "Task";



    public testTask(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;

    }

    public testTask(int id, String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;

    }

    public String getType() {
        return type;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    @Override
    public String toString() {
        return id + "," + type + "," + name + "," + status + "," + description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status && Objects.equals(type, task.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, type);
    }


}
