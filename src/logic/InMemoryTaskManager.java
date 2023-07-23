package logic;


import entities.EpicTask;
import entities.Status;
import entities.SubTask;
import entities.Task;
import interfaces.HistoryManager;
import interfaces.TaskManager;

import java.time.Duration;
import java.time.Instant;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    int id = 0;


    private final HashMap<Integer, Task> tasks = new HashMap<>();

    private final HashMap<Integer, EpicTask> epics = new HashMap<>();

    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getHistory();

    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(task -> task.getStartTime().toEpochMilli()));


    @Override
    public Collection<Task> showTasksList() {

        return tasks.values();
    }

    @Override
    public Collection<EpicTask> showEpicTasksList() {
        return epics.values();
    }

    @Override
    public Collection<SubTask> showSubTasksList() {
        return subTasks.values();
    }


    @Override
    public void deleteAllTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task);
        }

        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Task task : epics.values()) {
            historyManager.remove(task);
        }

        epics.clear();

        deleteAllSubTasks();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Task task : subTasks.values()) {
            historyManager.remove(task);
        }

        subTasks.clear();

        for (EpicTask value : epics.values()) {
            value.getEpicsSubTasks().clear();
            updateEpicCondition(value);
        }
    }

    @Override
    public Task getById(int searchId) {

        if (tasks.containsKey(searchId)) {
            historyManager.add(tasks.get(searchId));
            return tasks.get(searchId);
        }

        if (epics.containsKey(searchId)) {
            historyManager.add(epics.get(searchId));
            return epics.get(searchId);
        }

        if (subTasks.containsKey(searchId)) {
            historyManager.add(subTasks.get(searchId));
            return subTasks.get(searchId);
        }
        return null;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public void createTask(Task createdTask) {
        for (Task task : prioritizedTasks) {
            if (!(createdTask.getEndTime().isBefore(task.getStartTime()) || task.getEndTime().isBefore(createdTask.getStartTime()))) {
                throw new IllegalStateException("время выполненения обьекта пересекается с другими");
            }
        }
        createdTask.setId(id);
        tasks.put(id, createdTask);
        id++;

        prioritizedTasks.add(createdTask);
    }

    @Override
    public void createEpicTask(EpicTask createdEpic) {
        createdEpic.setId(id);
        epics.put(id, createdEpic);
        id++;

    }

    @Override
    public void createSubTask(SubTask createdSubTask) {


        createdSubTask.setId(id);

        subTasks.put(id, createdSubTask);
        epics.get(createdSubTask.getEpicId()).getEpicsSubTasks().add(createdSubTask);
        updateEpicCondition(epics.get(createdSubTask.getEpicId()));
        id++;

        prioritizedTasks.add(createdSubTask);

    }

    @Override
    public void deleteById(int searchId) {
        if (epics.containsKey(searchId)) {
            for (SubTask epicsSubTask : epics.get(searchId).getEpicsSubTasks()) {
                subTasks.remove(epicsSubTask.getId());
                historyManager.remove(epicsSubTask);
            }
            historyManager.remove(epics.get(searchId));
            epics.remove(searchId);
        }
        if (subTasks.containsKey(searchId)) {
            SubTask subTask = subTasks.get(searchId);
            epics.get(subTask.getEpicId()).getEpicsSubTasks().remove(subTask);
            updateEpicCondition(epics.get(subTask.getEpicId()));
            historyManager.remove(subTasks.get(searchId));
            subTasks.remove(searchId);
        }
        if (tasks.containsKey(searchId)) {
            historyManager.remove(tasks.get(searchId));
            tasks.remove(searchId);
        }

    }

    @Override
    public void updateTask(Task task) {
        prioritizedTasks.remove(tasks.get(task.getId()));
        prioritizedTasks.add(task);

        tasks.put(task.getId(), task);

    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        epicTask.setId(id);
        epics.put(epicTask.getId(), epicTask);
        id++;
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        prioritizedTasks.remove(tasks.get(subTask.getId()));

        ArrayList<SubTask> epicsSubTasks = epics.get(subTask.getEpicId()).getEpicsSubTasks();
        subTasks.put(subTask.getId(), subTask);
        for (SubTask epicsSubTask : epicsSubTasks) {
            if (epicsSubTask.getId() == subTask.getId()) {
                epicsSubTasks.add(epicsSubTasks.indexOf(epicsSubTask), subTask);
                epicsSubTasks.remove(epicsSubTask);
            }
        }
        updateEpicCondition(epics.get(subTask.getEpicId()));

        prioritizedTasks.add(subTask);
    }

    @Override
    public ArrayList<SubTask> showEpicsSubTasks(EpicTask searchEpic) {
        return searchEpic.getEpicsSubTasks();
    }


    public void updateEpicCondition(EpicTask epic) {
        updateEpicTime(epic);
        updateEpicStatus(epic);
    }

    void updateEpicTime(EpicTask epic){
        Instant startTime = Instant.MAX;
        Instant endTime = Instant.MIN;
        for (SubTask epicsSubTask : epic.getEpicsSubTasks()) {
            if (startTime.isAfter(epicsSubTask.getStartTime())) {
                startTime = epicsSubTask.getStartTime();
            }
            if (endTime.isBefore(epicsSubTask.getEndTime())) {
                endTime = epicsSubTask.getEndTime();
            }
        }
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(Duration.between(startTime,endTime));
    }

    void updateEpicStatus(EpicTask epic){
        boolean isDone = true;
        boolean isNew = true;
        if (epic.getEpicsSubTasks().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        for (SubTask epicsSubTask : epic.getEpicsSubTasks()) {
            if (!epicsSubTask.getStatus().equals(Status.NEW)) {
                isNew = false;
                break;
            }
        }
        if (isNew) {
            epic.setStatus(Status.NEW);
            epics.put(epic.getId(), epic);
            return;

        }

        for (SubTask epicsSubTask : epic.getEpicsSubTasks()) {
            if (!epicsSubTask.getStatus().equals(Status.DONE)) {
                isDone = false;
                break;
            }
        }
        if (isDone) {
            epic.setStatus(Status.DONE);
            epics.put(epic.getId(), epic);
            return;
        }

        epic.setStatus(Status.IN_PROGRESS);
        epics.put(epic.getId(), epic);
    }


    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }
}
