package entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

public class EpicTask extends Task {

    protected ArrayList<SubTask> epicsSubTasks;
    protected String type = "EpicTask";
    protected Instant endTime;

    public ArrayList<SubTask> getEpicsSubTasks() {
        return epicsSubTasks;
    }




    public EpicTask(String name, String description) {
        super(name, description, Status.NEW, null, null);
        epicsSubTasks = new ArrayList<>();

    }

    public EpicTask(int id, String name, String description) {
        super(id, name, description, Status.NEW,null, null);
        epicsSubTasks = new ArrayList<>();
    }

    @Override
    public String toString() {
         return id + "," + type + "," + name + "," + status + "," + description + "," + startTime + "," + duration + "," + endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EpicTask epicTask = (EpicTask) o;
        return Objects.equals(epicsSubTasks, epicTask.epicsSubTasks) && Objects.equals(type, epicTask.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(epicsSubTasks, type);
    }

    public void setEndTime(Instant endtime) {
        this.endTime = endtime;
    }
}
