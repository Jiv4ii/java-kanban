package entities;

public class SubTask extends Task {
   protected int epicId;


    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public SubTask(int epicId, String name, String description, Status status) {
        super(name, description, status);
        this.epicId = epicId;
    }
    public SubTask(int epicId, String name, String description,  Status status, int id) {
        super(name, description, status);
        this.epicId = epicId;
        this.id = id;
    }

}
