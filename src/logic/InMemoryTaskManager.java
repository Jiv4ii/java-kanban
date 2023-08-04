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
    protected int id = 0;


    protected final HashMap<Integer, Task> tasks = new HashMap<>();

    protected final HashMap<Integer, EpicTask> epics = new HashMap<>();

    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    protected final HistoryManager historyManager = Managers.getHistory();

    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(task -> task.getStartTime().toEpochMilli()));


    @Override
    public ArrayList<Task> showTasksList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<EpicTask> showEpicTasksList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> showSubTasksList() {

        return new ArrayList<>(subTasks.values());
    }


    @Override
    public void deleteAllTasks() {
        for (Task task : tasks.values()) {
            if (historyManager.getHistory().contains(task)) {
                historyManager.remove(task);
            }
        }

        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Task task : epics.values()) {
            if (historyManager.getHistory().contains(task)) {
                historyManager.remove(task);
            }
        }

        epics.clear();

        deleteAllSubTasks();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Task task : subTasks.values()) {
            if (historyManager.getHistory().contains(task)) {
                historyManager.remove(task);
            }
        }

        subTasks.clear();

        for (EpicTask value : epics.values()) {
            value.getEpicsSubTasks().clear();
            updateEpicCondition(value);
        }
    }

    @Override
    public Task getById(int searchId) {
        if (searchId > id || searchId < 0){
            throw new IllegalArgumentException("Некорректный Id");
        }


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
        throw new IllegalStateException("Задача не найдена");
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void createTask(Task createdTask) {
        for (Task task : prioritizedTasks) {
            if (!(createdTask.getEndTime().isBefore(task.getStartTime()) || task.getEndTime().isBefore(createdTask.getStartTime()))) {
                throw new IllegalStateException("Время выполненения задачи пересекается с другими");
            }
        }
        if (createdTask.getId() == null) {
            createdTask.setId(id);
        }

        tasks.put(createdTask.getId(),createdTask);
        id++;


        prioritizedTasks.add(createdTask);
    }

    @Override
    public void createEpicTask(EpicTask createdEpic) {
        if (createdEpic.getId() == null) {
            createdEpic.setId(id);
        }

        epics.put(createdEpic.getId(),createdEpic);
        id++;

    }

    @Override
    public void createSubTask(SubTask createdSubTask) {
        for (Task task : prioritizedTasks) {
            if (!(createdSubTask.getEndTime().isBefore(task.getStartTime()) || task.getEndTime().isBefore(createdSubTask.getStartTime()))) {
                throw new IllegalStateException("Время выполненения задачи пересекается с другими");
            }
        }


        if (createdSubTask.getId() == null) {
            createdSubTask.setId(id);
        }

        subTasks.put(createdSubTask.getId(),createdSubTask);
        id++;
        epics.get(createdSubTask.getEpicId()).getEpicsSubTasks().add(createdSubTask);
        updateEpicCondition(epics.get(createdSubTask.getEpicId()));

        prioritizedTasks.add(createdSubTask);

    }

    @Override
    public void deleteById(int searchId) {
        if (tasks.get(searchId) == null && epics.get(searchId) == null &&  subTasks.get(searchId) == null){
            throw  new IllegalStateException("Задача не найдена");
        }

        if (epics.containsKey(searchId)) {
            for (SubTask epicsSubTask : epics.get(searchId).getEpicsSubTasks()) {
                subTasks.remove(epicsSubTask.getId());
                if (historyManager.getHistory().contains(epicsSubTask)) {
                    historyManager.remove(epicsSubTask);
                }
            }
            if (historyManager.getHistory().contains(epics.get(searchId))) {
                historyManager.remove(epics.get(searchId));
            }
            epics.remove(searchId);
        }
        if (subTasks.containsKey(searchId)) {
            if (historyManager.getHistory().contains(tasks.get(searchId))) {
                historyManager.remove(tasks.get(searchId));
            }
            SubTask subTask = subTasks.get(searchId);
            epics.get(subTask.getEpicId()).getEpicsSubTasks().remove(subTask);
            updateEpicCondition(epics.get(subTask.getEpicId()));
            subTasks.remove(searchId);
        }
        if (tasks.containsKey(searchId)) {
            if (historyManager.getHistory().contains(tasks.get(searchId))) {
                historyManager.remove(tasks.get(searchId));
            }
            tasks.remove(searchId);
        }

    }

    @Override
    public void updateTask(Task updatedTask) {
        Task original = tasks.get(updatedTask.getId());
        if (original == null){
            throw new IllegalStateException("Обновляемый элемент не был создан или Id не верен");
        }
        prioritizedTasks.remove(tasks.get(updatedTask.getId()));
        for (Task task : prioritizedTasks) {
            if (!(updatedTask.getEndTime().isBefore(task.getStartTime()) || task.getEndTime().isBefore(updatedTask.getStartTime()))) {
                prioritizedTasks.add(original);
                throw new IllegalStateException("Время выполненения задачи пересекается с другими");
            }
        }

        prioritizedTasks.add(updatedTask);

        tasks.put(updatedTask.getId(), updatedTask);

    }

    @Override
    public void updateEpicTask(EpicTask updatedEpicTask) {
        EpicTask original = epics.get(updatedEpicTask.getId());
        if (original == null){
            throw new IllegalStateException("Обновляемый элемент не был создан или Id некорректен");
        }
        for (SubTask epicsSubTask : original.getEpicsSubTasks()) {
            updatedEpicTask.getEpicsSubTasks().add(epicsSubTask);
        }
        updateEpicCondition(updatedEpicTask);
        epics.put(updatedEpicTask.getId(), updatedEpicTask);
    }

    @Override
    public void updateSubTask(SubTask updatedSubTask) {
        SubTask original = subTasks.get(updatedSubTask.getId());
        if (original == null){
            throw new IllegalStateException("Обновляемый элемент не был создан или Id некорректен");
        }
        prioritizedTasks.remove(subTasks.get(updatedSubTask.getId()));
        for (Task task : prioritizedTasks) {
            if (!(updatedSubTask.getEndTime().isBefore(task.getStartTime()) || task.getEndTime().isBefore(updatedSubTask.getStartTime()))) {
                prioritizedTasks.add(original);
                throw new IllegalStateException("Время выполненения задачи пересекается с другими");
            }
        }


        ArrayList<SubTask> epicsSubTasks = epics.get(updatedSubTask.getEpicId()).getEpicsSubTasks();
        subTasks.put(updatedSubTask.getId(), updatedSubTask);
        Iterator<SubTask> iterator = epicsSubTasks.iterator();
        SubTask oldSub = null;
            for (SubTask epicsSubTask : epicsSubTasks) {
                if (epicsSubTask.getId() == updatedSubTask.getId()) {
                    oldSub = epicsSubTask;
                    break;
                }
            }
        epicsSubTasks.add(epicsSubTasks.indexOf(oldSub), updatedSubTask);
        epicsSubTasks.remove(oldSub);

        updateEpicCondition(epics.get(updatedSubTask.getEpicId()));

        prioritizedTasks.add(updatedSubTask);
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

    public int getId() {
        return id;
    }
}
