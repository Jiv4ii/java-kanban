package logic;

import entities.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {


    private final ArrayList<Task> history = new ArrayList<>();

    private static final int maxHistorySize = 10;


    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }

    @Override
    public void add(Task task) {
        if (history.size() > maxHistorySize) {
            history.remove(0);
        }
        history.add(task);
    }
}
