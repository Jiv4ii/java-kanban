package entities;

import java.util.ArrayList;

public class EpicTask extends Task {

    protected ArrayList<SubTask> epicsSubTasks;
    protected String type = "EpicTask";

    public ArrayList<SubTask> getEpicsSubTasks() {
        return epicsSubTasks;
    }




    public EpicTask(String name, String description, Status status) {
        super(name, description, status);
        epicsSubTasks = new ArrayList<>();

    }

    public EpicTask(int id, String name, String description, Status status) {

        super(id, name, description, status);
        epicsSubTasks = new ArrayList<>();
    }

    @Override
    public String toString() {
         return id + "," + type + "," + name + "," + status + "," + description;
    }
}
