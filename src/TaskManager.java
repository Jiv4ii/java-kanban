import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    int id = 0;

    HashMap<Integer, Task> tasks;
    HashMap<Integer, EpicTask> epics;
    HashMap<Integer, SubTask> subTasks;

    TaskManager(){
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
    }


    Collection<Task> showTasksList() {
        return tasks.values();
    }

    Collection<EpicTask> showEpicTasksList() {
        return epics.values();
    }

    Collection<SubTask> showSubTasksList() {
        return subTasks.values();
    }

    void deleteAllTasks() {
        tasks.clear();
    }

    void deleteAllEpics() {
        epics.clear();
    }

    void deleteAllSubTasks() {
        subTasks.clear();
    }

    Object getById(int searchId) {

        if (tasks.containsKey(searchId)) {
            return tasks.get(searchId);
        }

        if (epics.containsKey(searchId)) {
            return epics.get(searchId);
        }

        if (subTasks.containsKey(searchId)) {
            return subTasks.get(searchId);
        }
        return null;
    }

    void createTask(Task createdTask) {
        createdTask.id = id;
        tasks.put(id, createdTask);
        id++;

    }

    void createEpicTask(EpicTask createdEpic) {
        createdEpic.id = id;
        epics.put(id, createdEpic);
        id++;

    }

    void createSubTask(SubTask createdSubTask) {
        createdSubTask.id = id;
        subTasks.put(id, createdSubTask);
        epics.get(createdSubTask.epicId).epicsSubTasks.add(createdSubTask);
        updateEpicStatus(epics.get(createdSubTask.epicId));
        id++;

    }

    void deleteById(int searchId) {
        if (tasks.containsKey(searchId)) {
            tasks.remove(searchId);
            return;
        }
        if (epics.containsKey(searchId)) {
            for (SubTask epicsSubTask : epics.get(searchId).epicsSubTasks) {
                subTasks.remove(epicsSubTask.id);
            }
            epics.remove(searchId);
            return;
        }
        if (subTasks.containsKey(searchId)) {
            subTasks.remove(searchId);
        }
    }

    void updateTask(Task task){
        tasks.put(task.id, task);
    }

    void updateEpicTask(EpicTask epicTask){
        epics.put(epicTask.id, epicTask);
    }

    void updateSubTask(SubTask subTask){
        subTasks.put(subTask.id, subTask);
    }

    ArrayList<SubTask> getEpicsSubTasks(EpicTask searchEpic) {
        return searchEpic.epicsSubTasks;
    }

    void updateEpicStatus(EpicTask epic) {

        boolean isDone = true;
        boolean isNew = true;
        if (epic.epicsSubTasks.isEmpty()) {
            epic.status = "NEW";
            return;
        }

        for (SubTask epicsSubTask : epic.epicsSubTasks) {
            if (!epicsSubTask.status.equals("NEW")) {
                isNew = false;
                break;
            }
        }
        if (isNew) {
            epic.status = "NEW";
            epics.put(epic.id, epic);
            return;

        }

        for (SubTask epicsSubTask : epic.epicsSubTasks) {
            if (!epicsSubTask.status.equals("DONE")) {
                isDone = false;
                break;
            }
        }
        if (isDone) {
            epic.status = "DONE";
            epics.put(epic.id, epic);
            return;
        }

        epic.status = "IN_PROGRESS";
        epics.put(epic.id, epic);


    }


}
