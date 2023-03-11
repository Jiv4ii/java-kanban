package entities;

import java.util.ArrayList;

public class EpicTask extends Task {

    protected ArrayList<SubTask> getEpicsSubTasks;

    public ArrayList<SubTask> getEpicsSubTasks() {
        return getEpicsSubTasks;
    }




    public EpicTask(String name, String description, String status) {
        super(name, description, status);
        getEpicsSubTasks = new ArrayList<>();

    }

}
