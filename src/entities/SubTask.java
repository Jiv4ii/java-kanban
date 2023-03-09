package entities;

public class SubTask extends Task {
    int epicId;

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", getStatus='" + getStatus + '\'' +
                ", id=" + id +
                '}';
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public SubTask(int epicId, String name, String description, String status) {
        super(name, description, status);
        this.epicId = epicId;
    }
    public SubTask(int epicId, String name, String description,  String status, int id) {
        super(name, description, status);
        this.epicId = epicId;
        this.id = id;
    }
}
