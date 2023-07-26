package interfaces;

import entities.EpicTask;
import entities.SubTask;
import entities.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

public interface TaskManager {
    Collection<Task> showTasksList();
    Collection<EpicTask> showEpicTasksList();
    Collection<SubTask> showSubTasksList();
    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubTasks();
    Task getById(int searchId);
    void createTask(Task createdTask);
    void createEpicTask(EpicTask createdEpic);
    void createSubTask(SubTask createdSubTask);
    void deleteById(int searchId);
    void updateTask(Task task);
    void updateEpicTask(EpicTask epicTask);
    void updateSubTask(SubTask subTask);
    ArrayList<SubTask> showEpicsSubTasks(EpicTask searchEpic);
    HistoryManager getHistoryManager();
    TreeSet<Task> getPrioritizedTasks();


}
