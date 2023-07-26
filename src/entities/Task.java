package entities;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class Task {

    protected Integer id;
    protected String name;

    protected String description;
    protected Status status;
    protected String type = "Task";
    protected Instant startTime;

    protected Duration duration;



    public Task(String name, String description, Status status, Instant startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;

    }

    public Task(int id, String name, String description, Status status, Instant startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
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

    public void setId(int id) {
        this.id = id;
    }

    public Instant getEndTime(){
        return startTime.plus(duration);
    }

    public  Instant getStartTime(){
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }


    @Override
    public String toString() {
        return id + "," + type + "," + name + "," + status + "," + description + "," + startTime + "," + duration;
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

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

}
