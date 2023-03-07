import java.util.ArrayList;
import java.util.Objects;

public class EpicTask extends Task {

    ArrayList<SubTask> epicsSubTasks;


    public EpicTask(String name, String description, String status) {
        super(name, description, status);
        epicsSubTasks = new ArrayList<>();

    }

}
