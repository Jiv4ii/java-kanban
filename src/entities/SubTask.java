package entities;

import com.google.gson.annotations.SerializedName;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class SubTask extends Task {

   protected int epicId;
    @SerializedName("subType")
    protected String type = "SubTask";

    public SubTask(String name, String description, Status status, int epicId, Instant startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(int id, String name, String description, Status status, int epicId, Instant startTime, Duration duration) {
        super(id, name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, Instant startTime, Duration duration) {
        super(name, description, status, startTime, duration);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return id + "," + type + "," + name + "," + status + "," + description + "," + epicId + "," + startTime + "," + duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId && Objects.equals(type, subTask.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId, type);
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
