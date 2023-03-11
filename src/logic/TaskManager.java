package logic;


import entities.EpicTask;
import entities.SubTask;
import entities.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;



public class TaskManager {
    int id = 0;

    HashMap<Integer, Task> tasks;
    HashMap<Integer, EpicTask> epics;
    HashMap<Integer, SubTask> subTasks;

    public  TaskManager(){
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
    }


   public Collection<Task> showTasksList() {

        return tasks.values();
    }

    public Collection<EpicTask> showEpicTasksList() {
        return epics.values();
    }

    public Collection<SubTask> showSubTasksList() {
        return subTasks.values();
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        deleteAllSubTasks();
    }

    public void deleteAllSubTasks() {
        subTasks.clear();
        for (EpicTask value : epics.values()) {
            value.getEpicsSubTasks().clear();
            updateEpicStatus(value);
        }
    }

    public Task getById(int searchId) {

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

    public void createTask(Task createdTask) {
        createdTask.setId(id);
        tasks.put(id, createdTask);
        id++;

    }

    public void createEpicTask(EpicTask createdEpic) {
        createdEpic.setId(id);
        epics.put(id, createdEpic);
        id++;

    }

    public void createSubTask(SubTask createdSubTask) {
        createdSubTask.setId(id);;
        subTasks.put(id, createdSubTask);
        epics.get(createdSubTask.getEpicId()).getEpicsSubTasks().add(createdSubTask);
        updateEpicStatus(epics.get(createdSubTask.getEpicId()));
        id++;

    }

    public void deleteById(int searchId) {
        if (epics.containsKey(searchId)) {
            for (SubTask epicsSubTask : epics.get(searchId).getEpicsSubTasks()) {
                subTasks.remove(epicsSubTask.getId());
            }
            epics.remove(searchId);
        }
        if (subTasks.containsKey(searchId)) {
            SubTask subTask = subTasks.get(searchId);
            epics.get(subTask.getEpicId()).getEpicsSubTasks().remove(subTask);
            updateEpicStatus(epics.get(subTask.getEpicId()));
            subTasks.remove(searchId);
        }
        tasks.remove(searchId);
    }

    public void updateTask(Task task){
        tasks.put(task.getId(), task);
    }

    public void updateEpicTask(EpicTask epicTask){
        epics.put(epicTask.getId(), epicTask);
    }

    public void updateSubTask(SubTask subTask){
        ArrayList<SubTask> epicsSubTasks= epics.get(subTask.getEpicId()).getEpicsSubTasks();
        subTasks.put(subTask.getId(), subTask);
        for (SubTask epicsSubTask : epicsSubTasks) {
            if (epicsSubTask.getId() == subTask.getId()) {
                epicsSubTasks.add(epicsSubTasks.indexOf(epicsSubTask), subTask);
                epicsSubTasks.remove(epicsSubTask);
            }
        }
        updateEpicStatus(epics.get(subTask.getEpicId()));
    }

    public ArrayList<SubTask> showEpicsSubTasks(EpicTask searchEpic) {
        return searchEpic.getEpicsSubTasks();
    }

    public void updateEpicStatus(EpicTask epic) {

        boolean isDone = true;
        boolean isNew = true;
        if (epic.getEpicsSubTasks().isEmpty()) {
            epic.setStatus("NEW");
            return;
        }

        for (SubTask epicsSubTask : epic.getEpicsSubTasks()) {
            if (!epicsSubTask.getStatus().equals("NEW")) {
                isNew = false;
                break;
            }
        }
        if (isNew) {
            epic.setStatus("NEW");
            epics.put(epic.getId(), epic);
            return;

        }

        for (SubTask epicsSubTask : epic.getEpicsSubTasks()) {
            if (!epicsSubTask.getStatus().equals("DONE")) {
                isDone = false;
                break;
            }
        }
        if (isDone) {
            epic.setStatus("DONE");
            epics.put(epic.getId(), epic);
            return;
        }

        epic.setStatus("IN_PROGRESS");
        epics.put(epic.getId(), epic);


    }


}
