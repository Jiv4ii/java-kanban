public class SubTask extends Task{
    int epicId;

    public SubTask( int epicId, String name, String description,  String status) {
        super(name, description, status);
        this.epicId = epicId;
    }
    public SubTask(int epicId, String name, String description,  String status, int id) {
        super(name, description, status);
        this.epicId = epicId;
        this.id = id;
    }
}
