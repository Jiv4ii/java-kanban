package logic;

import entities.Task;

import java.util.ArrayList;

public interface HistoryManager {
    public ArrayList<Task> getHistory();
    public void add(Task task);
}